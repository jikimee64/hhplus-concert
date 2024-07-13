package hhplus.concert.domain.user;

public interface UserRepository {
    User findById(Long userId);
}
