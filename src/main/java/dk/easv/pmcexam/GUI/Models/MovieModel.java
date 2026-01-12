package dk.easv.pmcexam.GUI.Models;

// Project imports
import dk.easv.pmcexam.BE.Movie;
import dk.easv.pmcexam.BLL.MovieManager;

// Java imports
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MovieModel {
    private static MovieModel instance;
    private MovieManager movieManager = new MovieManager();
    private ObservableList<Movie> moviesToBeViewed;

    private MovieModel() throws Exception {
        moviesToBeViewed = FXCollections.observableArrayList();
        moviesToBeViewed.addAll(movieManager.getAllMovies());
    }

    public static MovieModel getInstance() throws Exception {
        if (instance == null) {
            instance = new MovieModel();
        }
        return instance;
    }

    public ObservableList<Movie> getObservableMovies() throws Exception {
        return moviesToBeViewed;
    }

    public Movie createMovie(Movie newMovie) throws Exception {
        Movie createdMovie = movieManager.createMovie(newMovie);
        moviesToBeViewed.add(createdMovie); // Add this line!
        return createdMovie;
    }

    public void updateMovie(Movie movie) throws Exception {
        movieManager.updateMovie(movie);
        // refresh the specific movie in the list
        int index = moviesToBeViewed.indexOf(movie);
        if (index >= 0) {
            moviesToBeViewed.set(index, movie);
        }
    }

    public void deleteMovie(Movie movie) throws Exception {
        movieManager.deleteMovie(movie);
        moviesToBeViewed.remove(movie);
    }

    public void refreshMovies() throws Exception {
        moviesToBeViewed.clear();
        moviesToBeViewed.addAll(movieManager.getAllMovies());
    }


    public void updatePersonalRating(Movie movie, float newRating) throws Exception {
        if (movie == null) throw new IllegalArgumentException("movie cannot be null");
        movieManager.updatePersonalRating(movie.getId(), newRating);
        refreshMovies();
    }

}
