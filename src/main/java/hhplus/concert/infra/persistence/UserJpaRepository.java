package hhplus.concert.infra.persistence;

import hhplus.concert.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<User, Long> {
}
