package com.example.sp.maxsat.Repositories;

import com.example.sp.maxsat.Entities.ParkingLotEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingLotRepository extends JpaRepository<ParkingLotEntity,Long> {

}
