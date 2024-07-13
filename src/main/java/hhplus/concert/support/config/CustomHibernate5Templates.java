package hhplus.concert.support.config;

import com.querydsl.jpa.DefaultQueryHandler;
import com.querydsl.jpa.Hibernate5Templates;
import com.querydsl.jpa.QueryHandler;

/**
 * querydsl의 transform을 사용하기 위한 커스텀 클래스
 *
 * spring boo 3.x의 경우 hibernate6.x 버전으로 넘어오면서 ScrollableResults에서 get(int) 메서드가 사라짐
 * Hibernate5Templates를 상속받아서 getQueryHandler를 오버라이딩 해주어야 한다.
 */
public class CustomHibernate5Templates extends Hibernate5Templates {
    @Override
    public QueryHandler getQueryHandler() {
        return DefaultQueryHandler.DEFAULT;
    }
}