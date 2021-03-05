package com.example.data_service.Service;

import com.example.data_service.domain.PData;

import java.util.List;

public interface PDataService {
    void insertPData(int data);

    void updatePData(int id, int val);

    List<PData> getAllPData();

    PData getPDatabyID(int id) throws InterruptedException;
}
