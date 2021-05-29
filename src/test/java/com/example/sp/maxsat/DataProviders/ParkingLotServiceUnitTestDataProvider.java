package com.example.sp.maxsat.DataProviders;

import com.example.sp.maxsat.Entities.ParkingLotEntity;

import java.util.ArrayList;
import java.util.List;

public class ParkingLotServiceUnitTestDataProvider {

    public static final Long parkingLotId = 1L;
    public static ParkingLotEntity parkingLotEntity;
    public static ParkingLotEntity parkingLotEntity1;
    public static ParkingLotEntity parkingLotEntity2;
    public static ParkingLotEntity parkingLotEntity3;
    public static List<ParkingLotEntity> parkingLotEntities;
    static {
        parkingLotEntity = ParkingLotEntity.builder().parkingLotId(parkingLotId).zoneId(1L).isGuarded(true).isPaid(true).isForHandicapped(true)
                .freeSpaces(50).build();
        parkingLotEntity1 = ParkingLotEntity.builder().parkingLotId(2L).zoneId(2L).isGuarded(true).isPaid(true).isForHandicapped(true)
                .freeSpaces(50).build();
        parkingLotEntity2 = ParkingLotEntity.builder().parkingLotId(1L).zoneId(3L).isGuarded(true).isPaid(true).isForHandicapped(true)
                .freeSpaces(60).build();
        parkingLotEntity3 = ParkingLotEntity.builder().parkingLotId(3L).zoneId(4L).isGuarded(true).isPaid(true).isForHandicapped(true)
                .freeSpaces(60).build();
        parkingLotEntities = new ArrayList<>();
        parkingLotEntities.add(parkingLotEntity);
        parkingLotEntities.add(parkingLotEntity1);

    }
}
