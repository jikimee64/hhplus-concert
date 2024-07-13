package hhplus.concert.domain.user;

import hhplus.concert.interfaces.api.support.ApiException;
import hhplus.concert.interfaces.api.support.error.ErrorCode;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class UserTest {

    @Test
    void 유저의_잔액_추가_및_차감_로직을_검증한다(){
        // given
        User user = new User("userId", 1000);

        // when
        user.addAmount(3000);
        user.subtractAmount(1000);

        // then
        assertThat(user.getAmount()).isEqualTo(3000);
    }

    @Test
    void 유저의_잔액차감시_잔액이_부족할_경우_예외가_발생한다(){
        // given
        User user = new User("userId", 1000);

        // when & then
        assertThatThrownBy(() ->  user.subtractAmount(2000))
                .isInstanceOf(ApiException.class)
                .hasMessage(ErrorCode.E005.getMessage());
    }
}
