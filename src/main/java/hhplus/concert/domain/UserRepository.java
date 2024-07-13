package hhplus.concert.domain;

public interface UserRepository {
    User findById(Long userId);
}
