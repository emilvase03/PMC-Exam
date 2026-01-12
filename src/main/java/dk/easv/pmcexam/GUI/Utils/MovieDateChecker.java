package dk.easv.pmcexam.GUI.Utils;

// Project imports
import dk.easv.pmcexam.BE.Movie;

// Java imports
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MovieDateChecker {
    private static List<Movie> oldMovies = new ArrayList<>();


    public static boolean isAnyMoviesOld(List<Movie> movies) {
        if (getMoviesOlderThanTwoYears(movies).isEmpty())
            return false;
        return true;
    }

    private static List<Movie> getMoviesOlderThanTwoYears(List<Movie> movies) {
        LocalDate twoYearsAgo = LocalDate.now().minusYears(2);

        for (Movie movie : movies) {
            if (movie.getLastViewed() != null && movie.getLastViewed().isBefore(twoYearsAgo) && movie.getPersonalRating() < 6.0f) {
                oldMovies.add(movie);
            }
        }
        return oldMovies;
    }

    public static String getOldMoviesAsString() {
        String movies = "";

        for (Movie m : oldMovies) {
                movies += m.getTitle() + "\n";
        }

        return movies;
    }
}
