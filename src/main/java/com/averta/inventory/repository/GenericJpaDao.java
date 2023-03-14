package com.averta.inventory.repository;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class GenericJpaDao extends AbstractJpaDao implements ObjectDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void saveObject(Object entity) {
        persist(entity);
    }

    @Override
    public void deleteObject(Object entity) {
        delete(entity);
    }

    @Override
    public void updateObject(Object entity) {
        update(entity);
    }

    @Override
    public <T> T getObjectById(Class<T> entity, Serializable id) {
        return entityManager.find(entity, id);
    }

    public <T> void deleteById(final Class<T> entity, final long entityId) {
        final T deletingEntity = getObjectById(entity, entityId);
        if (deletingEntity != null)
            delete(deletingEntity);
    }

    @SuppressWarnings("unchecked")
	public <T> T listObject(Class<T> entity) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entity);
        Root<T> root = criteriaQuery.from(entity);
        CriteriaQuery<T> select = criteriaQuery.select(root);
        TypedQuery<T> typedQuery = entityManager.createQuery(select);
        return (T) typedQuery.getResultList();
    }

    @Override
    public <T> T getObjectByParam(Class<T> entity, String param, Object obj) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entity);

            Root<T> root = criteriaQuery.from(entity);

            CriteriaQuery<T> select = criteriaQuery.select(root).where(criteriaBuilder.equal(root.get(param), obj));

            TypedQuery<T> typedQuery = entityManager.createQuery(select);

            return typedQuery.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
	@Override
    public <T> T listObjectAsc(Class<T> entity, String id) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entity);

        Root<T> root = criteriaQuery.from(entity);
        CriteriaQuery<T> select = criteriaQuery.select(root).orderBy(criteriaBuilder.asc(root.get(id)));

        TypedQuery<T> typedQuery = entityManager.createQuery(select);
        return (T) typedQuery.getResultList();
    }

    @SuppressWarnings("unchecked")
	@Override
    public <T> T listObjectByParam(Class<T> entity, String param, String orderBy, Object obj) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entity);

            Root<T> root = criteriaQuery.from(entity);

            CriteriaQuery<T> select = criteriaQuery.select(root).where(criteriaBuilder.equal(root.get(param), obj))
                    .orderBy(criteriaBuilder.asc(root.get(orderBy)));

            TypedQuery<T> typedQuery = entityManager.createQuery(select);
            return (T) typedQuery.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
	@Override
    public <T> T listObjectByMultiParam(Class<T> entity, String param1, Object obj1, String param2, Object obj2) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entity);

            Root<T> root = criteriaQuery.from(entity);

            CriteriaQuery<T> select = criteriaQuery.select(root).where(criteriaBuilder
                    .and(criteriaBuilder.equal(root.get(param1), obj1), criteriaBuilder.equal(root.get(param2), obj2)));

            TypedQuery<T> typedQuery = entityManager.createQuery(select);
            return (T) typedQuery.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public <T> T getObjectByMultipleParam(Class<T> entity, String param1, Object obj1, String param2, Object obj2) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entity);

            Root<T> root = criteriaQuery.from(entity);

            CriteriaQuery<T> select = criteriaQuery.select(root).where(criteriaBuilder
                    .and(criteriaBuilder.equal(root.get(param1), obj1), criteriaBuilder.equal(root.get(param2), obj2)));

            TypedQuery<T> typedQuery = entityManager.createQuery(select);
            return typedQuery.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public <T> T deleteByParam(Class<T> entity, String param, Object value) {
        Query query = entityManager.createQuery("delete from " + entity.getName() + "  where " + param + "=:obj");
        query.setParameter("obj", value);
        query.executeUpdate();
        return null;
    }

    @SuppressWarnings("unchecked")
	@Override
    public <T> T listObjectByParamAsc(Class<T> entity, String param, String orderBy, Object obj) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entity);

        Root<T> root = criteriaQuery.from(entity);
        criteriaQuery.orderBy(criteriaBuilder.asc(root.get(orderBy)));

        Expression<T> expression = root.get(param);
        Predicate predicate = expression.in(obj);
        criteriaQuery.where(predicate);

        CriteriaQuery<T> select = criteriaQuery.select(root);

        TypedQuery<T> typedQuery = entityManager.createQuery(select);
        return (T) typedQuery.getResultList();
    }

    @Override
    public <T> Long getCount(Class<T> entity) {
        Query query = entityManager.createQuery("select count(*) from " + entity.getName());
        return (Long) query.getSingleResult();
    }

    @Override
    public <T> String getColumnByParam(Class<T> entity, String param) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<String> columnNames(String table) throws Exception {
        List<String> columns = new ArrayList<String>();
        Connection conn = null;
        try {
            conn = getConnection();
            DatabaseMetaData dbmd = conn.getMetaData();
            ResultSet rs = dbmd.getColumns(null, null, table, "%");
            while (rs.next()) {
                columns.add(rs.getString("COLUMN_NAME"));
            }
            rs.close();
        } catch (Exception e) {
            throw e;
        } finally {
            if (null != conn)
                conn.close();
        }
        return columns;
    }

    @Override
    public <T> T getObjectByTripleParam(Class<T> entity, String param1, Object obj1, String param2, Object obj2,
            String param3, Object obj3) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entity);

            Root<T> root = criteriaQuery.from(entity);

            CriteriaQuery<T> select = criteriaQuery.select(root)
                    .where(criteriaBuilder.and(criteriaBuilder.equal(root.get(param1), obj1),
                            criteriaBuilder.equal(root.get(param2), obj2),
                            criteriaBuilder.equal(root.get(param3), obj3)));

            TypedQuery<T> typedQuery = entityManager.createQuery(select);
            return (T) typedQuery.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

}