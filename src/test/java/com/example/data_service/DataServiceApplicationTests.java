package com.example.data_service;

import com.example.data_service.Config.DBContext;
import com.example.data_service.Mapper.PDataMapper;
import com.example.data_service.Service.Impl.PDataImpl;
import com.example.data_service.domain.PData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;

@SpringBootTest
class DataServiceApplicationTests {

    @Autowired
    private PDataImpl pDataimpl;
    @Autowired
    PDataMapper pDataMapper;

    @Test
    void contextLoads() throws InterruptedException, SQLException {
        pDataimpl.BatchInsertPData();
    }

}
