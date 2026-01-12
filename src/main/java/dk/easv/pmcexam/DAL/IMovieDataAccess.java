package dk.easv.pmcexam.DAL;

// Project imports
import dk.easv.pmcexam.BE.Movie;

// Java imports
import java.util.List;

public interface IMovieDataAccess {

    public List<Movie> getAllMovies() throws Exception;

    public Movie createMovie(Movie newMovie) throws Exception;

    public void updateMovie(Movie movie) throws Exception;

    public void deleteMovie(Movie movie) throws Exception;

   public void updatePersonalRating(int movieId, float rating) throws Exception;
}
