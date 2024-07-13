package hhplus.concert.domain.concert;

import hhplus.concert.interfaces.api.support.ApiException;
import hhplus.concert.interfaces.api.support.error.ErrorCode;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class TotalSeatStatusTest {

    @Test
    public void SOLD_OUT_enum값_확인() {
        // Given
        String status = "SOLD_OUT";

        // When
        TotalSeatStatus result = TotalSeatStatus.of(status);

        // Then
        assertEquals(TotalSeatStatus.SOLD_OUT, result);
    }

    @Test
    public void AVAILABLE_enum값_확인() {
        // Given
        String status = "AVAILABLE";

        // When
        TotalSeatStatus result = TotalSeatStatus.of(status);

        // Then
        assertEquals(TotalSeatStatus.AVAILABLE, result);
    }

    @Test
    public void TotalSeatStatus에_존재하지_않는_enum값일_경우_예외가_발생한다() {
        // Given
        String status = "NOT_EXIST";

        // when & then
        assertThatThrownBy(() -> TotalSeatStatus.of(status))
                .isInstanceOf(ApiException.class)
                .hasMessage(ErrorCode.E404.getMessage());
    }

}
