package hhplus.concert.infra.persistence;

import hhplus.concert.domain.pay.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PaymentJpaRepository extends JpaRepository<Payment, Long> {
    @Modifying
    @Query("delete from Payment p where p.reservationId in :reservationIds")
    void deleteAllBy(@Param("reservationIds") List<Long> reservationIds);
}
