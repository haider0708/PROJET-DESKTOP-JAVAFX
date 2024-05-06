package services;

import java.sql.SQLException;
import java.util.List;

public interface CRUDc<T> {
    void insertOne(T entity) throws SQLException;
    void updateOne(T entity) throws SQLException;
    void deleteOne(T entity) throws SQLException;
    List<T> selectAll() throws SQLException;
}