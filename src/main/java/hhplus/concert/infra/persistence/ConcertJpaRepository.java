package hhplus.concert.infra.persistence;

import hhplus.concert.domain.concert.Concert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertJpaRepository extends JpaRepository<Concert, Long>{
}
