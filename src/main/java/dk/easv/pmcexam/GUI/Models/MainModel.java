package dk.easv.pmcexam.GUI.Models;

// Project imports
import dk.easv.pmcexam.BE.Movie;
import dk.easv.pmcexam.BLL.MovieManager;

// Java imports
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MainModel {
    private static MainModel instance;
    private MovieManager movieManager = new MovieManager();
    private ObservableList<Movie> moviesToBeViewed;

    private MainModel() throws Exception {
        moviesToBeViewed = FXCollections.observableArrayList();
        moviesToBeViewed.addAll(movieManager.getAllMovies());
    }

    public static MainModel getInstance() throws Exception {
        if (instance == null) {
            instance = new MainModel();
        }
        return instance;
    }

    public ObservableList<Movie> getObservableMovies() throws Exception {
        return moviesToBeViewed;
    }

    public Movie createMovie(Movie newMovie) throws Exception {
        return movieManager.createMovie(newMovie);
    }

    public void updateMovie(Movie movie) throws Exception {
        movieManager.updateMovie(movie);
    }

    public void deleteMovie(Movie movie) throws Exception {
        movieManager.deleteMovie(movie);
    }
}
