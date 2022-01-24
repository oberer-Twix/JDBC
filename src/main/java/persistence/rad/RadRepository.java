package persistence.rad;

import domain.Lager;
import domain.Rad;
import domain.RadGroesen;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface RadRepository {
    Optional<Rad> findByID(int id) throws SQLException;
    List<Rad> findAll() throws SQLException;
    List<Rad> findAllInLagerWithID(int id) throws SQLException;
    List<Rad> findAllInLager() throws SQLException;
    List<Rad> findAllFromBrand(String marke) throws SQLException;
    List<Rad> findAllWithGroesen(RadGroesen groese) throws SQLException;
    Rad save(Rad rad) throws SQLException;
    void delete(int id) throws SQLException;
    void store(Lager lager, int id) throws SQLException;
    void withdraw(int id) throws SQLException;
    int getAnzahlRaederInLager(int id) throws SQLException;
}
