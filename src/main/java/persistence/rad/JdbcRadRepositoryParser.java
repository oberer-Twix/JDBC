package persistence.rad;

import domain.Lager;
import domain.Rad;
import persistence.lager.JdbcLagerRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class JdbcRadRepositoryParser {

    private static final String COLUMNS_WITHOUT_ID = "Rad_name, Rad_groese, Rad_marke, Rad_Kaufpreis, Rad_Lager";
    private static final String COLUMNS = "Rad_ID, " + COLUMNS_WITHOUT_ID;

    public static String getSqlForSelect(){
        return "select " + COLUMNS + """
                 from Rad
                """;
    }

    public static String getSqlForSelectWithId(){
        return getSqlForSelect() + """
                where Rad_ID = ?;
                """;
    }

    public static Optional<Rad> getRadFromResultSet(ResultSet resultSet, Optional<Lager> optionalLager) throws SQLException {
        Lager lager;
        if(optionalLager.isPresent()){//todo mögl fehler vorher mit null verglichen
            lager = optionalLager.orElseThrow();
        }else{
            lager = null;
        }
        return Optional.of(new Rad(
                resultSet.getInt("Rad_ID"),
                resultSet.getString("Rad_name"),
                resultSet.getString("Rad_groese"),
                resultSet.getString("Rad_marke"),
                resultSet.getInt("Rad_Kaufpreis"),
                lager));
    }

    public static String getSqlForSelectWithLagerID(){
        return getSqlForSelect() + """
                where Rad_Lager = ?;
                """;
    }

    public static String getSqlForSelectAllInLager(){
        return getSqlForSelect() + """
                where Rad_Lager is not null;
                """;
    }

    public static String getSqlForSelectWithMarke(){
        return getSqlForSelect() + """
                where Rad_marke = ?;
                """;
    }

    public static String getSqlForSelectWithGroese(){
        return getSqlForSelect() + """
                where Rad_groese = ?;
                """;
    }

    public static String getSqlForInsert(){
        return """
                insert 
                into Rad
                (""" + COLUMNS_WITHOUT_ID + """
                ) values (?, ?, ?, ?, ?);
                """;
    }

    public static String getSqlForDelete(){
        return """
                delete
                from Rad
                where Rad_id = ?;
                """;
    }

    public static String getSqlForTakingOutOfLager(){
        return """
                update Rad
                set Rad_Lager = null
                where Rad_ID = ?;
                """;
    }

    public static String getSqlForStore(){
        return """
                update Rad
                set Rad_Lager = ?
                where Rad_ID = ?;
                """;
    }

    public static String getSqlForRaederAnzahlInLager(){
        return """
            select count(*)
            from Rad
            where Rad_Lager = ?
            """;
    }

    public static List<Rad> getReaderFromResultSet(PreparedStatement statement, Connection connection) throws SQLException {
        ResultSet resultSet = statement.executeQuery();
        var bilder = Stream.<Optional<Rad>>builder();
        while (resultSet.next()) {
            bilder.add(getRad(resultSet, connection));
        }
        return bilder.build()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    public static Optional<Lager> getLagerWithLagerID(ResultSet resultSet, Connection connection) throws SQLException {
        var lagerRepository = new JdbcLagerRepository(connection);
        var lagerID = resultSet.getInt("Rad_Lager");
        /*if(lagerID == 0){
            return Optional.empty(); /-/to do null mit OPtional empty ersetzt
        }*/
        return lagerRepository.findByID(lagerID);
    }

    public static Optional<Rad> getRad(ResultSet resultSet, Connection connection) throws SQLException {
        Optional<Lager> lager = getLagerWithLagerID(resultSet, connection);
        return JdbcRadRepositoryParser.getRadFromResultSet(resultSet, lager);
    }


}
