package com.example.sp.maxsat.DataProviders;

import com.example.sp.maxsat.Entities.ZoneEntity;

import java.util.ArrayList;
import java.util.List;

public class ZoneServiceUnitTestDataProvider {

    public static final Long zoneId = 1L;
    public static ZoneEntity zoneEntity;
    public static ZoneEntity zoneEntity1;
    public static ZoneEntity zoneEntity2;
    public static ZoneEntity zoneEntity3;
    public static ZoneEntity zoneEntity4;
    public static ZoneEntity zoneEntity5;
    public static ZoneEntity zoneEntity6;
    public static ZoneEntity zoneEntity7;
    public static List<ZoneEntity> zoneEntities;
    static {
        zoneEntity = ZoneEntity.builder().zoneId(zoneId).zoneType("testType").occupiedRatio(0.6).attractivenessRatio(0.6)
                .requestRatio(0.6).build();
        zoneEntity1 = ZoneEntity.builder().zoneId(2L).zoneType("testType1").occupiedRatio(0.7).attractivenessRatio(0.7)
                .requestRatio(0.7).build();
        zoneEntity2 = ZoneEntity.builder().zoneId(3L).zoneType("testType2").occupiedRatio(0.7).attractivenessRatio(0.6)
                .requestRatio(0.6).build();
        zoneEntity3 = ZoneEntity.builder().zoneId(3L).zoneType("testType2").occupiedRatio(0.6).attractivenessRatio(0.6)
                .requestRatio(0.6).build();
        zoneEntity4 = ZoneEntity.builder().zoneId(4L).zoneType("testType3").occupiedRatio(0.6).attractivenessRatio(0.7)
                .requestRatio(0.6).build();
        zoneEntity5 = ZoneEntity.builder().zoneId(4L).zoneType("testType3").occupiedRatio(0.6).attractivenessRatio(0.6)
                .requestRatio(0.6).build();
        zoneEntity6 = ZoneEntity.builder().zoneId(5L).zoneType("testType4").occupiedRatio(0.6).attractivenessRatio(0.6)
                .requestRatio(0.7).build();
        zoneEntity7 = ZoneEntity.builder().zoneId(5L).zoneType("testType4").occupiedRatio(0.6).attractivenessRatio(0.6)
                .requestRatio(0.6).build();
        zoneEntities = new ArrayList<>();
        zoneEntities.add(zoneEntity);
        zoneEntities.add(zoneEntity1);

    }
}
