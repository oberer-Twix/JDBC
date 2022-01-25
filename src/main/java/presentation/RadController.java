package presentation;

import domain.Rad;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import persistence.ConnectionManager;
import persistence.rad.JdbcRadRepository;
import persistence.rad.RadRepository;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class RadController implements Initializable, Updatable {

    @FXML
    private TableView<Rad> tableViewRaeder;
    @FXML
    private TableColumn<Rad, Integer> tvRaederID;
    @FXML
    private TableColumn<Rad, String> tvRaederGroese;
    @FXML
    private TableColumn<Rad, String> tvRaederLager;
    @FXML
    private TableColumn<Rad, String> tvRaederMarke;
    @FXML
    private TableColumn<Rad, String> tvRaederName;
    @FXML
    private TableColumn<Rad, Double> tvRaederPreis;

    private RadRepository radRepository;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Connection connection = null;
        try {
            connection = ConnectionManager.getInstance();
        } catch (SQLException e) {
            Controller.alert("Verbindung zu Datenbank konnte nicht hergestellt werden \n" + e.getMessage());
        }
        radRepository = new JdbcRadRepository(connection);

        tvRaederID.setCellValueFactory(new PropertyValueFactory<>("radID"));
        tvRaederGroese.setCellValueFactory(new PropertyValueFactory<>("radGroese"));
        tvRaederLager.setCellValueFactory(new PropertyValueFactory<>("radLager"));
        tvRaederPreis.setCellValueFactory(new PropertyValueFactory<>("radKaufpreis"));
        tvRaederMarke.setCellValueFactory(new PropertyValueFactory<>("radMarke"));
        tvRaederName.setCellValueFactory(new PropertyValueFactory<>("radName"));

        Controller.addToUpdateList(this);
        update();
    }

    public void fillTable() throws SQLException {
        tableViewRaeder.getItems().clear();
        tableViewRaeder.getItems().addAll(radRepository.findAll());
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


