package persistence.verleih;

import domain.Rad;
import domain.Verleih;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface VerleihRepository {
    Optional<Verleih> findByID(int id) throws SQLException;
    List<Verleih> findAll() throws SQLException;
    List<Rad> findAllRaederWithVerleihId(int id) throws SQLException;
    int save(Verleih verleih) throws SQLException;
    void verwendeRadInVerleihWithIDs(int verleihID, int radID) throws SQLException;


}
