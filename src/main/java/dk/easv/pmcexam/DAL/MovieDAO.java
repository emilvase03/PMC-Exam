package dk.easv.pmcexam.DAL;

// Project imports
import dk.easv.pmcexam.BE.Movie;
import dk.easv.pmcexam.DAL.DB.DBConnector;

// Java imports
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MovieDAO implements IMovieDataAccess
{
    private DBConnector databaseConnector;

    public MovieDAO() throws Exception {//introduces the path to database,where we get connection from
        databaseConnector = new DBConnector();
    }

    public List<Movie> getAllMovies() throws Exception {
        ArrayList<Movie> allMovies = new ArrayList<>();

        String sql = "SELECT * FROM moives;";
        try (Connection conn = databaseConnector.getConnection();//try with resources.The connection should be closed after so it is in () with try.
             PreparedStatement stmt = conn.prepareStatement(sql)) {


            ResultSet rs = stmt.executeQuery();

            // Loop through rows from the database result set
            while (rs.next()) {

                //Map DB row to Movie object
                int id = rs.getInt("Id");
                String title = rs.getString("Title");
                float rating = rs.getFloat("Rating");
                String filePath = rs.getString("FilePath");
                LocalDate date = rs.getDate("LastViewed").toLocalDate();

                Movie moive = new Movie(id, title, rating, filePath, date);
                allMovies.add(moive);
            }
            return allMovies;
        }
    }
    @Override
    public Movie createMovie(Movie newMovie) throws Exception {
        String sql = "INSERT INTO dbo.movies (title, rating) VALUES (?,?,?);";

        // try-with-resources makes sure we close db connection etc.
        try (Connection conn = databaseConnector.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // bind parameters
            stmt.setString   (1, newMovie.getTitle());
            stmt.setFloat(2, newMovie.getRating());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            int id = 0;

            if (rs.next()) {
                id = rs.getInt(1);
            }

            Movie createdMovie = new Movie(id, newMovie.getTitle(), newMovie.getRating(), newMovie.getFilePath(), newMovie.getLastViewed());
            return createdMovie;
        }
    }


    @Override
    public void updateMovie(Movie movie) throws Exception {
        String sql = "UPDATE dbo.songs SET title = ?, rating = ?, WHERE id = ?";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            // bind parameters
            stmt.setString   (1, movie.getTitle());
            stmt.setFloat(2, movie.getRating());
            stmt.setInt(3, movie.getId());

            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteSong(Movie movie) throws Exception {
        String sql = "DELETE FROM movies WHERE id = ?;";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, movie.getId());

            stmt.executeUpdate();
        }
    }
}
