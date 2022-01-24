package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import persistence.ConnectionManager;

import java.sql.SQLException;
import java.util.Objects;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/FXML.fxml")));
        primaryStage.setTitle("Fahradverwaltung");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.setOnCloseRequest(windowEvent -> {
            try {
                close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private static void close() throws SQLException {
        ConnectionManager.closeConnection();
        System.out.println("Connection is closed successfully!");
    }

}
