package persistence.mitarbeiter;

import domain.Mitarbeiter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public record JdbcMitarbeiterRepository(Connection connection) implements MitarbeiterRepository {
    @Override
    public Optional<Mitarbeiter> getMitarbeiterWithID(String id) throws SQLException {
        try (var statement = connection.prepareStatement(JdbcMitarbeiterRepositoryParser.getSqlForSelectWithID())) {
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next())
                return JdbcMitarbeiterRepositoryParser.getOptionalMitarbeiter(resultSet);
            return Optional.empty();
        }
    }

    @Override
    public List<Mitarbeiter> getAllMitarbeiter() throws SQLException {
        try (var statement = connection.prepareStatement(JdbcMitarbeiterRepositoryParser.getSqlForSelect())) {
            ResultSet resultSet = statement.executeQuery();
            return getMitarbeiterList(resultSet);
        }
    }

    private List<Mitarbeiter> getMitarbeiterList(ResultSet resultSet) throws SQLException {
        var builder = Stream.<Optional<Mitarbeiter>>builder();
        while (resultSet.next()){
            builder.add(JdbcMitarbeiterRepositoryParser.getOptionalMitarbeiter(resultSet));
        }
        return builder.build()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }
}
