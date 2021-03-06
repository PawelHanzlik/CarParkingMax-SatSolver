package com.example.sp.maxsat.Entities;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@ToString
@Table(name = "Zones")
public class ZoneEntity {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long zoneId;

    @NotNull
    private String zoneType;

    @NotNull
    private Double occupiedRatio;

    @NotNull
    private Double attractivenessRatio;

    @NotNull
    private Double requestRatio;

    @NotNull
    private Integer cordX;

    @NotNull
    private Integer cordY;

    public long getPriority(){return (Math.round((occupiedRatio-requestRatio)*5))+11;}
}
