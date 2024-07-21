package hhplus.concert.infra.persistence;

import hhplus.concert.interfaces.api.support.ApiException;
import hhplus.concert.interfaces.api.support.error.ErrorCode;
import hhplus.concert.domain.user.User;
import hhplus.concert.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.logging.LogLevel;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public User findById(Long userId) {
        return userJpaRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.E404, LogLevel.INFO, "User not found userId = " + userId));
    }
}
