package dk.easv.pmcexam.GUI.Controllers;

import dk.easv.pmcexam.BE.Movie;
import dk.easv.pmcexam.GUI.Models.MovieModel;
import dk.easv.pmcexam.GUI.Utils.ValidationHelper;
import dk.easv.pmcexam.GUI.Utils.AlertHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddMovieViewController {
    private boolean movieAdded;
    private MovieModel movieModel = MovieModel.getInstance();

    @FXML private TextField txtTitle;
    @FXML private TextField txtPersonalRating;
    @FXML private TextField txtIMDBRating;
    @FXML private TextField txtGenre;
    @FXML private TextField txtFilePath;
    @FXML private Button btnSave;
    @FXML private Button btnChoose;
    @FXML private Button btnCancel;

    public AddMovieViewController() throws Exception {
    }

    @FXML
    private void onBtnSave(ActionEvent actionEvent) {
        String title = txtTitle.getText();
        String personalRatingStr = txtPersonalRating.getText();
        String imdbRatingStr = txtIMDBRating.getText();
        String genre = txtGenre.getText();
        String filePath = txtFilePath.getText();

        if (ValidationHelper.isNullOrEmpty(title)) {
            AlertHelper.showWarning("Validation Error", "Please enter a movie title.");
            txtTitle.requestFocus();
            return;
        }

        if (!ValidationHelper.isValidRating(personalRatingStr)) {
            AlertHelper.showWarning("Validation Error",
                    "Please enter a valid personal rating (0-10).");
            txtPersonalRating.requestFocus();
            return;
        }

        if (!ValidationHelper.isValidRating(imdbRatingStr)) {
            AlertHelper.showWarning("Validation Error",
                    "Please enter a valid IMDB rating (0-10).");
            txtIMDBRating.requestFocus();
            return;
        }

        if (ValidationHelper.isNullOrEmpty(genre)) {
            AlertHelper.showWarning("Validation Error", "Please enter a genre.");
            txtGenre.requestFocus();
            return;
        }

        if (ValidationHelper.isNullOrEmpty(filePath)) {
            AlertHelper.showWarning("Validation Error", "Please enter a file path.");
            txtFilePath.requestFocus();
            return;
        }

        if (!ValidationHelper.isValidFilePath(filePath)) {
            AlertHelper.showWarning("Validation Error", "Please only use .mp4 or .mpeg4 files.");
            txtFilePath.requestFocus();
            return;
        }

        try {
            float personalRating = Float.parseFloat(personalRatingStr);
            float imdbRating = Float.parseFloat(imdbRatingStr);

            Movie newMovie = new Movie(title, personalRating, imdbRating, genre, filePath);
            movieModel.createMovie(newMovie);

            movieAdded = true;
            AlertHelper.showInformation("Success", "Movie added successfully!");
            closeWindow();

        } catch (Exception e) {
            AlertHelper.showError("Error", "Failed to add movie: " + e.getMessage());
        }

    }

    @FXML
    private void onBtnChoose(ActionEvent actionEvent) {
    }

    @FXML
    private void onBtnCancel(ActionEvent actionEvent) {

    }

    public boolean isMovieAdded() {
        return movieAdded;
    }

    private void closeWindow() {
        Stage stage = (Stage) btnSave.getScene().getWindow();
        stage.close();
    }
}
