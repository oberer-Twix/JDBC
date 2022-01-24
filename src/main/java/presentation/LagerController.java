package presentation;

import domain.Lager;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import persistence.lager.LagerRepository;
import persistence.rad.JdbcRadRepository;

import java.sql.SQLException;

public class LagerController {

    private LagerRepository lagerRepository;
    private JdbcRadRepository radConnection;
    private AnchorPane tabLager;
    private TableView<Lager> tableViewLager;
    private TableColumn<Lager, Integer> tvLagerID;
    private TableColumn<Lager, String> tvLagerName;
    private TableColumn<Lager, Integer> tvLagerAnzahlRaeder;

    public LagerController(
            LagerRepository lagerRepository,
            JdbcRadRepository radConnection,
            AnchorPane tabLager,
            TableView<Lager> tableViewLager,
            TableColumn<Lager, Integer> tvLagerID,
            TableColumn<Lager, String> tvLagerName,
            TableColumn<Lager, Integer> tvLagerAnzahlRaeder) {
        this.lagerRepository = lagerRepository;
        this.radConnection = radConnection;
        this.tabLager = tabLager;
        this.tableViewLager = tableViewLager;
        this.tvLagerID = tvLagerID;
        this.tvLagerName = tvLagerName;
        this.tvLagerAnzahlRaeder = tvLagerAnzahlRaeder;

        tvLagerID.setCellValueFactory(new PropertyValueFactory<>("lagerID"));
        tvLagerName.setCellValueFactory(new PropertyValueFactory<>("lagerName"));
        tvLagerAnzahlRaeder.setCellValueFactory(item -> new SimpleObjectProperty<>(getAnzahl(item.getValue())));
    }

    private int getAnzahl(Lager lager){
        try {
            return radConnection.getAnzahlRaederInLager(lager.getLagerID());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void fillTable() throws SQLException {
        tableViewLager.getItems().clear();
        tableViewLager.getItems().addAll(lagerRepository.findAll());
    }
}
