package dk.easv.pmcexam.DAL;

// Project imports
import dk.easv.pmcexam.BE.Movie;
import dk.easv.pmcexam.DAL.DB.DBConnector;

// Java imports
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieDAO implements IMovieDataAccess
{
    private DBConnector databaseConnector;

    public MovieDAO() throws Exception {
        databaseConnector = new DBConnector();
    }

    public List<Movie> getAllMovies() throws Exception {
        ArrayList<Movie> allMovies = new ArrayList<>();
        String sql = "SELECT * FROM movies;";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                int id = rs.getInt("Id");
                String title = rs.getString("Title");
                float personalRating = rs.getFloat("PersonalRating");
                float imdbRating = rs.getFloat("IMDBRating");
                String filePath = rs.getString("FilePath");
                Date date = rs.getDate("LastViewed");

                Movie movie;
                if (date != null) {
                    movie = new Movie(id, title, personalRating, imdbRating, filePath, date.toLocalDate());
                } else {
                    movie = new Movie(id, title, personalRating, imdbRating, filePath);
                }

                List<String> genres = getGenres(movie);
                movie.setGenres(genres);

                allMovies.add(movie);
            }

            return allMovies;
        }
    }

    @Override
    public Movie createMovie(Movie newMovie) throws Exception {
        String sql = "INSERT INTO movies (Title, PersonalRating, FilePath, IMDBRating, Genre) VALUES (?,?,?,?,?);";

        // try-with-resources makes sure we close db connection etc.
        try (Connection conn = databaseConnector.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // bind parameters
            stmt.setString   (1, newMovie.getTitle());
            stmt.setFloat(2, newMovie.getPersonalRating());
            stmt.setString(3, newMovie.getFilePath());
            stmt.setFloat(4, newMovie.getImdbRating());
            stmt.setString(5, newMovie.getGenresAsString());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            int id = 0;

            if (rs.next()) {
                id = rs.getInt(1);
            }

            Movie createdMovie = new Movie(id, newMovie.getTitle(), newMovie.getPersonalRating(), newMovie.getImdbRating(), newMovie.getFilePath());
            return createdMovie;
        }
    }


    @Override
    public void updateMovie(Movie movie) throws Exception {
        String sql = "UPDATE moives SET title = ?, PersonalRating = ?, IMDBRating, WHERE id = ?";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            // bind parameters
            stmt.setString   (1, movie.getTitle());
            stmt.setFloat(2, movie.getPersonalRating());
            stmt.setFloat(3, movie.getImdbRating());
            stmt.setInt(4, movie.getId());

            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteMovie(Movie movie) throws Exception {
        String sql = "DELETE FROM movies WHERE id = ?;";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, movie.getId());

            stmt.executeUpdate();
        }
    }

    private List<String> getGenres(Movie movie) throws Exception {
        List<String> genreNames = new ArrayList<>();

        // Join query - get all genre names in one go
        String sql = "SELECT g.name " +
                "FROM genres g " +
                "INNER JOIN genremovie gm ON g.id = gm.genreid " +
                "WHERE gm.movieid = ?";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, movie.getId());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                genreNames.add(rs.getString("name"));
            }
        }
        System.out.println(genreNames);
        return genreNames;
    }


    public void updatePersonalRating(int movieId, float rating) throws SQLException {

        String sql = "UPDATE movies SET PersonalRating = ? WHERE id = ?";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setFloat(1, rating);
            ps.setInt(2, movieId);

            int updated = ps.executeUpdate();
            if (updated == 0) {
                throw new SQLException("No movie found with id=" + movieId);
            }
        }
    }

}



