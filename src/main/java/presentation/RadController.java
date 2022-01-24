package presentation;

import domain.Rad;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import persistence.rad.JdbcRadRepository;
import persistence.rad.RadRepository;

import java.sql.SQLException;

public class RadController {

    private Pane pane;
    private TableView<Rad> tableViewRaeder;
    private TableColumn<Rad, Integer> tvRaederID;
    private TableColumn<Rad, String> tvRaederGroese;
    private TableColumn<Rad, String> tvRaederLager;
    private TableColumn<Rad, String> tvRaederMarke;
    private TableColumn<Rad, String> tvRaederName;
    private TableColumn<Rad, Double> tvRaederPreis;

    private RadRepository radRepository;

    public RadController(AnchorPane tabRaeder, JdbcRadRepository radRepository, TableView<Rad> tableViewRaeder, TableColumn<Rad, Integer> tvRaederID,
                         TableColumn<Rad, String> tvRaederGroese, TableColumn<Rad, String> tvRaederLager, TableColumn<Rad,
            String> tvRaederMarke, TableColumn<Rad, String> tvRaederName, TableColumn<Rad, Double> tvRaederPreis) {
        pane = tabRaeder;
        this.radRepository = radRepository;
        this.tableViewRaeder = tableViewRaeder;
        this.tvRaederID = tvRaederID;
        this.tvRaederGroese = tvRaederGroese;
        this.tvRaederLager = tvRaederLager;
        this.tvRaederMarke = tvRaederMarke;
        this.tvRaederName = tvRaederName;
        this.tvRaederPreis = tvRaederPreis;

        tvRaederID.setCellValueFactory(new PropertyValueFactory<>("radID"));
        tvRaederGroese.setCellValueFactory(new PropertyValueFactory<>("radGroese"));
        tvRaederLager.setCellValueFactory(new PropertyValueFactory<>("radLager"));
        tvRaederPreis.setCellValueFactory(new PropertyValueFactory<>("radKaufpreis"));
        tvRaederMarke.setCellValueFactory(new PropertyValueFactory<>("radMarke"));
        tvRaederName.setCellValueFactory(new PropertyValueFactory<>("radName"));

    }

    public void fillTable() throws SQLException {
        tableViewRaeder.getItems().clear();
        tableViewRaeder.getItems().addAll(radRepository.findAll());
    }

}


