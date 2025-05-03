package com.gentlemonster.GentleMonsterBE.Repositories;

import com.gentlemonster.GentleMonsterBE.Entities.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface ICityRepository extends JpaRepository<City, UUID>, JpaSpecificationExecutor<City> {
    boolean existsByCityName(String cityName);
}
