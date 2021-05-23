package com.example.sp.maxsat.Repositories;

import com.example.sp.maxsat.Entities.ZoneEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ZoneRepository extends JpaRepository<ZoneEntity, Long> {

}
