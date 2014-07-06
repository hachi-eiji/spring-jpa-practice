package com.hachiyae.jpa.interceptor;

import java.sql.Connection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.internal.SessionImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SlaveConnectionInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(SlaveConnectionInterceptor.class);

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Around("execution(public * com.hachiyae.jpa..*.find*(..)) || execution(public * com.hachiyae.jpa..*.count*(..))")
    public Object proceed(ProceedingJoinPoint pjp) throws Throwable {
        LOGGER.info("call SlaveConnectionInterceptor from {}", pjp.getSignature().toShortString());
        SessionImpl session = entityManager.unwrap(SessionImpl.class);
        Connection connection = session.connection();
        boolean autoCommit = connection.getAutoCommit();
        boolean readOnly = connection.isReadOnly();
        connection.setAutoCommit(false);
        connection.setReadOnly(true);
        try {
            return pjp.proceed();
        } finally {
            connection.setAutoCommit(autoCommit);
            connection.setReadOnly(readOnly);
        }
    }
}
