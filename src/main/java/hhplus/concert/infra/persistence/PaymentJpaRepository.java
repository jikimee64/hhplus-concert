package hhplus.concert.infra.persistence;

import hhplus.concert.domain.pay.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentJpaRepository extends JpaRepository<Payment, Long> {
}
