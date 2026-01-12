package dk.easv.pmcexam.DAL;

// Project imports
import dk.easv.pmcexam.BE.GenreMovie;
import dk.easv.pmcexam.DAL.DB.DBConnector;

// Java imports
import java.sql.Connection;
import java.sql.PreparedStatement;

public class GenreMovieDAO implements IGenreMovieDataAccess {

    private DBConnector databaseConnector;

    public GenreMovieDAO() throws Exception {
        databaseConnector = new DBConnector();
    }

    @Override
    public void createGenreMovie(GenreMovie genreMovie) throws Exception {
        String sql = "INSERT INTO genremovie (GenreId, MovieId) VALUES (?, ?);";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, genreMovie.getGenreId());
            stmt.setInt(2, genreMovie.getMovieId());

            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteByMovieId(int movieId) throws Exception {
        String sql = "DELETE FROM genremovie WHERE MovieId = ?;";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, movieId);
            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteByGenreId(int genreId) throws Exception {
        String sql = "DELETE FROM genremovie WHERE GenreId = ?;";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, genreId);
            stmt.executeUpdate();
        }
    }
}
