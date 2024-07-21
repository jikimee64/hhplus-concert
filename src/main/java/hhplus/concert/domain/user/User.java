package hhplus.concert.domain.user;

import hhplus.concert.interfaces.api.support.ApiException;
import hhplus.concert.interfaces.api.support.error.ErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.boot.logging.LogLevel;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String loginId;

    private int amount;

    @Version
    private Integer version;

    public User(Long id, String loginId, int amount) {
        this.id = id;
        this.loginId = loginId;
        this.amount = amount;
    }

    public User(String loginId, int amount) {
        this.loginId = loginId;
        this.amount = amount;
    }

    public User(String loginId) {
        this(null, loginId, 0);
    }

    public Long getId() {
        return id;
    }

    public void addAmount(int amount) {
        this.amount += amount;
    }

    public void subtractAmount(int amount) {
        if (this.amount < amount) {
            throw new ApiException(ErrorCode.E005, LogLevel.INFO, "userAmount = " + amount + "this.amount = " + this.amount);
        }
        this.amount -= amount;
    }

    public int getAmount() {
        return amount;
    }
}
