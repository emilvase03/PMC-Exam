package dk.easv.pmcexam.GUI.Controllers;

// Project imports
import dk.easv.pmcexam.BE.Movie;
import dk.easv.pmcexam.GUI.Models.GenreModel;
import dk.easv.pmcexam.GUI.Models.MovieModel;
import dk.easv.pmcexam.GUI.Utils.ValidationHelper;
import dk.easv.pmcexam.GUI.Utils.AlertHelper;

// Java imports
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddMovieViewController implements Initializable {
    private boolean movieAdded;
    private MovieModel movieModel = MovieModel.getInstance();
    private GenreModel genreModel = GenreModel.getInstance();
    private ObservableSet<String> selectedGenres = FXCollections.observableSet();

    @FXML private TextField txtTitle;
    @FXML private TextField txtPersonalRating;
    @FXML private TextField txtIMDBRating;
    @FXML private TextField txtFilePath;
    @FXML private ComboBox<String> cbGenre;
    @FXML private Button btnSave;
    @FXML private Button btnChoose;
    @FXML private Button btnCancel;

    public AddMovieViewController() throws Exception {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupGenreComboBox();
    }

    private void setupGenreComboBox() {
        try {
            // Load all available genres from database
            List<String> allGenres = genreModel.getAllGenreNames();
            cbGenre.getItems().addAll(allGenres);
            cbGenre.setPromptText("Select genres...");

            // Setup button cell to show selected genres
            cbGenre.setButtonCell(new ListCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || selectedGenres.isEmpty()) {
                        setText("Select genres...");
                    } else {
                        setText(String.join(", ", selectedGenres));
                    }
                }
            });

            // Setup multi-selection ComboBox
            cbGenre.setCellFactory(listView -> {
                ListCell<String> cell = new ListCell<>() {
                    private final CheckBox checkBox = new CheckBox();

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            checkBox.setText(item);
                            checkBox.setSelected(selectedGenres.contains(item));
                            setGraphic(checkBox);
                            setText(null);
                        }
                    }
                };

                // Use addEventFilter to intercept the event before it reaches the default handler
                cell.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                    if (!cell.isEmpty()) {
                        String item = cell.getItem();
                        CheckBox checkBox = (CheckBox) cell.getGraphic();

                        if (selectedGenres.contains(item)) {
                            selectedGenres.remove(item);
                            checkBox.setSelected(false);
                        } else {
                            selectedGenres.add(item);
                            checkBox.setSelected(true);
                        }

                        // Update button cell display
                        cbGenre.setButtonCell(new ListCell<>() {
                            @Override
                            protected void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (selectedGenres.isEmpty()) {
                                    setText("Select genres...");
                                } else {
                                    setText(String.join(", ", selectedGenres));
                                }
                            }
                        });

                        event.consume(); // Prevent default selection behavior
                    }
                });

                return cell;
            });

            // Prevent ComboBox from selecting a single value
            cbGenre.valueProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    cbGenre.setValue(null);
                }
            });

            // Clear the selection model to prevent IndexOutOfBoundsException
            cbGenre.getSelectionModel().clearSelection();

        } catch (Exception e) {
            AlertHelper.showError("Error", "Failed to load genres: " + e.getMessage());
        }
    }

    @FXML
    private void onBtnSave(ActionEvent actionEvent) {
        String title = txtTitle.getText();
        String personalRatingStr = txtPersonalRating.getText();
        String imdbRatingStr = txtIMDBRating.getText();
        List<String> genres = new ArrayList<>(selectedGenres);
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
            cbGenre.requestFocus();
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
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void onBtnChoose(ActionEvent actionEvent) {
        javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();

        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Video Files (*.mp4, *.mpeg4)", "*.mp4", "*.mpeg4");
        fileChooser.getExtensionFilters().add(extFilter);

        File selectedFile = fileChooser.showOpenDialog(txtFilePath.getScene().getWindow());

        if (selectedFile != null) {
            String filePath = selectedFile.getAbsolutePath();
            txtFilePath.setText(filePath);
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