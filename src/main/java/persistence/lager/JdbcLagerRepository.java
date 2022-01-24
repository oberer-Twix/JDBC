package persistence.lager;

import domain.Lager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

public record JdbcLagerRepository(Connection connection) implements LagerRepository {
    @Override
    public Optional<Lager> findByID(int id) throws SQLException {
        try (var statement = connection.prepareStatement(JdbcLagerRepositoryParser.getSqlForSelectWithID())){
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                return JdbcLagerRepositoryParser.getOptionalLager(resultSet);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Lager> findAll() throws SQLException {
        try (var statement = connection.prepareStatement(JdbcLagerRepositoryParser.getSqlForSelect())){
            ResultSet resultSet = statement.executeQuery();
            return JdbcLagerRepositoryParser.getLagerList(resultSet);
        }
    }

    @Override
    public Lager saveLager(Lager lager) throws SQLException {
        try (var statement = connection.prepareStatement(
                JdbcLagerRepositoryParser.getSqlForSavingLager(),
                Statement.RETURN_GENERATED_KEYS)){
            statement.setString(1, lager.getLagerName());
            if (statement.executeUpdate() != 1) {
                throw new IllegalArgumentException("Konnte nicht gespeichert werden");
            }
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()){
                return new Lager(generatedKeys.getInt(1), lager.getLagerName());
            }
        }
        throw new IllegalArgumentException("HAt nicht FunkTIOniERt");
    }


}
