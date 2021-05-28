package com.example.data_service.BackGround;

import com.example.data_service.Config.DBContext;
import com.example.data_service.Constant.MysqlConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.DriverManager;
import java.sql.SQLException;


@Slf4j
@Component
public class CurrentState {
    @Autowired
    ConnectionState connectionState;
    public int Master_state() {
        System.out.println("先前主库状态 " + connectionState.getMaster_state().get());
        if (!connectionState.getMaster_state().get()) {
            if (connectionState.getSlave_state().get() == false) {
                log.error("mysql service down");
                return 2;
            } else {
                log.warn("mysql master down");
                DBContext.set_slave();
                return 1;
            }
        } else {
            Boolean master_flag = false;
            try {
                DriverManager.getConnection(MysqlConstant.Master_Url, "root", "root");
                master_flag = true;
                System.out.println("当前主库状态良好");
                return 0;
            } catch (SQLException e) {
                connectionState.setMaster_state(false);
                log.warn("mysql master down");
                System.out.println("当前主库状态糟糕");
            }
            if (!master_flag) {
                try {
                    DriverManager.getConnection(MysqlConstant.Slave_Url, "root", "root");
                    connectionState.setSlave_state(true);
                    DBContext.set_slave();
                    return 1;
                } catch (SQLException e) {
                    connectionState.setSlave_state(false);
                    log.error("mysql service down");
                    return 2;
                }
            }
            return 0;
        }
    }

    public int Slave_State(){
        System.out.println("先前从库状态 " + connectionState.getSlave_state().get());
        if (!connectionState.getSlave_state().get()) {
            if (connectionState.getMaster_state().get() == false) {
                log.error("mysql service down");
                return 2;
            } else {
                log.warn("mysql slave down");
                DBContext.set_master();
                return 1;
            }
        } else {
            Boolean slave_flag = false;
            try {
                DriverManager.getConnection(MysqlConstant.Slave_Url, "root", "root");
                slave_flag = true;
                System.out.println("当前从库状态良好");
                return 0;
            } catch (SQLException e) {
                connectionState.setSlave_state(false);
                log.warn("mysql slave down");
                System.out.println("当前从库状态糟糕");
            }
            if (!slave_flag) {
                try {
                    DriverManager.getConnection(MysqlConstant.Master_Url, "root", "root");
                    connectionState.setMaster_state(true);
                    DBContext.set_master();
                    return 1;
                } catch (SQLException e) {
                    connectionState.setMaster_state(false);
                    log.error("mysql service down");
                    return 2;
                }
            }
            return 0;
        }
    }
}

