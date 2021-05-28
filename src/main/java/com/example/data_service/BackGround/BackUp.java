package com.example.data_service.BackGround;


import com.example.data_service.domain.PData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Component
public class BackUp {
    private List Write_List; //Write lack
    private List Update_List;  //Update lack
    private List Delete_List;  //Delete lack
    public BackUp(){
        Write_List = Collections.synchronizedList(new ArrayList<>());
        Update_List = Collections.synchronizedList(new ArrayList<>());
        Delete_List = Collections.synchronizedList(new ArrayList<>());
    }

    public List getUpdate_List() {
        return Update_List;
    }

    public List getWrite_List() {
        return Write_List;
    }

    public void addWrite_List(PData pData){
        Write_List.add(pData);
    }

    public void addUpdate_List(PData pData){
        Update_List.add(pData);
    }

    public List getDelete_List() {return Delete_List; }

    public void addDelete_List(int id) { Delete_List.add(id); }
}
