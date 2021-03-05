package com.example.data_service.Service.Impl;

import com.example.data_service.Mapper.PDataMapper;
import com.example.data_service.Service.PDataService;
import com.example.data_service.domain.PData;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class PDataImpl implements PDataService {
    @Autowired
    private PDataMapper pDataMapper;
    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public void insertPData(int data) {
        PData pData = PData.builder().Permanent_Data(data).build();
        pDataMapper.Insert(pData);
    }

    @Override
    public void updatePData(int id, int val){
        PData pData = PData.builder().id(id).Permanent_Data(val).build();
        pDataMapper.UpdateById(pData);
    }

    @Override
    public List<PData> getAllPData(){
        return pDataMapper.Select_All_PData();
    }


    @Override
    public PData getPDatabyID(int id) throws InterruptedException {
        String str_id = String.valueOf(id);
        //System.out.println(str_id.getClass());
        if(!redisTemplate.hasKey(str_id)) {     //Redis不支持直接存储Java数据类型，需要先转换为String
            //System.out.println("true");
            if (!redisTemplate.opsForValue().setIfAbsent("PData_search_lock", "1", 60, TimeUnit.SECONDS)) {  //双重校验加互斥锁，防止缓存击穿
                //System.out.println("before get result");
                PData result = pDataMapper.Select_PDatabyID(id);
                //System.out.println(result);
                //System.out.println(result.getClass());
                if(result == null) {
                    redisTemplate.opsForValue().set(str_id, result, 120, TimeUnit.SECONDS);
                }        //使用了空指针加入redis，防止缓存穿透。另一种方法是bloomfilter
                else{
                    redisTemplate.opsForValue().set(str_id, result, 300, TimeUnit.SECONDS);
                }
                return result;
            } else {
                //System.out.println("enter");
                Thread.sleep(500);
                return getPDatabyID(id);
            }
        }
        else{
            //System.out.println("get success");
            //System.out.println(redisTemplate.opsForValue().get(str_id));
            //System.out.println(redisTemplate.opsForValue().get(str_id).getClass());
            return (PData) redisTemplate.opsForValue().get(str_id);
        }
    }
}
