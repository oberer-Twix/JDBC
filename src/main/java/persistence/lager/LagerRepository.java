package persistence.lager;

import domain.Lager;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface LagerRepository {
    Optional<Lager> findByID(int id) throws SQLException;
    List<Lager> findAll() throws SQLException;
    Lager saveLager(Lager lager) throws SQLException;
}
