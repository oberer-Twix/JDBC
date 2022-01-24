package persistence.kunde;

import domain.Kunde;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public record JdbcKundeRepository(Connection connection) implements KundeRepository{
    @Override
    public Optional<Kunde> findById(int id) throws SQLException {
        try (var statement = connection.prepareStatement(JdbcKundeRepositoryParser.getSqlForSelectWithId())){
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                return JdbcKundeRepositoryParser.getOptionalKunde(resultSet);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Kunde> findAll() throws SQLException {
        try (var statement = connection.prepareStatement(JdbcKundeRepositoryParser.getSqlForSelect())){
            ResultSet resultSet = statement.executeQuery();
            var builder = Stream.<Optional<Kunde>>builder();
            while (resultSet.next()){
                builder.add(JdbcKundeRepositoryParser.getOptionalKunde(resultSet));
            }
            return builder.build()
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();
        }
    }
}
