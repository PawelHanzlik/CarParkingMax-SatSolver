package com.example.sp.maxsat.UnitTest;

import com.example.sp.maxsat.Entities.ZoneEntity;
import com.example.sp.maxsat.Exceptions.Classes.NoSuchZoneException;
import com.example.sp.maxsat.Repositories.ZoneRepository;
import com.example.sp.maxsat.Services.ZoneServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static com.example.sp.maxsat.DataProviders.ZoneServiceUnitTestDataProvider.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

public class ZoneServiceUnitTests {

    @InjectMocks
    ZoneServiceImpl zoneService;

    @Mock
    ZoneRepository zoneRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getZoneTest(){
        when(zoneRepository.findById(zoneId)).thenReturn(java.util.Optional.ofNullable(zoneEntity));
        ZoneEntity zoneEntity1 = this.zoneService.getZone(zoneId);
        assertNotNull(zoneEntity1);
        assertEquals(1, zoneEntity1.getZoneId());
        assertEquals("testType", zoneEntity1.getZoneType());
        assertEquals(0.6, zoneEntity1.getOccupiedRatio());
        assertEquals(0.6, zoneEntity1.getAttractivenessRatio());
        assertEquals(0.6, zoneEntity1.getRequestRatio());
    }

    @Test
    void getZoneNoSuchZoneExceptionTest(){
        when(zoneRepository.findById(zoneId)).thenReturn(Optional.empty());
        assertThrows(NoSuchZoneException.class, () -> zoneService.getZone(zoneId));
    }

    @Test
    void getAllZones(){
        when(zoneRepository.findAll()).thenReturn(zoneEntities);
        List<ZoneEntity> zoneEntityList = this.zoneService.getAllZones();
        assertEquals(zoneEntity,zoneEntityList.get(0));
        assertEquals(zoneEntity1,zoneEntityList.get(1));
    }
    
    @Test
    void createZoneTest(){
        when(zoneRepository.save(any(ZoneEntity.class))).thenReturn(zoneEntity1);
        ZoneEntity newZoneEntity = this.zoneService.addZone(zoneEntity1);
        assertNotNull(newZoneEntity);
        assertEquals(2, newZoneEntity.getZoneId());
        assertEquals("testType1", newZoneEntity.getZoneType());
        assertEquals(0.7, newZoneEntity.getOccupiedRatio());
        assertEquals(0.7, newZoneEntity.getAttractivenessRatio());
        assertEquals(0.7, newZoneEntity.getRequestRatio());
    }

    @Test
    void deleteZoneTest() {
        Optional<ZoneEntity> optionalZoneEntity = Optional.of(zoneEntity);

        when(zoneRepository.findById(zoneId)).thenReturn(optionalZoneEntity);

        zoneService.deleteZone(zoneId);

        Mockito.verify(zoneRepository, times(1)).delete(optionalZoneEntity.get());
    }

    @Test
    void deleteZoneNoSuchZoneExceptionTest(){
        when(zoneRepository.findById(zoneId)).thenReturn(Optional.empty());
        assertThrows(NoSuchZoneException.class, () -> zoneService.deleteZone(zoneId));
    }

    @Test
    void changeOccupiedRatioTest(){
        Optional<ZoneEntity> optionalZoneEntity = Optional.of(zoneEntity3);
        when(zoneRepository.findById(3L)).thenReturn(optionalZoneEntity);
        when(zoneRepository.save(any(ZoneEntity.class))).thenReturn(zoneEntity2);
        this.zoneService.changeZoneOccupiedRatio(3L,0.7);
        assertEquals(3, zoneEntity3.getZoneId());
        assertEquals("testType2", zoneEntity3.getZoneType());
        assertEquals(0.7, zoneEntity3.getOccupiedRatio());
        assertEquals(0.6, zoneEntity3.getAttractivenessRatio());
        assertEquals(0.6, zoneEntity3.getRequestRatio());
    }

    @Test
    void changeOccupiedRatioNoSuchZoneExceptionTest(){
        when(zoneRepository.findById(zoneId)).thenReturn(Optional.empty());
        assertThrows(NoSuchZoneException.class, () -> zoneService.changeZoneOccupiedRatio(zoneId,0.7));
    }

    @Test
    void changeAttractivenessRatioTest(){
        Optional<ZoneEntity> optionalZoneEntity = Optional.of(zoneEntity5);
        when(zoneRepository.findById(4L)).thenReturn(optionalZoneEntity);
        when(zoneRepository.save(any(ZoneEntity.class))).thenReturn(zoneEntity4);
        this.zoneService.changeZoneAttractivenessRatio(4L,0.7);
        assertEquals(4, zoneEntity5.getZoneId());
        assertEquals("testType3", zoneEntity5.getZoneType());
        assertEquals(0.6, zoneEntity5.getOccupiedRatio());
        assertEquals(0.7, zoneEntity5.getAttractivenessRatio());
        assertEquals(0.6, zoneEntity5.getRequestRatio());
    }

    @Test
    void changeAttractivenessRatioNoSuchZoneExceptionTest(){
        when(zoneRepository.findById(zoneId)).thenReturn(Optional.empty());
        assertThrows(NoSuchZoneException.class, () -> zoneService.changeZoneAttractivenessRatio(zoneId,0.7));
    }

    @Test
    void changeRequestRatioTest(){
        Optional<ZoneEntity> optionalZoneEntity = Optional.of(zoneEntity7);
        when(zoneRepository.findById(5L)).thenReturn(optionalZoneEntity);
        when(zoneRepository.save(any(ZoneEntity.class))).thenReturn(zoneEntity6);
        this.zoneService.changeZoneRequestRatio(5L,0.7);
        assertEquals(5, zoneEntity7.getZoneId());
        assertEquals("testType4", zoneEntity7.getZoneType());
        assertEquals(0.6, zoneEntity7.getOccupiedRatio());
        assertEquals(0.6, zoneEntity7.getAttractivenessRatio());
        assertEquals(0.7, zoneEntity7.getRequestRatio());
    }

    @Test
    void changeRequestRatioNoSuchZoneExceptionTest(){
        when(zoneRepository.findById(zoneId)).thenReturn(Optional.empty());
        assertThrows(NoSuchZoneException.class, () -> zoneService.changeZoneRequestRatio(zoneId,0.7));
    }
}
