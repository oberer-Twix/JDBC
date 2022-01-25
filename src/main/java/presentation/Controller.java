package presentation;

import javafx.scene.control.Alert;

import java.util.ArrayList;
import java.util.List;

public class Controller {

    private final static List<Updatable> controllerList = new ArrayList<>();

    public static void addToUpdateList(Updatable controller) {
        synchronized (controllerList) {
            controllerList.add(controller);
        }
    }

    public static void update() {
        synchronized (controllerList){
            controllerList.forEach(Updatable::update);
        }
    }

    public static void alert(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(text);
        alert.showAndWait();
    }

}
