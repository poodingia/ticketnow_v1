package com.ticketnow.order.rest;


import com.ticketnow.order.dto.StatisticDTO;
import com.ticketnow.order.rest.response.APIResponse;
import com.ticketnow.order.rest.response.APIResponseBuilder;
import com.ticketnow.order.service.StatisticService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order/statistic")
public class StatisticResource {
    private final StatisticService statisticService;

    public StatisticResource(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @GetMapping("")
    public APIResponse<StatisticDTO> getCustomerStatistic() {
        APIResponseBuilder<StatisticDTO> builder = new APIResponseBuilder<>();
        return builder.data(statisticService.getCustomerStatistic()).ok().build();
    }

    @GetMapping("/{id}/organizer")
    public APIResponse<StatisticDTO> getCustomerStatisticByOrganizer(@PathVariable(name = "id") final Integer eventId) {
        APIResponseBuilder<StatisticDTO> builder = new APIResponseBuilder<>();
        return builder.data(statisticService.getOrganizerStatistic(eventId)).ok().build();
    }
}
