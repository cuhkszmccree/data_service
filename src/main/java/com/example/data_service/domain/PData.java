package com.example.data_service.domain;


import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class PData implements Serializable {                     //存储数据到Redis中时，必须将对象序列化，不能直接存储对象
    private int id;
    private int Permanent_Data;
}
