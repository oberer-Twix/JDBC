package persistence.rad;

import domain.Lager;
import domain.Rad;
import domain.RadGroesen;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public record JdbcRadRepository(Connection connection) implements RadRepository {

    @Override
    public Optional<Rad> findByID(int id) throws SQLException {
        try (var statement = connection.prepareStatement(JdbcRadRepositoryParser.getSqlForSelectWithId())) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return JdbcRadRepositoryParser.getRad(resultSet, connection);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Rad> findAll() throws SQLException {
        try (var statement = connection.prepareStatement(JdbcRadRepositoryParser.getSqlForSelect())) {
            return JdbcRadRepositoryParser.getReaderFromResultSet(statement, connection);
        }
    }

    @Override
    public List<Rad> findAllInLagerWithID(int id) throws SQLException {
        try (var statement = connection.prepareStatement(JdbcRadRepositoryParser.getSqlForSelectWithLagerID())) {
            statement.setInt(1, id);
            return JdbcRadRepositoryParser.getReaderFromResultSet(statement, connection);
        }
    }

    @Override
    public List<Rad> findAllInLager() throws SQLException {
        try (var statement = connection.prepareStatement(JdbcRadRepositoryParser.getSqlForSelectAllInLager())) {
            return JdbcRadRepositoryParser.getReaderFromResultSet(statement, connection);
        }
    }

    @Override
    public List<Rad> findAllFromBrand(String marke) throws SQLException {
        try (var statement = connection.prepareStatement(JdbcRadRepositoryParser.getSqlForSelectWithMarke())) {
            statement.setString(1, marke);
            return JdbcRadRepositoryParser.getReaderFromResultSet(statement, connection);
        }
    }

    @Override
    public List<Rad> findAllWithGroesen(RadGroesen groese) throws SQLException {
        try (var statement = connection.prepareStatement(JdbcRadRepositoryParser.getSqlForSelectWithGroese())) {
            statement.setString(1, groese.getFirstLetterAsString());
            return JdbcRadRepositoryParser.getReaderFromResultSet(statement, connection);
        }
    }

    @Override
    public Rad save(Rad rad) throws SQLException {
        if (rad.getRadID() != null) {
            throw new IllegalArgumentException("Rad hat bereits eine ID");
        }
        try (var statement = connection.prepareStatement(JdbcRadRepositoryParser.getSqlForInsert()
                , Statement.RETURN_GENERATED_KEYS)) {
            return saveRad(rad, statement); //todo saveRad von parser verschoben
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        try (var statement = connection.prepareStatement(JdbcRadRepositoryParser.getSqlForDelete())) {
            statement.setInt(1, id);
            if (statement.executeUpdate() == 0) {
                throw new IllegalArgumentException("Rad konnte nicht gelöscht werden");
            }
        }
    }

    @Override
    public void withdraw(int id) throws SQLException {
        try (var statement = connection.prepareStatement(JdbcRadRepositoryParser.getSqlForTakingOutOfLager())) {
            statement.setInt(1, id);
            if (statement.executeUpdate() == 0) {
                throw new IllegalArgumentException("Kein lager gespeichert");
            }
        }
    }

    @Override
    public void store(Lager lager, int id) throws SQLException {
        try (var statement = connection.prepareStatement(JdbcRadRepositoryParser.getSqlForStore())) {
            statement.setInt(1, lager.getLagerID());
            statement.setInt(2, id);
            if (statement.executeUpdate() == 0) {
                throw new IllegalArgumentException("Konnte Rad in Lager nicht speichern");
            }
        }

    }

    @Override
    public int getAnzahlRaederInLager(int id) throws SQLException {
        try (var statement = connection.prepareStatement(JdbcRadRepositoryParser.getSqlForRaederAnzahlInLager())) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }
        throw new IllegalArgumentException("Fehler, Anzahl der Räder konnte nicht bestimmt werden");
    }

    private Rad saveRad(Rad rad, PreparedStatement statement) throws SQLException {
        statement.setString(1, rad.getRadName());
        statement.setString(2, rad.getRadGroese().getFirstLetterAsString());
        statement.setString(3, rad.getRadMarke());
        statement.setInt(4, rad.getRadKaufpreis());
        statement.setInt(5, rad.getRadLager().getLagerID());
        statement.executeUpdate();
        var key = statement.getGeneratedKeys();
        if (key.next()) {
            return new Rad(
                    key.getInt(1),
                    rad.getRadName(),
                    rad.getRadGroese().getFirstLetter(),
                    rad.getRadMarke(),
                    rad.getRadKaufpreis(),
                    rad.getRadLager()
            );
        }
        throw new IllegalArgumentException("Fehler beim Speichern");
    }
}
