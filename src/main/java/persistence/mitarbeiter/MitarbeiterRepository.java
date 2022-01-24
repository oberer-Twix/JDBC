package persistence.mitarbeiter;

import domain.Mitarbeiter;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface MitarbeiterRepository {
    Optional<Mitarbeiter> getMitarbeiterWithID(String id) throws SQLException;
    List<Mitarbeiter> getAllMitarbeiter() throws SQLException;
}
