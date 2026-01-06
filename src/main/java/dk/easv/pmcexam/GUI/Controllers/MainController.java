package dk.easv.pmcexam.GUI.Controllers;

// Java imports
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MainController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    private void onBtnClickAddMovie(ActionEvent actionEvent) {
    }

    @FXML
    private void onBtnClickAddGenre(ActionEvent actionEvent) {
    }

    @FXML
    private void onBtnClickUpdateRating(ActionEvent actionEvent) {
    }

    @FXML
    private void onBtnClickDeleteMovie(ActionEvent actionEvent) {
    }
}
