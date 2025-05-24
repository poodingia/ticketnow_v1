package com.ticketnow.crud.repos;

import com.ticketnow.crud.domain.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {
}