package hhplus.concert.support.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConcertTopic {

    public static String payment;

    @Value("${kafka.topics.concert.payment}")
    public void setChannel(String payment) {
        ConcertTopic.payment = payment;
    }
}
