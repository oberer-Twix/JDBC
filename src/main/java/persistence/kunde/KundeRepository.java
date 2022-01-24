package persistence.kunde;

import domain.Kunde;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface KundeRepository {
    Optional<Kunde> findById(int id) throws SQLException;
    List<Kunde> findAll() throws SQLException;
}
