package hhplus.concert.support.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConcertTopic {

    public static String payment;

    public static String token;

    @Value("${kafka.topics.concert.payment}")
    public void setPayment(String payment) {
        ConcertTopic.payment = payment;
    }

    @Value("${kafka.topics.concert.token}")
    public void setToken(String token) {
        ConcertTopic.token = token;
    }
}
