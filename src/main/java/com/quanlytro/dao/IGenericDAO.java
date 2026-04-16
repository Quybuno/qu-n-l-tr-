package com.quanlytro.dao;

import java.util.List;


// các lớp dao phải tuân theo các hàm trong interface
public interface IGenericDAO<T> {

    void add(T t);

    void update(T t);

    void delete(Object id);

    T findById(Object id);

    List<T> getAll();
}
