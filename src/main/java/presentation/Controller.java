package presentation;

import domain.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import persistence.ConnectionManager;
import persistence.kunde.JdbcKundeRepository;
import persistence.lager.JdbcLagerRepository;
import persistence.mitarbeiter.JdbcMitarbeiterRepository;
import persistence.rad.JdbcRadRepository;
import persistence.verleih.JdbcVerleihRepository;
import persistence.verleih.VerleihRepository;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private TabPane tapPane;

    @FXML
    private AnchorPane tabRaeder;

    @FXML
    private TableView<Rad> tableViewRaeder;

    @FXML
    private TableColumn<Rad, Integer> tvRaederID;

    @FXML
    private TableColumn<Rad, String> tvRaederMarke;

    @FXML
    private TableColumn<Rad, String> tvRaederName;

    @FXML
    private TableColumn<Rad, String> tvRaederGroese;

    @FXML
    private TableColumn<Rad, String> tvRaederLager;

    @FXML
    private TableColumn<Rad, Double> tvRaederPreis;

    @FXML
    private AnchorPane tabLager;

    @FXML
    private TableView<Lager> tableViewLager;

    @FXML
    private TableColumn<Lager, Integer> tvLagerID;

    @FXML
    private TableColumn<Lager, String> tvLagerName;

    @FXML
    private TableColumn<Lager, Integer> tvLagerAnzahlRaeder;

    @FXML
    private AnchorPane tabMitarbeiter;

    @FXML
    private TableView<Mitarbeiter> tableViewMitarbeiter;

    @FXML
    private TableColumn<Mitarbeiter, String> tvMitarbeiterName;

    @FXML
    private TableColumn<Mitarbeiter, String> tvMitarbeiterHandy;

    @FXML
    private AnchorPane tabVerleih;

    @FXML
    private TableView<Verleih> tableVerleih;

    @FXML
    private TableColumn<Verleih, Integer> tvVerleihID;

    @FXML
    private TableColumn<Verleih, String> tvVerleihKunde;

    @FXML
    private TableColumn<Verleih, LocalDate> tvVerleihDatum;

    @FXML
    private TableColumn<Verleih, Integer> tvVerleihDauer;

    @FXML
    private TableColumn<Verleih, Double> tvVerleihPreis;

    @FXML
    private TableColumn<Verleih, String> tvVerleihRad;

    @FXML
    private TableColumn<Verleih, String> tvVerleihZusatzTools;

    @FXML
    private TextField verleihZusatzToolsTF;

    @FXML
    private ChoiceBox<Kunde> verleihKundenChoiceBox;

    @FXML
    private DatePicker verleihDatum;

    @FXML
    private TextField verleihDauerTF;

    @FXML
    private TextField verleihPreisTF;

    @FXML
    private TableView<Rad> verleihRadTableView;

    @FXML
    private TableColumn<Rad, String> verleihTVName;

    @FXML
    private TableColumn<Rad, String> verleihTVGroese;

    @FXML
    private TableColumn<Rad, String> verleihTVMarke;

    @FXML
    private Button verleihButtonSubmit;

    private Connection connection;
    private RadController radController;
    private LagerController lagerController;
    private MitarbeiterController mitarbeiterController;
    private VerleihController verleihController;
    private JdbcMitarbeiterRepository mitarbeiterRepository;
    private VerleihRepository verleihRepository;
    private JdbcLagerRepository lagerRepository;
    private JdbcRadRepository radRepository;
    private JdbcKundeRepository kundeRepository;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            connection = ConnectionManager.getInstance();
        } catch (SQLException e) {
            alert("Verbindung zu Datenbank konnte nicht hergestellt werden \n" + e.getMessage());
        }
        mitarbeiterRepository = new JdbcMitarbeiterRepository(connection);
        verleihRepository = new JdbcVerleihRepository(connection);
        lagerRepository = new JdbcLagerRepository(connection);
        radRepository = new JdbcRadRepository(connection);
        kundeRepository = new JdbcKundeRepository(connection);

        initializeRadController();
        initializeLagerController();
        initializeMitarbeiterController();
        initializeVerleihController();

        update();
    }

    public void update() {
        try {
            radController.fillTable();
            lagerController.fillTable();
            mitarbeiterController.fillTable();
            verleihController.fillTable();
        } catch (SQLException e) {
            alert("Fehler in Datenbank aufgetreten: \n" + e.getMessage());
        }
    }

    private void initializeVerleihController() {
        verleihController = new VerleihController(
                verleihRepository,
                radRepository,
                this,
                kundeRepository,
                tabVerleih,
                tableVerleih,
                tvVerleihID,
                tvVerleihKunde,
                tvVerleihDatum,
                tvVerleihDauer,
                tvVerleihPreis,
                tvVerleihRad,
                tvVerleihZusatzTools,
                verleihKundenChoiceBox,
                verleihDatum,
                verleihDauerTF,
                verleihPreisTF,
                verleihRadTableView,
                verleihTVName,
                verleihTVGroese,
                verleihTVMarke,
                verleihZusatzToolsTF,
                verleihButtonSubmit
        );
    }
    private void initializeMitarbeiterController() {
        mitarbeiterController = new MitarbeiterController(
                mitarbeiterRepository,
                tabMitarbeiter,
                tableViewMitarbeiter,
                tvMitarbeiterName,
                tvMitarbeiterHandy
        );
    }
    private void initializeLagerController() {
        lagerController = new LagerController(
                lagerRepository,
                radRepository,
                tabLager,
                tableViewLager,
                tvLagerID,
                tvLagerName,
                tvLagerAnzahlRaeder);
    }
    private void initializeRadController() {
        radController = new RadController(
                tabRaeder,
                radRepository,
                tableViewRaeder,
                tvRaederID,
                tvRaederGroese,
                tvRaederLager,
                tvRaederMarke,
                tvRaederName,
                tvRaederPreis);
    }

    public static void alert(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(text);
        alert.showAndWait();
    }

}
