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
@Table(name = "Users")
public class UserEntity {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long userId;

    @NotNull
    private Integer carSize; // -> parking strzezony

    @NotNull
    private Long preferableZone;

    @NotNull
    private String name;

    @NotNull
    private String surname;

    @NotNull
    private Integer age;

    @NotNull
    private Boolean handicaped;
}
