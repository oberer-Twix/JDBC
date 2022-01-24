package persistence.lager;

import domain.Lager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class JdbcLagerRepositoryParser {
    private static final String COLLUMS = "Lager_ID, Lager_name";

    public static String getSqlForSelect() {
        return "select " + COLLUMS + """
                 from Lager
                """;
    }

    public static String getSqlForSelectWithID() {
        return getSqlForSelect() + """
                where Lager_ID = ?;
                """;
    }

    public static Optional<Lager> getOptionalLager(ResultSet resultSet) throws SQLException {
        return Optional.of(
                new Lager(
                        resultSet.getInt("Lager_ID"),
                        resultSet.getString("Lager_name")
                )
        );
    }

    public static List<Lager> getLagerList(ResultSet resultSet) throws SQLException {
        var bilder = Stream.<Optional<Lager>>builder();
        while (resultSet.next()){
            bilder.add(getOptionalLager(resultSet));
        }
        return bilder.build()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    public static String getSqlForSavingLager(){
        return """
                insert 
                into Lager
                (Lager_name) values (?)
                """;
    }
}
