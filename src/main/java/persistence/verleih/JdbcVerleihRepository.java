package persistence.verleih;

import domain.Kunde;
import domain.Mitarbeiter;
import domain.Rad;
import domain.Verleih;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public record JdbcVerleihRepository(Connection connection) implements VerleihRepository{

    @Override
    public Optional<Verleih> findByID(int id) throws SQLException {
        try (var statement = connection.prepareStatement(JdbcVerleihRepositoryParser.getSqlForSelectWithID())){
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                return getOptionalVerleih(resultSet);
            }
        }
        return Optional.empty();
    }

    private Optional<Verleih> getOptionalVerleih(ResultSet resultSet) throws SQLException {
        Mitarbeiter mitarbeiter = JdbcVerleihRepositoryParser.getMitarbeiter(resultSet, connection);
        Kunde kunde = JdbcVerleihRepositoryParser.getKunde(resultSet, connection);
        return JdbcVerleihRepositoryParser.getRawOptionalVerleih(resultSet, mitarbeiter, kunde);
    }



    @Override
    public List<Verleih> findAll() throws SQLException {
        try (var statement = connection.prepareStatement(JdbcVerleihRepositoryParser.getSqlForSelect())){
            ResultSet resultSet = statement.executeQuery();
            var builder = Stream.<Optional<Verleih>>builder();
            while (resultSet.next()){
                builder.add(getOptionalVerleih(resultSet));
            }
            return builder.build()
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();
        }
    }

    @Override
    public List<Rad> findAllRaederWithVerleihId(int id) throws SQLException {
        try (var statement = connection.prepareStatement(JdbcVerleihRepositoryParser.getSqlForAllRaederInVerleih())){
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            var raeder = JdbcVerleihRepositoryParser.getRadList(resultSet, connection);
            return raeder;
        }
    }

    @Override
    public int save(Verleih verleih) throws SQLException {
        try (var statement = connection.prepareStatement(JdbcVerleihRepositoryParser.getSqlForSaving(),
                Statement.RETURN_GENERATED_KEYS)){
            JdbcVerleihRepositoryParser.setStatementsForSaving(verleih, statement);
            if(statement.executeUpdate() != 1){
                throw new IllegalArgumentException("Etwas hat beim Speichern nicht funktioniert");
            }
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if(generatedKeys.next()){
                return generatedKeys.getInt(1);
            }
        }
        throw new IllegalArgumentException("Etwas hat beim Speichern nicht funktioniert");
    }

    @Override
    public void verwendeRadInVerleihWithIDs(int verleihID, int radID) throws SQLException {
        try (var statement = connection.prepareStatement(JdbcVerleihRepositoryParser.getSqlForSavingVerwendung())){
            JdbcVerleihRepositoryParser.setStatementsForVerwendungSave(verleihID, radID, statement);
            if(statement.executeUpdate() != 1){
                throw new IllegalArgumentException("Etwas hat nicht funktioniert");
            }
        }
    }
}
