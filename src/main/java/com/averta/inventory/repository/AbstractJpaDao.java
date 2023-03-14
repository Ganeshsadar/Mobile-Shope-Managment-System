package com.averta.inventory.repository;

import java.sql.Connection;
import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.metamodel.Attribute;
import javax.sql.DataSource;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

@Transactional
public abstract class AbstractJpaDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private DataSource dataSource;

    public void persist(final Object entity) {
        entityManager.persist(entity);
    }

    public void update(final Object entity) {
        entityManager.merge(entity);
    }

    public void delete(final Object entity) {
        entityManager.remove(entity);
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public <T> Attribute<? super T, ?> columnByParam(Class<T> entity, String param) {
        return null;
    }

}
