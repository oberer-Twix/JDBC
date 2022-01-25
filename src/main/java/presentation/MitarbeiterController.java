package presentation;

import domain.Mitarbeiter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import persistence.ConnectionManager;
import persistence.mitarbeiter.JdbcMitarbeiterRepository;
import persistence.mitarbeiter.MitarbeiterRepository;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MitarbeiterController implements Initializable, Updatable {

    private MitarbeiterRepository mitarbeiterRepository;
    @FXML
    private TableView<Mitarbeiter> tableViewMitarbeiter;
    @FXML
    private TableColumn<Mitarbeiter, String> tvMitarbeiterName;
    @FXML
    private TableColumn<Mitarbeiter, String> tvMitarbeiterHandy;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Connection connection = null;
        try {
            connection = ConnectionManager.getInstance();
        } catch (SQLException e) {
            Controller.alert("Verbindung zu Datenbank konnte nicht hergestellt werden \n" + e.getMessage());
        }
        mitarbeiterRepository = new JdbcMitarbeiterRepository(connection);

        tvMitarbeiterName.setCellValueFactory(new PropertyValueFactory<>("mitarbeiterName"));
        tvMitarbeiterHandy.setCellValueFactory(new PropertyValueFactory<>("handynummer"));

        Controller.addToUpdateList(this);
        update();
    }


    public void fillTable() throws SQLException {
        tableViewMitarbeiter.getItems().clear();
        tableViewMitarbeiter.getItems().addAll(mitarbeiterRepository.getAllMitarbeiter());
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
