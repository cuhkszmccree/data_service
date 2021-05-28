package com.example.data_service.Config;

import com.example.data_service.Constant.DBTypeEnum;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class DBContext {
    private static final ThreadLocal<DBTypeEnum> dbcontext = new ThreadLocal<>();
    private static void set(DBTypeEnum dbType){
        dbcontext.set(dbType);
    }
    public static DBTypeEnum get(){
        return dbcontext.get();
    }
    public static void set_master(){
        set(DBTypeEnum.Master);
        log.info("change to master");
    }
    public static void set_slave(){
        set(DBTypeEnum.Slave);
        log.info("change to slave");
    }

}
