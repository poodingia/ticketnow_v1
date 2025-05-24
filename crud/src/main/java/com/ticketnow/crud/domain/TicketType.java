package com.ticketnow.crud.domain;


import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "ticket_types")
public class TicketType {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String description;

    @Column
    private Float price;

    @Column
    private Integer quantity;

    @Column
    private Integer reservedQuantity;

    @Column
    private Boolean boughtDirectly;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    @OneToMany(mappedBy = "type")
    private Set<Ticket> typeTickets;

    public TicketType() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(final Float price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(final Integer quantity) {
        this.quantity = quantity;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(final Event event) {
        this.event = event;
    }

    public Set<Ticket> getTypeTickets() {
        return typeTickets;
    }

    public void setTypeTickets(final Set<Ticket> typeTickets) {
        this.typeTickets = typeTickets;
    }

    public Integer getReservedQuantity() {
        return reservedQuantity;
    }

    public void setReservedQuantity(Integer reservedQuantity) {
        this.reservedQuantity = reservedQuantity;
    }

    public Boolean isBoughtDirectly() {
        return boughtDirectly;
    }

    public void setBoughtDirectly(Boolean boughtDirectly) {
        this.boughtDirectly = boughtDirectly;
    }
}
