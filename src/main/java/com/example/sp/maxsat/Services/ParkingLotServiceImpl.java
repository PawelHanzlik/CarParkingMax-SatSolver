package com.example.sp.maxsat.Services;

import com.example.sp.maxsat.Entities.ParkingLotEntity;
import com.example.sp.maxsat.Exceptions.Classes.NoSuchParkingLotException;
import com.example.sp.maxsat.Repositories.ParkingLotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ParkingLotServiceImpl implements ParkingLotService{

    private final ParkingLotRepository parkingLotRepository;
    @Autowired
    public ParkingLotServiceImpl(ParkingLotRepository parkingLotRepository) {
        this.parkingLotRepository = parkingLotRepository;
    }

    @Override
    public ParkingLotEntity getParkingLot(Long parkingLotId) throws NoSuchParkingLotException {
        Optional<ParkingLotEntity> parkingLotOptional = this.parkingLotRepository.findById(parkingLotId);
        if (parkingLotOptional.isEmpty()){
            throw new NoSuchParkingLotException();
        }
        return parkingLotOptional.get();
    }

    @Override
    public List<ParkingLotEntity> getAllParkingLots() {
        return this.parkingLotRepository.findAll();
    }

    @Override
    public ParkingLotEntity addParkingLot(ParkingLotEntity parkingLot) {
        return this.parkingLotRepository.save(parkingLot);
    }

    @Override
    public void deleteParkingLot(Long parkingLotId) throws NoSuchParkingLotException{
        Optional<ParkingLotEntity> parkingLotOptional = this.parkingLotRepository.findById(parkingLotId);
        if (parkingLotOptional.isEmpty()){
            throw new NoSuchParkingLotException();
        }
        else{
            ParkingLotEntity parkingLot = parkingLotOptional.get();
            this.parkingLotRepository.delete(parkingLot);
        }
    }

    @Override
    public void changeParkingLotOccupancy(Long parkingLotId, Integer newOccupancy) throws NoSuchParkingLotException{
        Optional<ParkingLotEntity> parkingLotOptional = this.parkingLotRepository.findById(parkingLotId);
        if (parkingLotOptional.isEmpty()){
            throw new NoSuchParkingLotException();
        }
        else{
            ParkingLotEntity parkingLot = parkingLotOptional.get();
            parkingLot.setFreeSpaces(newOccupancy);
            this.parkingLotRepository.save(parkingLot);
        }
    }
}
