package com.example.sp.maxsat.Controller;

import com.example.sp.maxsat.Entities.ParkingLotEntity;
import com.example.sp.maxsat.Entities.ZoneEntity;
import com.example.sp.maxsat.Services.ParkingLotService;
import com.example.sp.maxsat.Services.ZoneService;
import com.example.sp.maxsat.Solver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/maxsat")
public class CarSharingController {

    private final ZoneService zoneService;
    private final ParkingLotService parkingService;

    @Autowired
    public CarSharingController(ZoneService zoneService,ParkingLotService parkingLotService){
        this.zoneService = zoneService;
        this.parkingService = parkingLotService;
    }




    @GetMapping("/sfps")
    public String searchForParkingSpot(@RequestParam(value = "Lat", defaultValue = "0") int x,@RequestParam(value = "Lon", defaultValue = "0") int y) {

        List<ZoneEntity> zones = new ArrayList<>();
        //wybierz strefy z bazy danych które przylegają do lokacji
        zoneService.getAllZones().forEach(zone -> {
            if ((zone.getCordX()<= x+1 & zone.getCordX()>= x-1 & zone.getCordY()<= y+1 & zone.getCordY()>= y) ||
                    (zone.getCordX()==x & zone.getCordY()==y-1)){ zones.add(zone);}
        });

        class zonetouple{
            public final long ZoneId;
            public final int Score;
            public zonetouple(long parkingId,int Score){this.ZoneId = parkingId;this.Score=Score;}

            @Override
            public String toString(){
                return String.format("ZoneId: %d     Score: %d \n", ZoneId,Score);
            }
        }
        List<zonetouple> results = new ArrayList<>();
        Solver solver = new Solver(zones);
        parkingService.getAllParkingLots().forEach(parking ->
                results.add(new zonetouple(parking.getParkingLotId(),solver.test(parking))));

        results.sort(Comparator.comparingInt(obj -> obj.Score));
        return zoneService.getAllZones().size() + "\n" + results;
    }

    @GetMapping("/sfc")
    public String searchForCar(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("Hello %s!", name);
    }

    @GetMapping("/parkCar")
    public String park(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("Hello %s!", name);
    }

    @GetMapping("/takeCar")
    public String takeCar(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("Hello %s!", name);
    }

    @GetMapping("/GenerateData")
    public String GenerateZones(@RequestParam(value = "amount", defaultValue = "5") int amount) {


        //zones
        for (int x = (-1)*amount; x < amount; x++)
            for (int y = (-1)*amount; y < amount; y++) {
            ZoneEntity zone = new ZoneEntity();
            zone.setZoneType("test");
            zone.setOccupiedRatio(Math.random());
            zone.setAttractivenessRatio(Math.random());
            zone.setRequestRatio(Math.random());
            zone.setCordX(x);
            zone.setCordY(y);
            zoneService.addZone(zone);
        }
        List<ZoneEntity> zones = zoneService.getAllZones();
        //ParkingLots
        for (int i = 0; i < amount*amount*10; i++) {
            ParkingLotEntity parking = new ParkingLotEntity();
            parking.setZoneId(zones.get((int) Math.round(Math.random()*(zones.size()-1))).getZoneId());
            parking.setFreeSpaces((int) Math.round(Math.random()*100));
            parking.setIsGuarded(Math.random()>0.5);
            parking.setIsPaid(Math.random()>0.5);
            parking.setIsForHandicapped(Math.random()>0.5);

            parkingService.addParkingLot(parking);
        }

        return "ok ";
    }
}

