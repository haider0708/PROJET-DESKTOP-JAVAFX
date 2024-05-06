/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this temlivreure file, choose Tools | Temlivreures
 * and open the temlivreure in the editor.
 */
package services;

import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author asus
 */
public interface IlivreurService<T> {
    
       public void ajouterlivreur(T t) throws SQLException;
    public void modifierlivreur(T t) throws SQLException;
    public void supprimerlivreur(T t) throws SQLException;
    public List<T> recupererlivreur() throws SQLException;
    
}
