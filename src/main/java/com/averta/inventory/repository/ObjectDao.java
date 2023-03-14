package com.averta.inventory.repository;

import java.io.Serializable;
import java.util.List;

public interface ObjectDao {
    
    public void saveObject(final Object entity);

    public void deleteObject(final Object entity);

    public void updateObject(final Object entity);

    public <T> T getObjectByParam(Class<T> entity, String param, Object obj);

    public <T> T getObjectById(Class<T> entity, Serializable id);

    public <T> void deleteById(final Class<T> entity, final long entityId);
    
    public <T> T listObject(Class<T> entity);
    
    public <T> T listObjectAsc(Class<T> entity, String id);

    public <T> T listObjectByParam(Class<T> entity, String param, String orderBy, Object obj);

    public <T> T listObjectByMultiParam(Class<T> entity, String param1, Object obj1, String param2, Object obj2);

    public <T> T getObjectByMultipleParam(Class<T> entity, String param1, Object obj1, String param2, Object obj2);

    public <T> T deleteByParam(Class<T> entity, String param, Object value);

    public <T> T listObjectByParamAsc(Class<T> entity, String param, String orderBy, Object obj);

    public <T> Long getCount(Class<T> entity);

    public <T> String getColumnByParam(Class<T> entity, String param);

    public List<String> columnNames(String table) throws Exception;

    public <T> T getObjectByTripleParam(Class<T> entity, String param1, Object obj1, String param2, Object obj2,
            String param3, Object obj3);

}
