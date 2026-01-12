package dk.easv.pmcexam.BLL;

// Project imports
import dk.easv.pmcexam.BE.Movie;
import dk.easv.pmcexam.DAL.IMovieDataAccess;
import dk.easv.pmcexam.DAL.MovieDAO;

// Java imports
import java.util.List;

public class MovieManager {

    private IMovieDataAccess iMovieDataAccess;

    public MovieManager() throws Exception {
         iMovieDataAccess = new MovieDAO();
    }

    public List<Movie> getAllMovies() throws Exception {
        return iMovieDataAccess.getAllMovies();
    }

    public Movie createMovie(Movie newMovie) throws Exception {
        return iMovieDataAccess.createMovie(newMovie);
    }

    public void updateMovie(Movie movie) throws Exception {
        iMovieDataAccess.updateMovie(movie);
    }

    public void deleteMovie(Movie movie) throws Exception {
        iMovieDataAccess.deleteMovie(movie);
    }

    public void updatePersonalRating(int movieId, float rating) throws Exception {
        iMovieDataAccess.updatePersonalRating(movieId, rating);
    }


}
