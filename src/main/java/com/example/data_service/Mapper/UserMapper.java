package com.example.data_service.Mapper;

import com.example.data_service.domain.UserDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Insert("insert into User(username,password,role) values (#{username},#{password},#{role})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int Insert(UserDTO userDTO);

    @Select("Select * from User where username=#{username}")
    UserDTO Select_byUserName(String username);
}

