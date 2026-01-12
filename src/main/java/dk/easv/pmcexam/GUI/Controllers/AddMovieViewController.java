package dk.easv.pmcexam.GUI.Controllers;

// Project imports
import dk.easv.pmcexam.BE.Movie;
import dk.easv.pmcexam.GUI.Models.GenreModel;
import dk.easv.pmcexam.GUI.Models.MovieModel;
import dk.easv.pmcexam.GUI.Utils.ValidationHelper;
import dk.easv.pmcexam.GUI.Utils.AlertHelper;

// Java imports
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class AddMovieViewController implements Initializable {
    private boolean movieAdded;
    private MovieModel movieModel = MovieModel.getInstance();
    private GenreModel genreModel = GenreModel.getInstance();
    private Map<String, CheckBox> genreCheckBoxes = new HashMap<>();

    @FXML private TextField txtTitle;
    @FXML private TextField txtPersonalRating;
    @FXML private TextField txtIMDBRating;
    @FXML private TextField txtFilePath;
    @FXML private ListView<CheckBox> lvGenres;
    @FXML private Button btnSave;
    @FXML private Button btnChoose;
    @FXML private Button btnCancel;

    public AddMovieViewController() throws Exception {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupGenreListView();
    }

    private void setupGenreListView() {
        try {
            // Load all available genres from database
            List<String> allGenres = genreModel.getAllGenreNames();

            // Create a checkbox for each genre
            for (String genreName : allGenres) {
                CheckBox checkBox = new CheckBox(genreName);
                genreCheckBoxes.put(genreName, checkBox);
                lvGenres.getItems().add(checkBox);
            }

        } catch (Exception e) {
            AlertHelper.showError("Error", "Failed to load genres: " + e.getMessage());
        }
    }

    private List<String> getSelectedGenres() {
        List<String> selectedGenres = new ArrayList<>();
        for (Map.Entry<String, CheckBox> entry : genreCheckBoxes.entrySet()) {
            if (entry.getValue().isSelected()) {
                selectedGenres.add(entry.getKey());
            }
        }
        return selectedGenres;
    }

    @FXML
    private void onBtnSave(ActionEvent actionEvent) {
        String title = txtTitle.getText();
        String personalRatingStr = txtPersonalRating.getText();
        String imdbRatingStr = txtIMDBRating.getText();
        List<String> genres = getSelectedGenres();
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

        if (genres.isEmpty()) {
            AlertHelper.showWarning("Validation Error", "Please select at least one genre.");
            lvGenres.requestFocus();
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

            Movie newMovie = new Movie(title, personalRating, imdbRating, genres, filePath);
            movieModel.createMovie(newMovie);

            movieAdded = true;
            AlertHelper.showInformation("Success", "Movie added successfully!");
            closeWindow();

        } catch (Exception e) {
            AlertHelper.showError("Error", "Failed to add movie: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void onBtnChoose(ActionEvent actionEvent) {
        javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();

        File videosDir = new File("src/main/resources/videos");

        if (!videosDir.exists()) {
            videosDir = new File(System.getProperty("user.home"));
        }

        fileChooser.setInitialDirectory(videosDir);

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "Video Files (*.mp4, *.mpeg4)", "*.mp4", "*.mpeg4");
        fileChooser.getExtensionFilters().add(extFilter);

        File selectedFile = fileChooser.showOpenDialog(txtFilePath.getScene().getWindow());

        if (selectedFile != null) {
            try {
                Path projectVideosDir = Paths.get("src/main/resources/videos");
                if (!Files.exists(projectVideosDir)) {
                    Files.createDirectories(projectVideosDir);  // may throw IOException
                }

                Path target = projectVideosDir.resolve(selectedFile.getName());
                Files.copy(selectedFile.toPath(), target, StandardCopyOption.REPLACE_EXISTING); // may throw IOException

                Path relativePath = projectVideosDir.relativize(target);
                txtFilePath.setText("videos/" + relativePath.toString().replace("\\", "/"));

            } catch (IOException e) {
                AlertHelper.showError("Error", "Failed to add videos: " + e.getMessage());
            }
        }
    }

    @FXML
    private void onBtnCancel(ActionEvent actionEvent) {
        closeWindow();
    }

    public boolean isMovieAdded() {
        return movieAdded;
    }

    private void closeWindow() {
        Stage stage = (Stage) btnSave.getScene().getWindow();
        stage.close();
    }
}