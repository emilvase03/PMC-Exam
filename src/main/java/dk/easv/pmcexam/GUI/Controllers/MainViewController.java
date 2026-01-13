package dk.easv.pmcexam.GUI.Controllers;

// Project imports
import dk.easv.pmcexam.BE.Genre;
import dk.easv.pmcexam.BE.Movie;
import dk.easv.pmcexam.GUI.Models.GenreModel;
import dk.easv.pmcexam.GUI.Models.MovieModel;
import dk.easv.pmcexam.GUI.Utils.AlertHelper;
import dk.easv.pmcexam.GUI.Utils.MovieDateChecker;
import dk.easv.pmcexam.GUI.Utils.ValidationHelper;
import dk.easv.pmcexam.GUI.Utils.VideoPlayer;

// Java imports
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
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {

    private MovieModel movieModel;
    private GenreModel genreModel;
    private HostServices hostServices;
    private FilteredList<Movie> filteredMovies;
    private FilteredList<Genre> filteredGenres;
    private Genre selectedGenreFilter = null;

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
            setupListeners();
            checkLastviewDates(movieList.getItems());
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

        colGenreName.setCellValueFactory(new PropertyValueFactory<>("name"));
        genreList.setItems(genreModel.getObservableGenres());
    }

    private void setupListeners() {
        movieList.setRowFactory(tv -> {
            TableRow<Movie> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    Movie movie = row.getItem();
                    VideoPlayer.playVideo(hostServices, movie.getFilePath());
                }
            });
            return row;
        });

        final Genre[] lastSelectedGenre = {null};

        genreList.setOnMouseClicked(event -> {
            Genre currentSelection = genreList.getSelectionModel().getSelectedItem();

            // toggle deselect
            if (currentSelection != null && currentSelection.equals(lastSelectedGenre[0])) {
                genreList.getSelectionModel().clearSelection();
                lastSelectedGenre[0] = null;
                selectedGenreFilter = null;
                updateFilters();
            } else {
                lastSelectedGenre[0] = currentSelection;
                selectedGenreFilter = currentSelection;
                updateFilters();
            }
        });
    }

    private void updateFilters() {
        String query = searchMovie.getText() == null ? "" : searchMovie.getText().trim().toLowerCase();

        filteredMovies.setPredicate(movie -> {
            boolean matchesGenre = selectedGenreFilter == null ||
                    (movie.getGenresAsString() != null &&
                            movie.getGenresAsString().toLowerCase().contains(selectedGenreFilter.getName().toLowerCase()));

            if (!matchesGenre) {
                return false;
            }

            return query.isEmpty()
                    || (movie.getTitle() != null && movie.getTitle().toLowerCase().contains(query))
                    || (movie.getGenresAsString() != null && movie.getGenresAsString().toLowerCase().contains(query))
                    || String.valueOf(movie.getImdbRating()).contains(query)
                    || String.valueOf(movie.getPersonalRating()).contains(query);
        });

        // filter genres
        if (selectedGenreFilter == null) {
            filteredGenres.setPredicate(genre ->
                    query.isEmpty()
                            || (genre.getName() != null &&
                            genre.getName().toLowerCase().contains(query))
            );
        } else {
            // dont filter genres if genre is selected
            filteredGenres.setPredicate(genre -> true);
        }
    }

    private void setupSearch() {
        try {
            ObservableList<Movie> movieBase = movieModel.getObservableMovies();
            ObservableList<Genre> genreBase = genreModel.getObservableGenres();

            filteredMovies = new FilteredList<>(movieBase, m -> true);
            filteredGenres = new FilteredList<>(genreBase, g -> true);

            SortedList<Movie> sortedMovies = new SortedList<>(filteredMovies);
            sortedMovies.comparatorProperty().bind(movieList.comparatorProperty());
            movieList.setItems(sortedMovies);

            SortedList<Genre> sortedGenres = new SortedList<>(filteredGenres);
            sortedGenres.comparatorProperty().bind(genreList.comparatorProperty());
            genreList.setItems(sortedGenres);

            searchMovie.textProperty().addListener((obs, oldVal, newVal) -> {
                updateFilters();
            });

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void checkLastviewDates(List<Movie> movies) {
        if(MovieDateChecker.isAnyMoviesOld(movies)) {
            AlertHelper.showWarning("Alert", "You haven't watch the below mentioned movie(s) in two years or more." + "\n\n" + MovieDateChecker.getOldMoviesAsString());
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
        Movie selected = movieList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertHelper.showWarning("No Selection", "Please select a movie to update its rating.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog(String.valueOf(selected.getPersonalRating()));
        dialog.setTitle("Update Personal Rating");
        dialog.setHeaderText("Change rating for: " + selected.getTitle());
        dialog.setContentText("Enter rating (0–10):");

        Optional<String> result = dialog.showAndWait();
        if (result.isEmpty())
            return;

        String input = result.get().trim();
        if (!ValidationHelper.isValidRating(input)) {
            AlertHelper.showError("Invalid Input", "Please enter a valid number between 0 and 10.");
            return;
        }

        float newRating;
        try {
            newRating = Float.parseFloat(input);
        } catch (NumberFormatException e) {
            AlertHelper.showError("Invalid Input", "Please enter a numeric value.");
            return;
        }

        try {
            int keepId = selected.getId();
            movieModel.updatePersonalRating(selected, newRating);

            // re-select the same movie in the refreshed list
            ObservableList<Movie> list = movieModel.getObservableMovies();
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getId() == keepId) {
                    movieList.getSelectionModel().select(i);
                    movieList.scrollTo(i);
                    break;
                }
            }

            AlertHelper.showInformation("Success", "Personal rating updated.");
        } catch (Exception e) {
            AlertHelper.showError("Error", "Failed to update rating: " + e.getMessage());
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
    private void onBtnClickDeleteMovie(ActionEvent actionEvent){
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

        selectedMovie.setLastViewed(LocalDate.now());
        try {
            movieModel.updateMovie(selectedMovie);
        } catch (Exception e) {
            AlertHelper.showError("Error", "Unable to set new 'last viewed date' for this movie.");
        }
        VideoPlayer.playVideo(hostServices, selectedMovie.getFilePath());
    }

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }
}