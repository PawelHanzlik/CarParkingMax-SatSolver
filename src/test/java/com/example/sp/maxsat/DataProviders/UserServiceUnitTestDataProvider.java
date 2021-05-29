package com.example.sp.maxsat.DataProviders;

import com.example.sp.maxsat.Entities.UserEntity;
import com.example.sp.maxsat.Entities.ZoneEntity;

import java.util.ArrayList;
import java.util.List;

public class UserServiceUnitTestDataProvider {

    public static final Long userId = 1L;
    public static UserEntity userEntity;
    public static UserEntity userEntity1;
    public static UserEntity userEntity2;
    public static UserEntity userEntity3;
    public static UserEntity userEntity4;
    public static UserEntity userEntity5;
    public static ZoneEntity zoneEntity;
    public static List<UserEntity> userEntities;

    static {
        userEntity = UserEntity.builder().userId(userId).carSize("small").preferableZone(1L).name("test").surname("test")
                .age(30).build();
        userEntity1 = UserEntity.builder().userId(2L).carSize("small").preferableZone(1L).name("new_test").surname("new_test")
                .age(40).build();
        userEntity2 = UserEntity.builder().userId(3L).carSize("small").preferableZone(1L).name("test").surname("test")
                .age(35).build();
        userEntity3 = UserEntity.builder().userId(3L).carSize("medium").preferableZone(1L).name("test").surname("test")
                .age(35).build();
        userEntity4 = UserEntity.builder().userId(4L).carSize("small").preferableZone(1L).name("test").surname("test")
                .age(30).build();
        userEntity5 = UserEntity.builder().userId(4L).carSize("small").preferableZone(3L).name("test").surname("test")
                .age(30).build();
        zoneEntity = ZoneEntity.builder().zoneId(3L).zoneType("testType").occupiedRatio(0.6).attractivenessRatio(0.6)
                .requestRatio(0.6).build();
        userEntities = new ArrayList<>();
        userEntities.add(userEntity);
        userEntities.add(userEntity1);

    }
}
