package com.ticketnow.crud.repos;

import com.ticketnow.crud.domain.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City, Integer> {
}