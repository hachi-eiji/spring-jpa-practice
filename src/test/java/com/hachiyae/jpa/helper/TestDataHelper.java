package com.hachiyae.jpa.helper;

import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.ParameterizedType;
import java.util.Map;

/**
 * Create test data helper
 *
 * @param <T> database entity class
 */
public abstract class TestDataHelper<T> {
    /**
     * Create a test data object.
     * <p/>
     * merge <code>recode</code> and return value of defaultData method.
     *
     * @param record test data. key:column name, value:test data
     * @return Entity object
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public T createData(Map<String, Object> record) throws Exception {
        Class<? extends TestDataHelper> aClass = this.getClass();
        ParameterizedType type = (ParameterizedType) aClass.getGenericSuperclass();
        Class<?> entityClass = (Class<?>) type.getActualTypeArguments()[0];
        T instance = (T) entityClass.newInstance();
        BeanUtils.copyProperties(instance, defaultData());
        BeanUtils.copyProperties(instance, record);
        return instance;
    }

    /**
     * create a default test data.
     * <p/>
     * implements subclass
     *
     * @return default data
     */
    public abstract T defaultData();
}
