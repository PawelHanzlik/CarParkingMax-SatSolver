package com.example.sp.maxsat.UnitTest;

import com.example.sp.maxsat.Entities.ParkingLotEntity;
import com.example.sp.maxsat.Exceptions.Classes.NoSuchParkingLotException;
import com.example.sp.maxsat.Repositories.ParkingLotRepository;
import com.example.sp.maxsat.Services.ParkingLotServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static com.example.sp.maxsat.DataProviders.ParkingLotServiceUnitTestDataProvider.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

public class ParkingLotServiceUnitTests {

    @InjectMocks
    ParkingLotServiceImpl parkingLotService;

    @Mock
    ParkingLotRepository parkingLotRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getParkingLotTest(){
        when(parkingLotRepository.findById(parkingLotId)).thenReturn(java.util.Optional.ofNullable(parkingLotEntity));
        ParkingLotEntity parkingLotEntity1 = this.parkingLotService.getParkingLot(parkingLotId);
        assertNotNull(parkingLotEntity1);
        assertEquals(1, parkingLotEntity1.getParkingLotId());
        assertEquals(1, parkingLotEntity1.getZoneId());
        assertEquals(true, parkingLotEntity1.getIsGuarded());
        assertEquals(true, parkingLotEntity1.getIsForHandicapped());
        assertEquals(true, parkingLotEntity1.getIsPaid());
        assertEquals(50, parkingLotEntity1.getFreeSpaces());
    }

    @Test
    void getParkingLotNoSuchParkingLotExceptionTest(){
        when(parkingLotRepository.findById(parkingLotId)).thenReturn(Optional.empty());
        assertThrows(NoSuchParkingLotException.class, () -> parkingLotService.getParkingLot(parkingLotId));
    }

    @Test
    void getAllParkingLots(){
        when(parkingLotRepository.findAll()).thenReturn(parkingLotEntities);
        List<ParkingLotEntity> parkingLotEntityList = this.parkingLotService.getAllParkingLots();
        assertEquals(parkingLotEntity,parkingLotEntityList.get(0));
        assertEquals(parkingLotEntity1,parkingLotEntityList.get(1));
    }

    @Test
    void createParkingLotTest(){
        when(parkingLotRepository.save(any(ParkingLotEntity.class))).thenReturn(parkingLotEntity1);
        ParkingLotEntity newParkingLotEntity = this.parkingLotService.addParkingLot(parkingLotEntity1);
        assertNotNull(newParkingLotEntity);
        assertEquals(2, parkingLotEntity1.getParkingLotId());
        assertEquals(2, parkingLotEntity1.getZoneId());
        assertEquals(true, parkingLotEntity1.getIsGuarded());
        assertEquals(true, parkingLotEntity1.getIsForHandicapped());
        assertEquals(true, parkingLotEntity1.getIsPaid());
        assertEquals(50, parkingLotEntity1.getFreeSpaces());
    }

    @Test
    void deleteParkingLotTest() {
        Optional<ParkingLotEntity> optionalParkingLotEntity = Optional.of(parkingLotEntity);

        when(parkingLotRepository.findById(parkingLotId)).thenReturn(optionalParkingLotEntity);

        parkingLotService.deleteParkingLot(parkingLotId);

        Mockito.verify(parkingLotRepository, times(1)).delete(optionalParkingLotEntity.get());
    }

    @Test
    void deleteParkingLotNoSuchParkingLotExceptionTest(){
        when(parkingLotRepository.findById(parkingLotId)).thenReturn(Optional.empty());
        assertThrows(NoSuchParkingLotException.class, () -> parkingLotService.deleteParkingLot(parkingLotId));
    }

    @Test
    void changeOccupancyTest(){
        Optional<ParkingLotEntity> optionalParkingLotEntity = Optional.of(parkingLotEntity3);
        when(parkingLotRepository.findById(3L)).thenReturn(optionalParkingLotEntity);
        when(parkingLotRepository.save(any(ParkingLotEntity.class))).thenReturn(parkingLotEntity2);
        this.parkingLotService.changeParkingLotOccupancy(3L,60);
        assertEquals(3, parkingLotEntity3.getParkingLotId());
        assertEquals(4, parkingLotEntity3.getZoneId());
        assertEquals(true, parkingLotEntity3.getIsGuarded());
        assertEquals(true, parkingLotEntity3.getIsForHandicapped());
        assertEquals(true, parkingLotEntity3.getIsPaid());
        assertEquals(60, parkingLotEntity3.getFreeSpaces());
    }

    @Test
    void changeOccupancyNoSuchParkingLotExceptionTest(){
        when(parkingLotRepository.findById(parkingLotId)).thenReturn(Optional.empty());
        assertThrows(NoSuchParkingLotException.class, () -> parkingLotService.changeParkingLotOccupancy(3L,60));
    }

}
