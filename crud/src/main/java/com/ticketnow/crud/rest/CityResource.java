package com.ticketnow.crud.rest;

import com.ticketnow.crud.dto.CityDTO;
import com.ticketnow.crud.rest.response.APIResponse;
import com.ticketnow.crud.rest.response.APIResponseBuilder;
import com.ticketnow.crud.service.CityService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/crud/city")
public class CityResource {
    private final CityService cityService;

    public CityResource(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping
    public APIResponse<List<CityDTO>> getAllCities() {
        APIResponseBuilder<List<CityDTO>> responseBuilder = new APIResponseBuilder<>();
        return responseBuilder.data(cityService.getAllCities()).ok().build();
    }
}
