package hhplus.concert.domain.concert;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReservationTest {

    @Test
    void 유저잔액이_예약한_좌석의_금액보다_같거나_클경우_true를_반환한다() {
        // given
        int userAmount = 1001;
        int seatAmount = 1000;
        Reservation reservation = Reservation.builder()
                .seatAmount(seatAmount)
                .build();
        // when
        boolean result = reservation.isUserAmountSufficient(userAmount);
        // then
        assertTrue(result);
    }

    @Test
    void 유저잔액이_예약한_좌석의_금액보다_작을경우_false를_반환한다() {
        // given
        int userAmount = 999;
        int seatAmount = 1000;
        Reservation reservation = Reservation.builder()
                .seatAmount(seatAmount)
                .build();
        // when
        boolean result = reservation.isUserAmountSufficient(userAmount);
        // then
        assertFalse(result);
    }

    @Test
    void 예약상태가_예약중일_경우_true를_반환한다() {
        // given
        Reservation reservation = Reservation.builder()
                .status(ReservationStatus.RESERVED)
                .build();
        // when
        boolean result = reservation.isReserved();
        // then
        assertTrue(result);
    }

    @Test
    void 예약상태가_임시예약일경우_true를_반환한다() {
        // given
        Reservation reservation = Reservation.builder()
                .status(ReservationStatus.TEMP_RESERVED)
                .build();
        // when
        boolean result = reservation.isTempReserved();
        // then
        assertTrue(result);
    }
}