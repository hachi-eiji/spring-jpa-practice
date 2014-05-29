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
     * Create test data.
     * <p/>
     * merge <code>data</code> and return value of defaultData method.
     *
     * @param data test data
     * @return merged data
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public T create(Map<String, Object> data) throws Exception {
        Class<? extends TestDataHelper> aClass = this.getClass();
        ParameterizedType type = (ParameterizedType) aClass.getGenericSuperclass();
        Class<?> entityClass = (Class<?>) type.getActualTypeArguments()[0];
        T instance = (T) entityClass.newInstance();
        BeanUtils.copyProperties(instance, defaultData());
        BeanUtils.copyProperties(instance, data);
        return instance;
    }

    /**
     * create default data.
     * <p/>
     * implements subclass
     *
     * @return default data
     */
    public abstract Map<String, Object> defaultData();
}
