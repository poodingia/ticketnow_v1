package com.ticketnow.crud.service;

import com.ticketnow.crud.domain.Coupon;
import com.ticketnow.crud.domain.CouponCategory;
import com.ticketnow.crud.dto.CouponDTO;
import com.ticketnow.crud.dto.CouponOrganizerDTO;
import com.ticketnow.crud.exception.CustomCrudException;
import com.ticketnow.crud.exception.ErrorMessage;
import com.ticketnow.crud.repos.CouponRepository;
import com.ticketnow.crud.repos.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CouponService {
    private final CouponRepository couponRepository;
    private final EventRepository eventRepository;

    public CouponService(CouponRepository couponRepository, EventRepository eventRepository) {
        this.couponRepository = couponRepository;
        this.eventRepository = eventRepository;
    }

    public Integer create(CouponDTO couponDTO) {
        Coupon coupon = new Coupon();
        return mapDTOtoCoupon(couponDTO, coupon);
    }

    public Integer update(CouponDTO couponDTO) {
        Coupon coupon = couponRepository.findById(couponDTO.id()).orElseThrow();
        return mapDTOtoCoupon(couponDTO, coupon);
    }

    private Integer mapDTOtoCoupon(CouponDTO couponDTO, Coupon coupon) {
        coupon.setCode(couponDTO.code());
        coupon.setEvent(eventRepository.getReferenceById(couponDTO.eventId()));
        coupon.setCategory(CouponCategory.valueOf(couponDTO.category()));
        coupon.setValue(couponDTO.value());
        coupon.setQuantity(couponDTO.quantity());
        coupon.setUsed(0);
        return couponRepository.save(coupon).getId();
    }

    public void updateUsed(Integer couponId) {
        Coupon coupon = couponRepository.findById(couponId).orElseThrow();
        if (coupon.getUsed() >= coupon.getQuantity()) {
            throw new CustomCrudException(ErrorMessage.INVALID_COUPON);
        }
        coupon.setUsed(coupon.getUsed() + 1);
        couponRepository.save(coupon);
    }

    public CouponDTO getOne(String code, Integer eventId) {
        Coupon coupon = couponRepository.findByCodeAndEventId(code, eventId).orElseThrow();
        if (coupon.getUsed() >= coupon.getQuantity()) {
            throw new CustomCrudException(ErrorMessage.INVALID_COUPON);
        }
        return new CouponDTO(coupon.getId(), coupon.getCode(), coupon.getEvent().getId(),
                coupon.getCategory().name(), coupon.getValue(), coupon.getQuantity());
    }

    public CouponDTO findOneById(int id) {
        Coupon coupon = couponRepository.findById(id).orElseThrow();
        return new CouponDTO(coupon.getId(), coupon.getCode(), coupon.getEvent().getId(),
                coupon.getCategory().name(), coupon.getValue(), coupon.getQuantity());
    }

    public List<CouponOrganizerDTO> getALl(Integer eventId) {
        return couponRepository.findAllByEventId(eventId).stream()
                .map(coupon -> new CouponOrganizerDTO(coupon.getId(), coupon.getCode(), coupon.getEvent().getId(),
                        coupon.getCategory().name(), coupon.getValue(), coupon.getQuantity(), coupon.getUsed()))
                .toList();
    }
}
