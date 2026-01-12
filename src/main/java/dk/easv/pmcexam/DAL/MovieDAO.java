package dk.easv.pmcexam.DAL;

// Project imports
import dk.easv.pmcexam.BE.GenreMovie;
import dk.easv.pmcexam.BE.Movie;
import dk.easv.pmcexam.DAL.DB.DBConnector;

// Java imports
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieDAO implements IMovieDataAccess
{
    private DBConnector databaseConnector;
    private GenreMovieDAO genreMovieDAO;

    public MovieDAO() throws Exception {
        databaseConnector = new DBConnector();
        genreMovieDAO = new GenreMovieDAO();
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

                // load genres for this movie from GenreMovie junction table
                List<String> genres = getGenres(movie);
                movie.setGenres(genres);

                allMovies.add(movie);
            }

            return allMovies;
        }
    }

    @Override
    public Movie createMovie(Movie newMovie) throws Exception {
        String sql = "INSERT INTO movies (Title, PersonalRating, FilePath, IMDBRating) VALUES (?,?,?,?);";

        try (Connection conn = databaseConnector.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // bind parameters
            stmt.setString(1, newMovie.getTitle());
            stmt.setFloat(2, newMovie.getPersonalRating());
            stmt.setString(3, newMovie.getFilePath());
            stmt.setFloat(4, newMovie.getImdbRating());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            int movieId = 0;

            if (rs.next()) {
                movieId = rs.getInt(1);
            }

            // set the ID on the movie object
            newMovie.setId(movieId);

            // insert genres into GenreMovie junction table
            if (newMovie.getGenres() != null && !newMovie.getGenres().isEmpty()) {
                insertGenresForMovie(movieId, newMovie.getGenres());
            }

            Movie createdMovie = new Movie(movieId, newMovie.getTitle(), newMovie.getPersonalRating(),
                    newMovie.getImdbRating(), newMovie.getFilePath());
            createdMovie.setGenres(newMovie.getGenres());

            return createdMovie;
        }
    }

    // insert genre associations for a movie into the GenreMovie junction table
    private void insertGenresForMovie(int movieId, List<String> genreNames) throws Exception {
        String sqlGetGenreId = "SELECT id FROM genres WHERE name = ?";

        for (String genreName : genreNames) {
            try (Connection conn = databaseConnector.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sqlGetGenreId)) {

                stmt.setString(1, genreName);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    int genreId = rs.getInt("id");

                    // create GenreMovie association
                    GenreMovie genreMovie = new GenreMovie(genreId, movieId);
                    genreMovieDAO.createGenreMovie(genreMovie);
                }
            }
        }
    }

    @Override
    public void updateMovie(Movie movie) throws Exception {
        String sql = "UPDATE movies SET title = ?, PersonalRating = ?, IMDBRating = ?, LastViewed = ? WHERE id = ?";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            // bind parameters
            stmt.setString(1, movie.getTitle());
            stmt.setFloat(2, movie.getPersonalRating());
            stmt.setFloat(3, movie.getImdbRating());
            stmt.setDate(4, Date.valueOf(movie.getLastViewed()));
            stmt.setInt(5, movie.getId());

            stmt.executeUpdate();

            // update genres - delete old associations and insert new ones
            if (movie.getGenres() != null) {
                genreMovieDAO.deleteByMovieId(movie.getId());
                insertGenresForMovie(movie.getId(), movie.getGenres());
            }
        }
    }

    @Override
    public void deleteMovie(Movie movie) throws Exception {
        // first delete genre associations from GenreMovie
        genreMovieDAO.deleteByMovieId(movie.getId());

        // then delete the movie
        String sql = "DELETE FROM movies WHERE id = ?;";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, movie.getId());

            stmt.executeUpdate();
        }
    }

    // get all genre names for a specific movie using JOIN query on GenreMovie
    private List<String> getGenres(Movie movie) throws Exception {
        List<String> genreNames = new ArrayList<>();

        // join query - get all genre names from GenreMovie junction table
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



