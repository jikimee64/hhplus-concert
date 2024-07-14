package hhplus.concert.interfaces.api.filter;

import jakarta.servlet.*;
import org.jboss.logging.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TransactionIdFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String transactionId = UUID.randomUUID().toString();

        MDC.put("transactionId", transactionId);

        try {
            chain.doFilter(request, response);
        } finally {
            MDC.remove("transactionId");
        }
    }

}
