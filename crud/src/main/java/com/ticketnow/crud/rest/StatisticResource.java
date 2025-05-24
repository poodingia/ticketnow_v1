package com.ticketnow.crud.rest;

import com.ticketnow.crud.dto.RevenueDashboardDTO;
import com.ticketnow.crud.rest.response.APIResponse;
import com.ticketnow.crud.rest.response.APIResponseBuilder;
import com.ticketnow.crud.service.StatisticService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/crud/statistic")
@RestController
public class StatisticResource {
    private final StatisticService statisticService;

    public StatisticResource(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @GetMapping("/organizer-dashboard")
    public APIResponse<RevenueDashboardDTO> getOrganizerDashboard() {
        APIResponseBuilder<RevenueDashboardDTO> builder = new APIResponseBuilder<>();
        RevenueDashboardDTO dto = statisticService.generateOrganizerDashboard();
        return builder.data(dto).ok().build();
    }

    @GetMapping("/admin-dashboard")
    public APIResponse<RevenueDashboardDTO> getAdminDashboard() {
        APIResponseBuilder<RevenueDashboardDTO> builder = new APIResponseBuilder<>();
        RevenueDashboardDTO dto = statisticService.generateAdminDashboard();
        return builder.data(dto).ok().build();
    }
}
