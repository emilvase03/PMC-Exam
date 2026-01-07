package dk.easv.pmcexam.GUI.Controllers;

import dk.easv.pmcexam.BE.Genre;
import dk.easv.pmcexam.GUI.Models.GenreModel;
import dk.easv.pmcexam.GUI.Utils.AlertHelper;
import dk.easv.pmcexam.GUI.Utils.ValidationHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddGenreViewController {

    @FXML
    private TextField txtGenreName;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnCancel;

    private GenreModel genreModel;
    private boolean genreAdded = false;

    public AddGenreViewController() {
        try {
            genreModel = GenreModel.getInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void onBtnClickSave(ActionEvent actionEvent) {
        String genreName = txtGenreName.getText();

        if (ValidationHelper.isNullOrEmpty(genreName)) {
            AlertHelper.showWarning("Validation Error", "Please enter a genre name.");
            return;
        }

        try {
            Genre newGenre = new Genre(genreName);
            genreModel.createGenre(newGenre);

            genreAdded = true;
            AlertHelper.showInformation("Success", "Genre added successfully!");

            closeWindow();
        } catch (Exception e) {
            AlertHelper.showError("Error", "Failed to add genre: " + e.getMessage());
        }
    }

    @FXML
    private void onBtnClickCancel(ActionEvent actionEvent) {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    public boolean isGenreAdded() {
        return genreAdded;
    }
}