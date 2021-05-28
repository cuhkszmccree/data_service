package com.example.data_service.BackGround;

import com.example.data_service.Config.DBContext;
import com.example.data_service.Constant.MysqlConstant;
import com.example.data_service.Mapper.PDataMapper;
import com.example.data_service.domain.PData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


@Component
@Slf4j
public class ReWrite {
    public ReWrite(@Autowired BackUp backUp,
                   @Autowired ConnectionState connectionState,
                   @Autowired PDataMapper pDataMapper) {
        new Thread(() -> {
            while (true) {
                System.out.println("重写开始");
                if (connectionState.getMaster_state().get()) {
                    DBContext.set_master();
                } else if (connectionState.getSlave_state().get()) {
                    DBContext.set_slave();
                } else {
                    log.error("Mysql service still down");
                    log.error("ReWrite Failed");
                    try {
                        Thread.sleep(MysqlConstant.ReWrite);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                }
                List Current_Write_List = new ArrayList(backUp.getWrite_List());
                if (Current_Write_List.isEmpty()) {
                    try {
                        Thread.sleep(MysqlConstant.ReWrite);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                }
                backUp.getWrite_List().clear();      //清空原有列表
                Boolean flag = true;
                try {
                    pDataMapper.BatchInsert(Current_Write_List); // 使用事务批量插入
                } catch (Exception e) {
                    log.error(e.toString());
                    log.error("ReWrite Failed");
                    flag = false;
                } finally {
                    if (!flag) {
                        synchronized (backUp.getWrite_List()) {
                            for (Object pdata : Current_Write_List) {
                                backUp.getWrite_List().add(pdata);
                            }
                        }
                    }
                }
                try {
                    Thread.sleep(MysqlConstant.ReWrite);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(() -> {
            while (true) {
                System.out.println("重更新开始");
                if (connectionState.getMaster_state().get()) {
                    DBContext.set_master();
                } else if (connectionState.getSlave_state().get()) {
                    DBContext.set_slave();
                } else {
                    log.error("Mysql service still down");
                    log.error("ReUpdate Failed");
                    try {
                        Thread.sleep(MysqlConstant.ReUpdate);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                }
                List Current_Update_List = new ArrayList(backUp.getUpdate_List());
                if (Current_Update_List.isEmpty()) {
                    try {
                        Thread.sleep(MysqlConstant.ReUpdate);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                }
                backUp.getUpdate_List().clear();      //清空原有列表
                Boolean flag = true;
                try {
                    pDataMapper.BatchUpdate(Current_Update_List); // 使用事务批量更新
                } catch (Exception e) {
                    log.error(e.toString());
                    log.error("ReUpdate Failed");
                    flag = false;
                } finally {
                    if (!flag) {
                        synchronized (backUp.getUpdate_List()) {
                            for (Object pdata : Current_Update_List) {
                                backUp.getUpdate_List().add(pdata);
                            }
                        }
                    }
                }
                try {
                    Thread.sleep(MysqlConstant.ReUpdate);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(() -> {
            while (true) {
                System.out.println("重删除开始");
                if (connectionState.getMaster_state().get()) {
                    DBContext.set_master();
                } else if (connectionState.getSlave_state().get()) {
                    DBContext.set_slave();
                } else {
                    log.error("Mysql service still down");
                    log.error("ReDelete Failed");
                    try {
                        Thread.sleep(MysqlConstant.ReDelete);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                }
                List Current_Delete_List = new ArrayList(backUp.getDelete_List());
                if (Current_Delete_List.isEmpty()) {
                    try {
                        Thread.sleep(MysqlConstant.ReDelete);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                }
                backUp.getDelete_List().clear();      //清空原有列表
                Boolean flag = true;
                try {
                    pDataMapper.BatchDelete(Current_Delete_List); // 使用事务批量更新
                } catch (Exception e) {
                    log.error(e.toString());
                    log.error("ReDelete Failed");
                    flag = false;
                } finally {
                    if (!flag) {
                        synchronized (backUp.getDelete_List()) {
                            for (Object id : Current_Delete_List) {
                                backUp.getDelete_List().add(id);
                            }
                        }
                    }
                }
                try {
                    Thread.sleep(MysqlConstant.ReDelete);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
