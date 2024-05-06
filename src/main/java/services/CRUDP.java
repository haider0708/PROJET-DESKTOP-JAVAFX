package services;

import models.Pharmacie;

import java.sql.SQLException;
import java.util.List;

public interface CRUDP<T> {

    void insertOne(T t) throws SQLException;
    void updateOne(T t) throws SQLException;
    void deleteOne(T t) throws SQLException;
    List<T> selectAll() throws SQLException;


}
