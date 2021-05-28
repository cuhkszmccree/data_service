package com.example.data_service.Service.Impl;

import com.example.data_service.Annotation.Master;
import com.example.data_service.Annotation.Slave;
import com.example.data_service.BackGround.CurrentState;
import com.example.data_service.Mapper.UserMapper;
import com.example.data_service.domain.PData;
import com.example.data_service.domain.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//@Service
//public class UserImpl {
//    @Autowired
//    UserMapper userMapper;
//    @Autowired
//    CurrentState currentState;
//
//    @Slave
//    public UserDTO Select_byusername(){
//        int state = currentState.Slave_State();
//        if (state != 2) {
//            try {
//                UserDTO User = userMapper.Select_byUserName()
//                return res;
//            } catch (Exception e) {
//                log.error("Mysql instant error");
//                log.error(e.toString());
//                return null;
//            }
//        }
//        else{
//            log.error("Mysql service down, please read again");
//            return null;
//        }
//    }
//
//    @Master
//    public void insert(){
//
//    }
//
//}
