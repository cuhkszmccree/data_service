package com.example.data_service.Mapper;

import com.example.data_service.domain.PData;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface PDataMapper {
    @Insert("insert into PData(permanent_Data) values (#{Permanent_Data})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int Insert(PData pData);

    @Update("Update PData set permanent_Data=#{Permanent_Data} where id=#{id}")
    int UpdateById(PData pData);

    @Select("Select * from PData")
    List<PData> Select_All_PData();

    @Select("Select * from PData where id=#{id}")
    PData Select_PDatabyID(int id);
}
