package presentation;

import domain.Mitarbeiter;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import persistence.mitarbeiter.MitarbeiterRepository;

import java.sql.SQLException;

public class MitarbeiterController {

    private MitarbeiterRepository mitarbeiterRepository;
    private AnchorPane tabMitarbeiter;
    private TableView<Mitarbeiter> tableViewMitarbeiter;
    private TableColumn<Mitarbeiter, String> tvMitarbeiterName;
    private TableColumn<Mitarbeiter, String> tvMitarbeiterHandy;

    public MitarbeiterController(
            MitarbeiterRepository mitarbeiterRepository,
            AnchorPane tabMitarbeiter,
            TableView<Mitarbeiter> tableViewMitarbeiter,
            TableColumn<Mitarbeiter, String> tvMitarbeiterName,
            TableColumn<Mitarbeiter, String> tvMitarbeiterHandy
    ) {
        this.mitarbeiterRepository = mitarbeiterRepository;
        this.tabMitarbeiter = tabMitarbeiter;
        this.tableViewMitarbeiter = tableViewMitarbeiter;
        this.tvMitarbeiterName = tvMitarbeiterName;
        this.tvMitarbeiterHandy = tvMitarbeiterHandy;

        tvMitarbeiterName.setCellValueFactory(new PropertyValueFactory<>("mitarbeiterName"));
        tvMitarbeiterHandy.setCellValueFactory(new PropertyValueFactory<>("handynummer"));
    }

    public void fillTable() throws SQLException {
        tableViewMitarbeiter.getItems().clear();
        tableViewMitarbeiter.getItems().addAll(mitarbeiterRepository.getAllMitarbeiter());
    }
}
