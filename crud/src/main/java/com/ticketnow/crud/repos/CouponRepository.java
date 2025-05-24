package com.ticketnow.crud.repos;

import com.ticketnow.crud.domain.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Integer> {
    @Query(
            value = "SELECT coupon from Coupon coupon where coupon.code = :code and coupon.event.id = :eventId"
    )
    Optional<Coupon> findByCodeAndEventId(String code, Integer eventId);

    @Query(
            value = "SELECT coupon from Coupon coupon where coupon.event.id = :eventId"
    )
    List<Coupon> findAllByEventId(Integer eventId);
}