package com.example.sp.maxsat.Services;

import com.example.sp.maxsat.Entities.ParkingLotEntity;
import com.example.sp.maxsat.Exceptions.Classes.NoSuchParkingLotException;

import java.util.List;

public interface ParkingLotService {

    ParkingLotEntity getParkingLot(Long parkingLotId) throws NoSuchParkingLotException;
    List<ParkingLotEntity> getAllParkingLots();
    ParkingLotEntity addParkingLot(ParkingLotEntity parkingLot);
    void deleteParkingLot(Long parkingLotId) throws NoSuchParkingLotException;
    void changeParkingLotOccupancy(Long parkingLotId, Integer newOccupancy) throws NoSuchParkingLotException;
}
