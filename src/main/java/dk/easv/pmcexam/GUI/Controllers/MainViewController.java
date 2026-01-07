package dk.easv.pmcexam.GUI.Controllers;

// Project imports
import dk.easv.pmcexam.BE.Genre;
import dk.easv.pmcexam.BE.Movie;
import dk.easv.pmcexam.GUI.Models.GenreModel;
import dk.easv.pmcexam.GUI.Models.MovieModel;

// Java imports
import dk.easv.pmcexam.GUI.Utils.AlertHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {

    private MovieModel movieModel;
    private GenreModel genreModel;

    @FXML
    private TableView<Movie> movieList;
    @FXML
    private TableView<Genre> genreList;
    @FXML
    private Button addMovie;
    @FXML
    private Button addGenre;
    @FXML
    private MenuItem updateRating;
    @FXML
    private MenuItem deleteMovie;
    @FXML
    private TextField searchMovie;
    @FXML
    private Button deleteGenre;
    @FXML
    private TableColumn colPersonalRating;
    @FXML
    private TableColumn colIMDBRating;
    @FXML
    private TableColumn colTitle;
    @FXML
    private TableColumn colGenre;
    @FXML
    private TableColumn colGenreId;
    @FXML
    private TableColumn colGenreName;

    public MainViewController() {
        try {
            movieModel = MovieModel.getInstance();
            genreModel = GenreModel.getInstance();
        } catch (Exception e) {
            AlertHelper.showException("Error", "Failed to get instances", e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            setupTables();
        } catch (Exception e) {
            AlertHelper.showException("Error", "Failed to setup tables", e);
        }
    }

    private void setupTables() throws Exception {
        colPersonalRating.setCellValueFactory(new PropertyValueFactory<>("personalRating"));
        colIMDBRating.setCellValueFactory(new PropertyValueFactory<>("imdbRating"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colGenre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        movieList.setItems(movieModel.getObservableMovies());

        colGenreId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colGenreName.setCellValueFactory(new PropertyValueFactory<>("name"));
        genreList.setItems(genreModel.getObservableGenres());
    }

    @FXML
    private void onBtnClickAddMovie(ActionEvent actionEvent) {
    }

    @FXML
    private void onBtnClickUpdateRating(ActionEvent actionEvent) {
    }

    @FXML
    private void onBtnClickDeleteMovie(ActionEvent actionEvent) {
    }

    @FXML
    private void onBtnClickAddGenre(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AddGenreView.fxml"));
            Parent root = loader.load();

            AddGenreViewController controller = loader.getController();

            Stage stage = new Stage();
            stage.setTitle("Add Genre");
            stage.setScene(new Scene(root));

            stage.initModality(Modality.APPLICATION_MODAL);

            stage.showAndWait();

            if (controller.isGenreAdded()) {
                genreModel.refreshGenres();
            }

        } catch (Exception e) {
            AlertHelper.showError("Error", "Failed to open Add Genre window: " + e.getMessage());
        }
    }

    @FXML
    private void onBtnClickDeleteGenre(ActionEvent actionEvent) {
        Genre selectedGenre = genreList.getSelectionModel().getSelectedItem();

        if (selectedGenre == null) {
            AlertHelper.showWarning("No Selection", "Please select a genre to delete.");
            return;
        }

        boolean confirmed = AlertHelper.showConfirmation(
                "Delete Genre",
                "Are you sure you want to delete '" + selectedGenre.getName() + "'?"
        );

        if (confirmed) {
            try {
                genreModel.deleteGenre(selectedGenre);
                AlertHelper.showInformation("Success", "Genre deleted successfully!");
            } catch (Exception e) {
                AlertHelper.showError("Error", "Failed to delete genre: " + e.getMessage());
            }
        }
    }
}
