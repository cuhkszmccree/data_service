package com.example.data_service.Service.Impl;

import com.example.data_service.Annotation.Master;
import com.example.data_service.Annotation.Slave;
import com.example.data_service.BackGround.BackUp;
import com.example.data_service.BackGround.ConnectionState;
import com.example.data_service.BackGround.CurrentState;
import com.example.data_service.Config.DBContext;
import com.example.data_service.Mapper.PDataMapper;
import com.example.data_service.Service.PDataService;
import com.example.data_service.domain.PData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class PDataImpl implements PDataService {
    @Autowired
    private PDataMapper pDataMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    CurrentState currentState;
    @Autowired
    BackUp backUp;



    @Master
    @Override
    public void insertPData(int data) {
        PData pData = PData.builder().Permanent_Data(data).build();
        int state = currentState.Master_state();
        if(state!=2) {
            Boolean bool = true;
            try {
                pDataMapper.Insert(pData);
            } catch (Exception e) {
                log.error("Mysql instant error");
                log.error(e.toString());
                bool = false;
            }
            finally {
                if(!bool){
                    backUp.addWrite_List(pData);
                }
            }
        }
        else{
            log.error("Mysql service down, data will be rewrite when mysql up");
            backUp.addWrite_List(pData);
        }
        //System.out.println("一次操作花费时间： " + (System.currentTimeMillis() - currentTime));
    }


    @Master
    public void BatchInsertPData(){
        List<PData> test_list = new ArrayList<>();
        for(int i=0;i<1000000;i++){
            test_list.add(PData.builder().Permanent_Data(111).build());
        }
        System.out.println(test_list);
        long current = System.currentTimeMillis();
        pDataMapper.BatchInsert(test_list);
        System.out.println(System.currentTimeMillis() - current);
    }

    @Master
    @Override
    public void updatePData(int id, int val){
        PData pData = PData.builder().id(id).Permanent_Data(val).build();
        int state = currentState.Master_state();
        if(state!=2) {
            Boolean bool = true;
            try {
                pDataMapper.UpdateById(pData);
            } catch (Exception e) {
                log.error("Mysql instant error");
                log.error(e.toString());
                bool = false;
            }
            finally {
                if(!bool){
                    backUp.addUpdate_List(pData);
                }
            }
        }
        else{
            log.error("Mysql service down, data will be reupdate when mysql up");
            backUp.addUpdate_List(pData);
        }
    }

    @Master
    @Override
    public void deletePData(int id){
        int state = currentState.Master_state();
        if(state!=2){
            Boolean bool = true;
            try {
                pDataMapper.DeleteData(id);
                redisTemplate.delete(String.valueOf(id));
            } catch (Exception e) {
                log.error("Mysql instant error");
                log.error(e.toString());
                bool = false;
            }
            finally {
                if(!bool){
                    backUp.addDelete_List(id);
                }
            }
        }
        else{
            log.error("Mysql service down, data will be redelete when mysql up");
            backUp.addDelete_List(id);
        }
    }

    @Slave
    @Override
    public List<PData> getAllPData(){
        int state = currentState.Slave_State();
        if(state!=2) {
            try {
                List<PData> res = pDataMapper.Select_All_PData();
                return res;
            } catch (Exception e) {
                log.error("Mysql instant error");
                log.error(e.toString());
                return null;
            }
        }
        else{
            log.error("Mysql service down, please read again");
            return null;
        }
    }

    @Slave
    @Override
    public PData getPDatabyID(int id) throws InterruptedException {
        int state = currentState.Slave_State();
        if (state != 2) {
            try {
                PData res = getbyid(id);
                return res;
            } catch (Exception e) {
                log.error("Mysql instant error");
                log.error(e.toString());
                return null;
            }
        }
        else{
            log.error("Mysql service down, please read again");
            return null;
        }
    }


    public PData getbyid(int id) throws InterruptedException {
        String str_id = String.valueOf(id);
        if(!redisTemplate.hasKey(str_id)) {     //Redis不支持直接存储Java数据类型，需要先转换为String
            if (!redisTemplate.opsForValue().setIfAbsent("PData_search_lock", "1", 1, TimeUnit.SECONDS)) {
                PData result;
                try {
                    System.out.println("尝试搜索");
                    result = pDataMapper.Select_PDatabyID(id);
                }
                catch (Exception e) {
                    log.error("Mysql instant error");
                    return null;
                }
                if (result == null) {
                    redisTemplate.opsForValue().set(str_id, null, 120, TimeUnit.SECONDS);
                }        //使用了空指针加入redis，防止缓存穿透。另一种方法是bloomfilter
                else {
                    redisTemplate.opsForValue().set(str_id, result, 300, TimeUnit.SECONDS);
                }
                return result;
            } else {
                Thread.sleep(500);
                return getPDatabyID(id);
            }
        }
        else{
            return (PData) redisTemplate.opsForValue().get(str_id);
        }
    }
}
