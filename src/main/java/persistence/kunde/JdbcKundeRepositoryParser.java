package persistence.kunde;

import domain.Kunde;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class JdbcKundeRepositoryParser {
    private static final String COLLUM = "Kunden_ID, Kunden_name, Kunden_adresse, Kunden_Telephonnummer, Kunden_schaeden";

    public static String getSqlForSelect(){
        return "select " + COLLUM + """
                 from Kunde
                """;
    }

    public static String getSqlForSelectWithId(){
        return getSqlForSelect() + """
                 where Kunden_ID = ?
                """;
    }

    public static Optional<Kunde> getOptionalKunde(ResultSet resultSet) throws SQLException {
        return Optional.of(
                new Kunde(
                        resultSet.getInt("Kunden_ID"),
                        resultSet.getString("Kunden_name"),
                        resultSet.getString("Kunden_adresse"),
                        resultSet.getString("Kunden_Telephonnummer"),
                        resultSet.getInt("Kunden_schaeden")
                )
        );
    }
}
