package presentation;

import domain.Lager;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import persistence.ConnectionManager;
import persistence.lager.JdbcLagerRepository;
import persistence.lager.LagerRepository;
import persistence.rad.JdbcRadRepository;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LagerController implements Initializable, Updatable {

    private LagerRepository lagerRepository;
    private JdbcRadRepository radRepository;
    @FXML
    private TableView<Lager> tableViewLager;
    @FXML
    private TableColumn<Lager, Integer> tvLagerID;
    @FXML
    private TableColumn<Lager, String> tvLagerName;
    @FXML
    private TableColumn<Lager, Integer> tvLagerAnzahlRaeder;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Connection connection = null;
        try {
            connection = ConnectionManager.getInstance();
        } catch (SQLException e) {
            Controller.alert("Verbindung zu Datenbank konnte nicht hergestellt werden \n" + e.getMessage());
        }
        lagerRepository = new JdbcLagerRepository(connection);
        radRepository = new JdbcRadRepository(connection);
        tvLagerID.setCellValueFactory(new PropertyValueFactory<>("lagerID"));
        tvLagerName.setCellValueFactory(new PropertyValueFactory<>("lagerName"));
        tvLagerAnzahlRaeder.setCellValueFactory(item -> new SimpleObjectProperty<>(getAnzahl(item.getValue())));

        Controller.addToUpdateList(this);
        update();
    }

    private int getAnzahl(Lager lager){
        try {
            return radRepository.getAnzahlRaederInLager(lager.getLagerID());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void fillTable() throws SQLException {
        tableViewLager.getItems().clear();
        tableViewLager.getItems().addAll(lagerRepository.findAll());
    }

    @Override
    public void update() {
        try {
            fillTable();
        } catch (SQLException e) {
            Controller.alert("Ups. Etwas ist schief gelaufen");
        }
    }
}
