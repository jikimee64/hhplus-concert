package hhplus.concert.support;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("mysql")
@SpringBootTest
public class DummyTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    //    @Test
    @DisplayName("콘서트 데이터 생성")
    void batchInsertConcert() {
        String sql = "INSERT INTO concert (title) VALUES (?)";
        int totalEntries = 11; // 총 생성할 행의 수
        int batchSize = 10;        // 한 번의 배치 작업에서 생성할 행의 수

        for (int i = 0; i < totalEntries / batchSize; i++) {
            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int j) throws SQLException {
                    int stringLength = 1 + (int) (Math.random() * 100); // 1부터 100 사이의 길이 선택
                    ps.setString(1, generateRandomString(stringLength));
                }

                @Override
                public int getBatchSize() {
                    return batchSize;
                }
            });
        }
    }

    //    @Test
    @DisplayName("콘서트 스케줄 데이터 생성")
    void batchInsertConcertSchedule() {
        String sql = "INSERT INTO concert_schedule (concert_id, open_date, start_at, end_at, total_seat, total_seat_status) VALUES (?, ?, ?, ?, ?, ?)";
        int totalEntries = 200_000; // 총 생성할 행의 수
        int batchSize = 1000;       // 한 번의 배치 작업에서 생성할 행의 수
        int currentId = 1;          // 시작 ID

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        while (currentId <= totalEntries) {
            final int startId = currentId;
            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int j) throws SQLException {
                    ps.setLong(1, startId + j); // concert_id
                    ps.setDate(2, java.sql.Date.valueOf(LocalDate.parse("2024-01-01"))); // open_date
                    ps.setTimestamp(3, java.sql.Timestamp.valueOf(LocalDateTime.parse("2024-01-01 00:00:00", dateTimeFormatter))); // start_at
                    ps.setTimestamp(4, java.sql.Timestamp.valueOf(LocalDateTime.parse("2024-01-30 00:00:00", dateTimeFormatter))); // end_at
                    ps.setInt(5, 50); // total_seat
                    ps.setString(6, "AVAILABLE"); // total_seat_status
                }

                @Override
                public int getBatchSize() {
                    return Math.min(batchSize, totalEntries - startId + 1); // 남은 항목이 batchSize보다 적을 경우 처리
                }
            });
            currentId += batchSize;
        }
    }

//    @Test
    @DisplayName("콘서트 좌석 데이터 생성(1000만개)")
    void batchInsertConcertSeat() {
        String sql = "INSERT INTO concert_seat_index (concert_schedule_id, amount, position) VALUES (?, ?, ?)";
        int totalSchedules = 200_000; // 총 생성할 concert_schedule_id의 수
        int positionsPerSchedule = 50; // 각 concert_schedule_id 당 생성할 position의 수
        int totalEntries = totalSchedules * positionsPerSchedule; // 총 생성할 행의 수
        int batchSize = 1000;       // 한 번의 배치 작업에서 생성할 행의 수
        int currentId = 1;          // 시작 ID

        while (currentId <= totalSchedules) {
            final int startId = currentId;
            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int j) throws SQLException {
                    int concertScheduleId = startId + j / positionsPerSchedule;
                    int position = j % positionsPerSchedule + 1;

                    ps.setLong(1, concertScheduleId); // concert_schedule_id
                    ps.setInt(2, generateRandomAmount()); // amount
                    ps.setInt(3, position); // position
                }

                @Override
                public int getBatchSize() {
                    return Math.min(batchSize, totalEntries - (startId - 1) * positionsPerSchedule); // 남은 항목이 batchSize보다 적을 경우 처리
                }
            });
            currentId += batchSize / positionsPerSchedule;
        }
    }

//    @Test
    @DisplayName("콘서트 예약 데이터 생성(600만개)")
    void batchInsertReservation() {
        String sql = """
        INSERT INTO reservation_index (user_id, concert_schedule_id, seat_id, seat_amount, seat_position, status, reserved_at) 
        VALUES (?, ?, ?, ?, ?, ?, ?) 
        """;
        int batchSize = 50;              // 한 번의 배치 작업에서 생성할 행의 수
        int totalEntries = 2_000_000;    // 총 생성할 데이터 수
        long seatIdCounter = 1;          // seat_id를 순차적으로 증가시키기 위한 변수
        int concertScheduleId = 1;       // 시작 concert_schedule_id

        while (seatIdCounter <= totalEntries) {
            final long startSeatId = seatIdCounter; // 시작 seat_id
            final int currentConcertScheduleId = concertScheduleId; // 현재 concert_schedule_id

            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int j) throws SQLException {
                    int position = j + 1;
//                    String status = j < 25 ? "TEMP_RESERVED" : "RESERVED";
                    long seatId = startSeatId + j;

                    ps.setLong(1, 1L); // user_id
                    ps.setLong(2, currentConcertScheduleId); // concert_schedule_id
                    ps.setLong(3, seatId); // seat_id
                    ps.setInt(4, generateRandomAmount()); // seat_amount
                    ps.setInt(5, position); // seat_position
                    ps.setString(6, "RESERVED"); // status
                    ps.setTimestamp(7, java.sql.Timestamp.valueOf(LocalDateTime.now())); // reserved_at
                }

                @Override
                public int getBatchSize() {
                    return batchSize;
                }
            });

            seatIdCounter += batchSize; // 다음 배치에서 시작할 seat_id
            concertScheduleId += 1;     // 50개의 예약마다 concert_schedule_id를 증가
        }
    }

//    @Test
    @DisplayName("콘서트 결제 데이터 생성")
    void batchInsertPayment() {
        String sql = """
        INSERT INTO payment_index (user_id, reservation_id, price, status, created_at) 
        VALUES (?, ?, ?, ?, ?) 
        """;
        int batchSize = 1000;             // 한 번의 배치 작업에서 생성할 행의 수
        long reservationIdCounter = 1;    // reservation_id를 순차적으로 증가시키기 위한 변수

        while (reservationIdCounter <= 2_000_000) {
            final long startReservationId = reservationIdCounter;

            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int j) throws SQLException {
                    long reservationId = startReservationId + j;

                    ps.setLong(1, 1L); // user_id
                    ps.setLong(2, reservationId); // reservation_id
                    ps.setInt(3, generateRandomAmount()); // price
                    ps.setString(4, "PROGRESS"); // status
                    ps.setTimestamp(5, java.sql.Timestamp.valueOf(LocalDateTime.now())); // created_at
                }

                @Override
                public int getBatchSize() {
                    // 한 번의 배치 작업에서 최대 batchSize만큼의 행 생성
                    return Math.min(batchSize, (int) (2_000_000 - startReservationId + 1));
                }
            });
            reservationIdCounter += batchSize; // 다음 batchSize만큼 이동
        }
    }

    private int generateRandomAmount() {
        return new SecureRandom().nextInt(10000) + 1; // 1부터 10000 사이의 금액
    }

    private String generateRandomString(int length) {
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom RANDOM = new SecureRandom();
        StringBuilder result = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            result.append(CHARACTERS.charAt(index));
        }
        return result.toString();
    }

}
