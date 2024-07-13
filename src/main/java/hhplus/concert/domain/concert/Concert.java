package hhplus.concert.domain.concert;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Concert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    public Concert(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public Concert(String title) {
        this(null, title);
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
