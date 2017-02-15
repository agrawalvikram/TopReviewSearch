package com.search.datastore;

import java.io.Serializable;

/**
 * Created by jitendra.k on 14/02/17.
 */
public interface IRepository<T> {

    T findById(Class<T> clazz, Serializable id);
}
