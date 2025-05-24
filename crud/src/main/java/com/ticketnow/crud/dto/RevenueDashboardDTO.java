package com.ticketnow.crud.dto;

import java.util.List;

public record RevenueDashboardDTO(long totalRevenue, long totalTicketsSold, List<RevenueDTO> topFiveRevenueEvents,
                                    List<QuantityDTO> topFiveTicketTypes) {
}
