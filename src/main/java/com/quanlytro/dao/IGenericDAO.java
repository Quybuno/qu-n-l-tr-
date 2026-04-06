package com.quanlytro.dao;

import java.util.List;

public interface IGenericDAO<T> {

    void add(T t);

    void update(T t);

    void delete(Object id);

    T findById(Object id);

    List<T> getAll();
}
