package com.ticketnow.crud.service;

import com.ticketnow.crud.domain.City;
import com.ticketnow.crud.dto.CityDTO;
import com.ticketnow.crud.repos.CityRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CityService {
    private final CityRepository cityRepository;

    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public List<CityDTO> getAllCities() {
        List<City> cities = cityRepository.findAll();
        List<CityDTO> cityDTOs = new ArrayList<>();
        for (City city : cities) {
            cityDTOs.add(new CityDTO(city.getId(), city.getName()));
        }
        return cityDTOs;
    }
}
