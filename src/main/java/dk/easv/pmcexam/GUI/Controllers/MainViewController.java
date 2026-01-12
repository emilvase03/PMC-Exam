package dk.easv.pmcexam.GUI.Controllers;

// Project imports
import dk.easv.pmcexam.BE.Genre;
import dk.easv.pmcexam.BE.Movie;
import dk.easv.pmcexam.GUI.Models.GenreModel;
import dk.easv.pmcexam.GUI.Models.MovieModel;

// Java imports
import dk.easv.pmcexam.GUI.Utils.AlertHelper;
import dk.easv.pmcexam.GUI.Utils.VideoPlayer;
import javafx.application.HostServices;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
import javafx.util.Callback;

import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {

    private MovieModel movieModel;
    private GenreModel genreModel;
    private HostServices hostServices;

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
            setupSearch();
        } catch (Exception e) {
            AlertHelper.showException("Error", "Failed to initialize setups.", e);
        }
    }



    private void setupTables() throws Exception {
        colPersonalRating.setCellValueFactory(new PropertyValueFactory<>("personalRating"));
        colIMDBRating.setCellValueFactory(new PropertyValueFactory<>("imdbRating"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colGenre.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Movie, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Movie, String> param) {
                return new SimpleStringProperty(param.getValue().getGenresAsString());
            }
        });

        movieList.setItems(movieModel.getObservableMovies());

        colGenreId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colGenreName.setCellValueFactory(new PropertyValueFactory<>("name"));
        genreList.setItems(genreModel.getObservableGenres());
    }

    private void setupSearch() {
        try {
            ObservableList<Movie> movieBase = movieModel.getObservableMovies();
            ObservableList<Genre> genreBase = genreModel.getObservableGenres();

            FilteredList<Movie> filteredMovies = new FilteredList<>(movieBase, m -> true);
            FilteredList<Genre> filteredGenres = new FilteredList<>(genreBase, g -> true);

            SortedList<Movie> sortedMovies = new SortedList<>(filteredMovies);
            sortedMovies.comparatorProperty().bind(movieList.comparatorProperty());
            movieList.setItems(sortedMovies);

            SortedList<Genre> sortedGenres = new SortedList<>(filteredGenres);
            sortedGenres.comparatorProperty().bind(genreList.comparatorProperty());
            genreList.setItems(sortedGenres);

            searchMovie.textProperty().addListener((obs, oldVal, newVal) -> {
                String query = newVal == null ? "" : newVal.trim().toLowerCase();

                filteredMovies.setPredicate(movie ->
                        query.isEmpty()
                                || (movie.getTitle() != null &&
                                movie.getTitle().toLowerCase().contains(query))
                                || (movie.getGenresAsString() != null &&
                                movie.getGenresAsString().toLowerCase().contains(query))
                                || String.valueOf(movie.getImdbRating()).contains(query)
                                || String.valueOf(movie.getPersonalRating()).contains(query)
                );

                filteredGenres.setPredicate(genre ->
                        query.isEmpty()
                                || (genre.getName() != null &&
                                genre.getName().toLowerCase().contains(query))
                );
            });

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

    private <T> void selectAndScrollToLastItem(TableView<T> table, ObservableList<T> items) {
        int newIndex = items.size() - 1;
        if (newIndex >= 0) {
            table.getSelectionModel().select(newIndex);
            table.scrollTo(newIndex);
        }
    }

    @FXML
    private void onBtnClickPlay(ActionEvent actionEvent) {
        Movie selectedMovie = movieList.getSelectionModel().getSelectedItem();

        if(selectedMovie == null) {
            AlertHelper.showWarning("No Movie Selected", "Please select a movie to play");
            return;
        }

        VideoPlayer.playVideo(hostServices, selectedMovie.getFilePath());
    }

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;

    }
}
