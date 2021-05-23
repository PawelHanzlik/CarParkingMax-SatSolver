package com.example.sp.maxsat.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ParkingLotEntity {

    @Id
    private Long parkingLotId;
    private Long zoneId;
    private Boolean isGuarded;
    private Boolean isPaid;
    private Boolean isForHandicapped;
}
