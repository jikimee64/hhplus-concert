package hhplus.concert.support.holder;

import java.time.LocalDateTime;

public class TestTimeHolder implements TimeHolder {

    private LocalDateTime localDateTime;

    public TestTimeHolder() {
    }

    public TestTimeHolder(LocalDateTime localDateTime){
        this.localDateTime = localDateTime;
    }

    @Override
    public LocalDateTime currentDateTime() {
        return localDateTime;
    }
}