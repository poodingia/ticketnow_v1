package com.ticketnow.crud.rest;

import com.ticketnow.crud.dto.CouponDTO;
import com.ticketnow.crud.dto.CouponOrganizerDTO;
import com.ticketnow.crud.rest.response.APIResponse;
import com.ticketnow.crud.rest.response.APIResponseBuilder;
import com.ticketnow.crud.service.CouponService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/crud/coupons")
@RestController
public class CouponResource {
    private final CouponService couponService;

    public CouponResource(CouponService couponService) {
        this.couponService = couponService;
    }

    @PostMapping
    public APIResponse<Integer> create(@RequestBody CouponDTO couponDTO) {
        APIResponseBuilder<Integer> responseBuilder = new APIResponseBuilder<>();
        Integer couponId = couponService.create(couponDTO);
        return responseBuilder.data(couponId).ok().build();
    }

    @PostMapping("/apply")
    public APIResponse<Void> apply(Integer couponId) {
        APIResponseBuilder<Void> responseBuilder = new APIResponseBuilder<>();
        couponService.updateUsed(couponId);
        return responseBuilder.ok().build();
    }

    @GetMapping
    public APIResponse<CouponDTO> getOne(@RequestParam String code, @RequestParam Integer eventId) {
        APIResponseBuilder<CouponDTO> responseBuilder = new APIResponseBuilder<>();
        CouponDTO coupon = couponService.getOne(code, eventId);
        return responseBuilder.data(coupon).ok().build();
    }

    @GetMapping("/all")
    public APIResponse<List<CouponOrganizerDTO>> getALl(@RequestParam Integer eventId) {
        APIResponseBuilder<List<CouponOrganizerDTO>> responseBuilder = new APIResponseBuilder<>();
        List<CouponOrganizerDTO> coupon = couponService.getALl(eventId);
        return responseBuilder.data(coupon).ok().build();
    }

    @PutMapping
    public APIResponse<Integer> update(@RequestBody CouponDTO couponDTO) {
        APIResponseBuilder<Integer> responseBuilder = new APIResponseBuilder<>();
        Integer couponId = couponService.update(couponDTO);
        return responseBuilder.data(couponId).ok().build();
    }


    @GetMapping("/{id}")
    public APIResponse<CouponDTO> getOne(@PathVariable Integer id) {
        APIResponseBuilder<CouponDTO> responseBuilder = new APIResponseBuilder<>();
        CouponDTO coupon = couponService.findOneById(id);
        return responseBuilder.data(coupon).ok().build();
    }
}
