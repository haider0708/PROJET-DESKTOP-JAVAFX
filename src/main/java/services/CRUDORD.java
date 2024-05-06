package services;

import models.Ordonnance;

import java.sql.SQLException;
import java.util.List;

public interface CRUDORD<O> {
    void insertOne(Ordonnance ordonnance) throws SQLException;

    void updateOne(Ordonnance ordonnance) throws SQLException;

    void deleteOne(Ordonnance ordonnance) throws SQLException, SQLException;

    List<Ordonnance> selectAll() throws SQLException;
}
