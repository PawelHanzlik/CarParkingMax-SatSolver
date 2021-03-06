package com.example.sp.maxsat.Controller;

import com.example.sp.maxsat.Entities.ParkingLotEntity;
import com.example.sp.maxsat.Entities.UserEntity;
import com.example.sp.maxsat.Entities.ZoneEntity;
import com.example.sp.maxsat.Services.ParkingLotService;
import com.example.sp.maxsat.Services.UserService;
import com.example.sp.maxsat.Services.ZoneService;
import com.example.sp.maxsat.Solver;
import lombok.Getter;
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
    private final UserService userService;

    private int sfpcTestCounter = 0;

    @Autowired
    public CarSharingController(ZoneService zoneService,ParkingLotService parkingLotService,UserService userService){
        this.zoneService = zoneService;
        this.parkingService = parkingLotService;
        this.userService = userService;
    }



    @GetMapping("/sfps")
    public String searchForParkingSpot(@RequestParam(value = "Lat", defaultValue = "0") int x,
                                       @RequestParam(value = "Lon", defaultValue = "0") int y,
                                       @RequestParam(value = "userId", defaultValue = "-1")int userId,
                                       @RequestParam(value = "czySkapy", defaultValue = "1")int czySkapy,
                                       @RequestParam(value = "czyDba_o_Wygode", defaultValue = "0")int czyDba){
        List<Integer> preferences = new ArrayList<>();
        preferences.add(czySkapy);
        preferences.add(czyDba);
        List<ZoneEntity> zones = new ArrayList<>();
        //wybierz strefy z bazy danych kt??re przylegaj?? do lokacji
        zoneService.getAllZones().forEach(zone -> {
            if (zoneService.isAdjacent(zone, x, y)) {
                zones.add(zone);
            }
        });

        @Getter
        class zoneTuple {
            public final long ParkingId;
            public final int Score;

            public zoneTuple(long parkingId, int Score) {
                this.ParkingId = parkingId;
                this.Score = Score;
            }

            @Override
            public String toString() {
                return String.format("ParkingId: %d     Score: %d \n", ParkingId, Score);
            }
        }

        int NoUser;
        if (userId == (-1)) NoUser = this.sfpcTestCounter++;
        else NoUser = userId;

        List<zoneTuple> results = new ArrayList<>();
        //try {
        Solver solver = new Solver(zones, userService.getUser((long) NoUser),preferences);
        parkingService.getAllParkingLots().forEach(parking ->
                results.add(new zoneTuple(parking.getParkingLotId(), solver.test(parking))));

        results.sort(Comparator.comparingInt(zoneTuple::getScore).reversed());
        return zoneService.getAllZones().size() + "\n" + results;
        /*}catch (NoSuchUserException ex){
            System.out.println("User with such id doesn't exist");
        }
        catch(Exception ex){
        System.out.println("Invalid Request");}
        return null;*/
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
            zone.setOccupiedRatio(Math.round(Math.random()*100.0)/100.0);
            zone.setAttractivenessRatio(Math.round(Math.random()*100.0)/100.0);
            zone.setRequestRatio(Math.round(Math.random()*100.0)/100.0);
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
            parking.setSpotSize((int) Math.round(Math.random()*5));

            parkingService.addParkingLot(parking);
        }

        for (int i = 0; i < amount*amount*10; i++) {
            UserEntity user = new UserEntity();
            user.setCarSize((int) Math.round(Math.random()*10)+1);
            user.setAge((int) (Math.round(Math.random()*50)+20));
            user.setHandicaped(Math.random()>0.75);
            user.setName("Jack");
            user.setSurname("aaa");
            user.setPreferableZone(zones.get((int) Math.round(Math.random()*(zones.size()-1))).getZoneId());

            userService.addUser(user);
        }



        return "ok ";
    }
}

