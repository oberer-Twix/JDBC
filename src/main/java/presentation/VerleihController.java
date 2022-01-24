package presentation;

import domain.Kunde;
import domain.Mitarbeiter;
import domain.Rad;
import domain.Verleih;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import persistence.kunde.JdbcKundeRepository;
import persistence.rad.JdbcRadRepository;
import persistence.verleih.VerleihRepository;

import java.sql.SQLException;
import java.time.LocalDate;

public class VerleihController {
    private final VerleihRepository verleihRepository;
    private final JdbcRadRepository radRepository;
    private final Controller controller;
    private final JdbcKundeRepository kundeRepository;
    private final AnchorPane tabVerleih;
    private final TableView<Verleih> tableVerleih;
    private final TableColumn<Verleih, Integer> tvVerleihID;
    private final TableColumn<Verleih, String> tvVerleihKunde;
    private final TableColumn<Verleih, LocalDate> tvVerleihDatum;
    private final TableColumn<Verleih, Integer> tvVerleihDauer;
    private final TableColumn<Verleih, Double> tvVerleihPreis;
    private final TableColumn<Verleih, String> tvVerleihRad;
    private final TableColumn<Verleih, String> tvVerleihZusatzTools;
    private final ChoiceBox<Kunde> verleihKundenChoiceBox;
    private final DatePicker verleihDatum;
    private final TextField verleihDauerTF;
    private final TextField verleihPreisTF;
    private final TableView<Rad> verleihRadTableView;
    private final TableColumn<Rad, String> verleihTVName;
    private final TableColumn<Rad, String> verleihTVGroese;
    private final TableColumn<Rad, String> verleihTVMarke;
    private final TextField verleihZusatzToolsTF;
    private final Button verleihButtonSubmit;

    public VerleihController(
            VerleihRepository verleihRepository,
            JdbcRadRepository radRepository,
            Controller controller,
            JdbcKundeRepository kundeRepository,
            AnchorPane tabVerleih,
            TableView<Verleih> tableVerleih,
            TableColumn<Verleih, Integer> tvVerleihID,
            TableColumn<Verleih, String> tvVerleihKunde,
            TableColumn<Verleih, LocalDate> tvVerleihDatum,
            TableColumn<Verleih, Integer> tvVerleihDauer,
            TableColumn<Verleih, Double> tvVerleihPreis,
            TableColumn<Verleih, String> tvVerleihRad,
            TableColumn<Verleih, String> tvVerleihZusatzTools,
            ChoiceBox<Kunde> verleihKundenChoiceBox,
            DatePicker verleihDatum,
            TextField verleihDauerTF,
            TextField verleihPreisTF,
            TableView<Rad> verleihRadTableView,
            TableColumn<Rad, String> verleihTVName,
            TableColumn<Rad, String> verleihTVGroese,
            TableColumn<Rad, String> verleihTVMarke,
            TextField verleihZusatzToolsTF,
            Button verleihButtonSubmit) {

        this.verleihRepository = verleihRepository;
        this.radRepository = radRepository;
        this.controller = controller;
        this.kundeRepository = kundeRepository;
        this.tabVerleih = tabVerleih;
        this.tableVerleih = tableVerleih;
        this.tvVerleihID = tvVerleihID;
        this.tvVerleihKunde = tvVerleihKunde;
        this.tvVerleihDatum = tvVerleihDatum;
        this.tvVerleihDauer = tvVerleihDauer;
        this.tvVerleihPreis = tvVerleihPreis;
        this.tvVerleihRad = tvVerleihRad;
        this.tvVerleihZusatzTools = tvVerleihZusatzTools;
        this.verleihKundenChoiceBox = verleihKundenChoiceBox;
        this.verleihDatum = verleihDatum;
        this.verleihDauerTF = verleihDauerTF;
        this.verleihPreisTF = verleihPreisTF;
        this.verleihRadTableView = verleihRadTableView;
        this.verleihTVName = verleihTVName;
        this.verleihTVGroese = verleihTVGroese;
        this.verleihTVMarke = verleihTVMarke;
        this.verleihZusatzToolsTF = verleihZusatzToolsTF;
        this.verleihButtonSubmit = verleihButtonSubmit;

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
        controller.update();
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
}
