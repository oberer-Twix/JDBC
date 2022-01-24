package persistence.verleih;

import domain.Kunde;
import domain.Mitarbeiter;
import domain.Rad;
import domain.Verleih;
import persistence.kunde.JdbcKundeRepository;
import persistence.mitarbeiter.JdbcMitarbeiterRepository;
import persistence.rad.JdbcRadRepository;

import java.sql.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class JdbcVerleihRepositoryParser {
    private static final String COLLUMSWITHOUTID
            = "Verleih_Dauer_In_Tagen, Verleih_Preis, Verleih_Anfangsdatum, Verleih_Zusatz_Tools, Verleih_Kunde, Verleih_Mitarbeiter";
    private static final String COLLUMS = "Verleih_ID, " + COLLUMSWITHOUTID;

    public static String getSqlForSelect() {
        return "select " + COLLUMS + """
                 from Verleih
                """;
    }
    public static String getSqlForSelectWithID() {
        return getSqlForSelect() + """
                where Verleih_ID = ?;
                """;
    }
    public static Mitarbeiter getMitarbeiter(ResultSet resultSet, Connection connection) throws SQLException {
        return new JdbcMitarbeiterRepository(connection)
                .getMitarbeiterWithID(resultSet.getString("Verleih_Mitarbeiter"))
                .orElseThrow();
    }
    public static Kunde getKunde(ResultSet resultSet, Connection connection) throws SQLException {
        return new JdbcKundeRepository(connection)
                .findById(resultSet.getInt("Verleih_Kunde"))
                .orElseThrow();
    }
    public static Optional<Verleih> getRawOptionalVerleih(ResultSet resultSet, Mitarbeiter mitarbeiter, Kunde kunde) throws SQLException {
        return Optional.of(
                new Verleih(
                        resultSet.getInt("Verleih_ID"),
                        resultSet.getInt("Verleih_Dauer_In_Tagen"),
                        resultSet.getInt("Verleih_Preis"),
                        resultSet.getDate("Verleih_Anfangsdatum").toLocalDate(),
                        resultSet.getString("Verleih_Zusatz_Tools"),
                        kunde,
                        mitarbeiter
                )
        );
    }
    public static String getSqlForAllRaederInVerleih() {
        return """
                select verw_Rad
                from verwendung
                where verw_Verleih = ?
                """;
    }
    public static List<Rad> getRadList(ResultSet resultSet, Connection connection) throws SQLException {
        var builder = Stream.<Optional<Rad>>builder();
        var radRepository = new JdbcRadRepository(connection);
        while (resultSet.next()) {
            var id = resultSet.getInt("verw_Rad");
            builder.add(radRepository.findByID(id));
        }
        return builder.build()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }
    public static String getSqlForSaving() {
        return """
                insert 
                into Verleih
                (
                """ + COLLUMSWITHOUTID + """
                ) values (?, ?, ?, ?, ?, ?)
                """;
    }
    public static String getSqlForSavingVerwendung(){
        return """
                insert 
                into verwendung
                (verw_Rad, verw_Verleih) values (?, ?)
                """;
    }
    public static void setStatementsForSaving(Verleih verleih, PreparedStatement statement) throws SQLException {
        statement.setInt(1, verleih.getVerleihDauer());
        statement.setInt(2, verleih.getVerleihPreis());
        statement.setDate(3, Date.valueOf(verleih.getVerleihAnfangsdatum()));
        statement.setString(4, verleih.getVerleihZusatztools());
        statement.setInt(5, verleih.getVerleihKunde().getKundenID());
        statement.setString(6, verleih.getVerleihMitarbeiter().getMitarbeiterName());
    }

    public static void setStatementsForVerwendungSave(int verleihID, int radID, PreparedStatement statement) throws SQLException {
        statement.setInt(1, radID);
        statement.setInt(2, verleihID);
    }
}
