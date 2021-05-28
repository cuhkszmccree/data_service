package com.example.data_service.Controller;

import com.example.data_service.Service.PDataService;
import com.example.data_service.domain.PData;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PDataController {
    @Autowired
    private PDataService pDataService;
    @Autowired
    private RedissonClient redissonClient;

    @RequestMapping(value = "/pdata_all_search", method = RequestMethod.GET)    //永久数据查找
    //@HystrixCommand(fallbackMethod = "GetAllFallback")
    public String SearchPData(){
        try {
            List<PData> result = pDataService.getAllPData();
            System.out.println(result);
            return "Search Complete";
        }
        catch (Exception e){
            System.out.println(e);
            return "Search Failed";
        }
    }

    @RequestMapping(value = "/pdata_searchbyid", method = RequestMethod.POST)    //永久数据查找
    //@HystrixCommand(fallbackMethod = "GetByIdFallback")
    public String SearchPDataById(@RequestBody int id){
        try {
            PData result = pDataService.getPDatabyID(id);
            System.out.println(result);
            return "Search Complete";
        }
        catch (Exception e){
            System.out.println(e);
            return "Search Failed";
        }
    }

    @RequestMapping(value = "/pdatainsert", method = RequestMethod.POST)     //永久数据插入
    //@HystrixCommand(fallbackMethod = "InsertFallback")
    public String InsertPData(@RequestBody int data){
        RLock lock = redissonClient.getLock("insert");
        try {
            lock.lock();
            pDataService.insertPData(data);
            return "Insert Complete";
        }
        catch (Exception e){
            System.out.println(e);
            return "Insert Failed";
        }
        finally {
            lock.unlock();
        }
    }

    @RequestMapping(value = "/pdataupdate", method = RequestMethod.POST)      //永久数据更新
    //@HystrixCommand(fallbackMethod = "UpdateFallback")
    public String UpdatePData(@RequestBody PData pData){
        RLock lock = redissonClient.getLock("update");
        try {
            lock.lock();
            int id = pData.getId();
            int val = pData.getPermanent_Data();
            pDataService.updatePData(id,val);
            return "Update Complete";
        }
        catch (Exception e){
            System.out.println(e);
            return "Update Failed";
        }
        finally {
            lock.unlock();
        }
    }

    @RequestMapping(value = "pdatadelete", method = RequestMethod.DELETE)       //永久数据删除
    //@HystrixCommand(fallbackMethod = "DeleteFallback")
    public String DeletePData(@RequestBody int id){
        RLock lock = redissonClient.getLock("delete");
        try {
            lock.lock();
            pDataService.deletePData(id);
            return "Delete Complete";
        }
        catch (Exception e){
            System.out.println(e);
            return "Delete Failed";
        }
        finally {
            lock.unlock();
        }
    }

    public String GetByIdFallback(@RequestBody int id){
        return "Search service timeout";
    }

    public String UpdateFallback(@RequestBody PData pData){ return "Update service timeout"; }

    public String GetAllFallback(){ return "Search all service timeout"; }

    public String InsertFallback(@RequestBody int data){ return "Insert service timeout"; }

    public String DeleteFallback(@RequestBody int id){ return "Delete service timeout"; }
}
