package com.example.data_service.BackGround;

import com.example.data_service.Constant.MysqlConstant;
import org.springframework.stereotype.Component;

import java.sql.DriverManager;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class ConnectionState {
    private AtomicBoolean master_state;
    private AtomicBoolean slave_state;
    public ConnectionState(){
        master_state = new AtomicBoolean(true);
        slave_state = new AtomicBoolean(true);
        master_state.set(true);
        slave_state.set(true);
        new Thread(() -> {
            while(true){
                System.out.println("尝试连接主库");
                try{
                    DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306","root","root");
                    master_state.set(true);
                }
                catch (Exception e){
                    master_state.set(false);
                }
                try {
                    Thread.sleep(MysqlConstant.TryConnect);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(() -> {
            while(true){
                System.out.println("尝试连接从库");
                try{
                    DriverManager.getConnection("jdbc:mysql://127.0.0.1:3307","root","root");
                    slave_state.set(true);
                }
                catch (Exception e){
                    slave_state.set(false);
                }
                try {
                    Thread.sleep(MysqlConstant.TryConnect);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public AtomicBoolean getMaster_state() {
        return master_state;
    }
    public AtomicBoolean getSlave_state() {
        return slave_state;
    }

    public void setMaster_state(Boolean bool) {
        this.master_state.set(bool);
    }
    public void setSlave_state(Boolean bool) {
        this.slave_state.set(bool);
    }
}
