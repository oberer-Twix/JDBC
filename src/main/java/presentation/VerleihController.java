package presentation;

import domain.Kunde;
import domain.Mitarbeiter;
import domain.Rad;
import domain.Verleih;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import persistence.ConnectionManager;
import persistence.kunde.JdbcKundeRepository;
import persistence.rad.JdbcRadRepository;
import persistence.verleih.JdbcVerleihRepository;
import persistence.verleih.VerleihRepository;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class VerleihController implements Initializable, Updatable {
    private VerleihRepository verleihRepository;
    private JdbcRadRepository radRepository;
    private JdbcKundeRepository kundeRepository;
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
    private TextField verleihZusatzToolsTF;
    @FXML
    private Button verleihButtonSubmit;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Connection connection = null;
        try {
            connection = ConnectionManager.getInstance();
        } catch (SQLException e) {
            Controller.alert("Verbindung zu Datenbank konnte nicht hergestellt werden \n" + e.getMessage());
        }
        radRepository = new JdbcRadRepository(connection);
        verleihRepository = new JdbcVerleihRepository(connection);
        kundeRepository = new JdbcKundeRepository(connection);

        tvVerleihID.setCellValueFactory(new PropertyValueFactory<>("verleihID"));
        tvVerleihKunde.setCellValueFactory(new PropertyValueFactory<>("verleihKunde"));
        tvVerleihDatum.setCellValueFactory(new PropertyValueFactory<>("verleihAnfangsdatum"));
        tvVerleihDauer.setCellValueFactory(new PropertyValueFactory<>("verleihDauer"));
        tvVerleihPreis.setCellValueFactory(new PropertyValueFactory<>("verleihPreis"));
        tvVerleihZusatzTools.setCellValueFactory(new PropertyValueFactory<>("verleihZusatztools"));
        tvVerleihRad.setCellValueFactory((item) -> new SimpleObjectProperty<>(getRaeder(item.getValue())));

        verleihTVName.setCellValueFactory(new PropertyValueFactory<>("radName"));
        verleihTVGroese.setCellValueFactory(new PropertyValueFactory<>("radGroese"));
        verleihTVMarke.setCellValueFactory(new PropertyValueFactory<>("radMarke"));

        verleihButtonSubmit.setOnAction(actionEvent -> submit());
        verleihRadTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        Controller.addToUpdateList(this);
        update();
    }

    private void submit() {
        var kunde = verleihKundenChoiceBox.getSelectionModel().getSelectedItem();
        var datum = verleihDatum.getValue();
        var dauerString = verleihDauerTF.getText();
        if(dauerString.isBlank() || !dauerString.matches("\\d+")){
            Controller.alert("Die Dauer muss eine Zahl sein");
            return;
        }
        int dauer = Integer.parseInt(dauerString);
        if(dauer <= 0){
            Controller.alert("Die Dauer muss größer als 0 sein");
            return;
        }
        int preis;
        var preisString = verleihPreisTF.getText();
        try {
            preis = Integer.parseInt(preisString);
            if(preis <= 0){
                throw new NumberFormatException();
            }
        }catch (NumberFormatException e){
            Controller.alert("Der Preis entspricht nicht den Erwartungen");
            return;
        }
        var raeder = verleihRadTableView.getSelectionModel().getSelectedItems();
        if(raeder.isEmpty()){
            Controller.alert("Ein Verleih ohne Räder ist nicht möglich");
            return;
        }
        var zusatzTools = verleihZusatzToolsTF.getText();
        var mitarbeiter = new Mitarbeiter("Flo", "1234");
        var verleih = new Verleih(dauer, preis, datum, zusatzTools, kunde, mitarbeiter);
        try {
            var id = verleihRepository.save(verleih);
            raeder.forEach((rad) -> {
                try {
                    verleihRepository.verwendeRadInVerleihWithIDs(id, rad.getRadID());
                    radRepository.withdraw(rad.getRadID());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        } catch (SQLException e) {
            Controller.alert("Etwas ist schief gelaufen\n" + e.getMessage());
        }
        Controller.update();
    }

    private String getRaeder(Verleih item){
        var sb = new StringBuilder();
        try {
            var raederList =  verleihRepository.findAllRaederWithVerleihId(item.getVerleihID());
            raederList.stream()
                    .map(Rad::getRadName)
                    .forEach((rad) -> sb.append(rad).append(", "));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public void fillTable() throws SQLException {
        tableVerleih.getItems().clear();
        verleihPreisTF.setText("");
        verleihDauerTF.setText("");
        verleihZusatzToolsTF.setText("");
        tableVerleih.getItems().addAll(verleihRepository.findAll());

        verleihRadTableView.getItems().clear();
        verleihRadTableView.getItems().addAll(radRepository.findAllInLager());
        verleihKundenChoiceBox.getItems().clear();
        verleihKundenChoiceBox
                .getItems()
                .addAll(kundeRepository
                        .findAll()
                        .stream()
                        .toList()
        );
        verleihKundenChoiceBox.getSelectionModel().selectFirst();
        verleihDatum.setValue(LocalDate.now());
        verleihRadTableView.getSelectionModel().selectFirst();
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
