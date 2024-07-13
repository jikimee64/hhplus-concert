package hhplus.concert.infra.persistence;

import hhplus.concert.api.support.ApiException;
import hhplus.concert.api.support.error.ErrorCode;
import hhplus.concert.domain.User;
import hhplus.concert.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public User findById(Long userId) {
        return userJpaRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.E404, "User not found userId = " + userId));
    }
}
