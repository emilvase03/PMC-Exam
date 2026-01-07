package dk.easv.pmcexam.GUI.Controllers;

// Project imports
import dk.easv.pmcexam.BE.Genre;
import dk.easv.pmcexam.BE.Movie;
import dk.easv.pmcexam.GUI.Models.MovieModel;

// Java imports
import dk.easv.pmcexam.GUI.Utils.AlertHelper;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {

    private MovieModel movieModel;

    @FXML
    private TableView<Movie> movieList;
    @FXML
    private ListView<Genre> genreList;
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

    public MainViewController() {
        try {
            movieModel = MovieModel.getInstance();
        } catch (Exception e) {
            // Display error to user
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            setupTables();
        } catch (Exception e) {
            // Display error to user
            throw new RuntimeException(e);
        }
    }

    private void setupTables() throws Exception {
        colPersonalRating.setCellValueFactory(new PropertyValueFactory<>("personalRating"));
        colIMDBRating.setCellValueFactory(new PropertyValueFactory<>("imdbRating"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colGenre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        movieList.setItems(movieModel.getObservableMovies());
    }

    @FXML
    private void onBtnClickAddMovie(ActionEvent actionEvent) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/addMovieView.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("New Movie");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.showAndWait();

            AddMovieViewController controller = fxmlLoader.getController();
            if (controller.isMovieAdded()) {
                movieModel.refreshMovies();
                selectAndScrollToLastItem(movieList, movieModel.getObservableMovies());
            }
        } catch (Exception e) {
            AlertHelper.showError("Error", "Failed to open Add Movie window: " + e.getMessage());
        }
    }

    @FXML
    private void onBtnClickAddGenre(ActionEvent actionEvent) {
    }

    @FXML
    private void onBtnClickUpdateRating(ActionEvent actionEvent) {
    }

    @FXML
    private void onBtnClickDeleteMovie(ActionEvent actionEvent) {
        Movie selectedMovie = movieList.getSelectionModel().getSelectedItem();

        if (selectedMovie == null) {
            AlertHelper.showWarning("No Selection", "Please select a movie to delete.");
            return;
        }

        boolean confirmed = AlertHelper.showConfirmation("Delete Movie", "Are you sure you want to delete '" + selectedMovie.getTitle() + "'?");

        if (confirmed) {
            try {
                movieModel.deleteMovie(selectedMovie);
                movieModel.getObservableMovies().remove(selectedMovie);
                AlertHelper.showInformation("Success", "Movie deleted succesfully!");
            } catch (Exception e) {
                AlertHelper.showError("Error", "Failed to delete movie: " + e.getMessage());
            }
        }
    }

    @FXML
    private void onBtnClickDeleteGenre(ActionEvent actionEvent) {
    }

    private <T> void selectAndScrollToLastItem(TableView<T> table, ObservableList<T> items) {
        int newIndex = items.size() - 1;
        if (newIndex >= 0) {
            table.getSelectionModel().select(newIndex);
            table.scrollTo(newIndex);
        }
    }
}
