package hhplus.concert.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    private int amount;

    public User(Long id, String userId, int amount) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
    }

    public User(String userId, int amount) {
        this.userId = userId;
        this.amount = amount;
    }

    public User(String userId) {
        this(null, userId, 0);
    }

    public Long getId() {
        return id;
    }

    public void addAmount(int amount) {
        this.amount += amount;
    }

    public void subtractAmount(int amount) {
        this.amount -= amount;
    }

    public int getAmount() {
        return amount;
    }
}
