package persistence.mitarbeiter;

import domain.Mitarbeiter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class JdbcMitarbeiterRepositoryParser {
    private static final String COLLUMS = "Mitarbeiter_name, Mitarbeiter_handynummer";

    public static String getSqlForSelect(){
        return "select " + COLLUMS + """
                 from Mitarbeiter
                """;
    }

    public static String getSqlForSelectWithID(){
        return getSqlForSelect() + """
                 where Mitarbeiter_name = ?
                """;
    }

    public static Optional<Mitarbeiter> getOptionalMitarbeiter(ResultSet resultSet) throws SQLException {
        return Optional.of(new Mitarbeiter(
                        resultSet.getString("Mitarbeiter_name"),
                        resultSet.getString("Mitarbeiter_handynummer")
                )
        );
    }
}
