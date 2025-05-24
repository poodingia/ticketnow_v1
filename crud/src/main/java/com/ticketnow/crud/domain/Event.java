package com.ticketnow.crud.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "events")
public class Event {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String title;
    @Column
    private String location;

    @Column(columnDefinition = "text")
    private String description;

    @Column
    private String bgImagePath;

    @Column
    private LocalDateTime date;

    @Column
    private Integer accountId;

    @Column
    private Integer max;

    @Column
    private Float revenue;

    @Column
    private LocalDateTime startSaleDate;

    @Column
    private LocalDateTime endSaleDate;

    @Column
    private Boolean isCanceled;

    @Column
    private Boolean isApproved;

    @Column
    private String userId;

    @Column
    private Integer bookingCapacity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    private City city;

    @OneToMany(mappedBy = "event")
    private Set<TicketType> eventTicketTypes;

    @OneToMany(mappedBy = "event")
    private Set<Coupon> coupons;

    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY)
    private Set<Ticket> eventTickets;

    @Enumerated(EnumType.STRING)
    private EventCategory category;

    public Event() {
    }

    @PrePersist
    public void prePersist() {
        if (bookingCapacity == null) {
            bookingCapacity = 0; // Set default before saving
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(final String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getBgImagePath() {
        return bgImagePath;
    }

    public void setBgImagePath(final String bgImagePath) {
        this.bgImagePath = bgImagePath;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(final LocalDateTime date) {
        this.date = date;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(final Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(final Integer max) {
        this.max = max;
    }

    public LocalDateTime getStartSaleDate() {
        return startSaleDate;
    }

    public void setStartSaleDate(final LocalDateTime startSaleDate) {
        this.startSaleDate = startSaleDate;
    }

    public LocalDateTime getEndSaleDate() {
        return endSaleDate;
    }

    public void setEndSaleDate(final LocalDateTime endSaleDate) {
        this.endSaleDate = endSaleDate;
    }

    public Boolean getIsCanceled() {
        return isCanceled;
    }

    public void setIsCanceled(final Boolean isCanceled) {
        this.isCanceled = isCanceled;
    }

    public City getCity() {
        return city;
    }

    public void setCity(final City city) {
        this.city = city;
    }

    public Set<TicketType> getEventTicketTypes() {
        return eventTicketTypes;
    }

    public void setEventTicketTypes(final Set<TicketType> eventTicketTypes) {
        this.eventTicketTypes = eventTicketTypes;
    }

    public Set<Ticket> getEventTickets() {
        return eventTickets;
    }

    public void setEventTickets(final Set<Ticket> eventTickets) {
        this.eventTickets = eventTickets;
    }

    public Boolean getApproved() {
        return isApproved;
    }

    public void setApproved(Boolean approved) {
        isApproved = approved;
    }

    public EventCategory getCategory() {
        return category;
    }

    public void setCategory(EventCategory category) {
        this.category = category;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getBookingCapacity() {
        return bookingCapacity;
    }

    public void setBookingCapacity(Integer bookingCapacity) {
        this.bookingCapacity = bookingCapacity;
    }

    public Float getRevenue() {
        return revenue;
    }

    public void setRevenue(Float revenue) {
        this.revenue = revenue;
    }

    public Set<Coupon> getCoupons() {
        return coupons;
    }

    public void setCoupons(Set<Coupon> coupons) {
        this.coupons = coupons;
    }
}
