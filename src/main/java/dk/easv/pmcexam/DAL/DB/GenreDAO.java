package dk.easv.pmcexam.DAL.DB;

import dk.easv.pmcexam.BE.Genre;
import dk.easv.pmcexam.BE.Movie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class GenreDAO implements IGenreDataAccess {
    private DBConnector databaseConnector;

    public GenreDAO() throws Exception {//the path to database
        databaseConnector = new DBConnector();
    }

    public List<Genre> getAllGenres() throws Exception {
        ArrayList<Genre> allGenres = new ArrayList<>();

        String sql = "SELECT * FROM genres;";
        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {


            ResultSet rs = stmt.executeQuery();

            // Loop through rows from the database result set
            while (rs.next()) {

                //Map DB row to Genre object
                int id = rs.getInt("Id");
                String name = rs.getString("Name");


                Genre genre = new Genre(id, name);
                allGenres.add(genre);
            }
            return allGenres;
        }
    }


    @Override
    public Genre createGenre(Genre newGenre) throws Exception {
        String sql = "INSERT INTO dbo.genres (name) VALUES (?);";

        // try-with-resources makes sure we close db connection etc.
        try (Connection conn = databaseConnector.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, newGenre.getName());


            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            int id = 0;

            if (rs.next()) {
                id = rs.getInt(1);
            }

        } catch (Exception e) {
            throw new Exception("Could not create genre", e);
        }
        return newGenre;
    }


    @Override
    public void updateGenre(Genre genre) throws Exception {
        String sql = "UPDATE dbo.genres SET name = ?,  WHERE id = ?";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            // bind parameters
            stmt.setString(1, genre.getName());
            stmt.setInt(2, genre.getId());

            stmt.executeUpdate();
        } catch (Exception e) {
            throw new Exception("Could not update genre", e);
        }
    }

    @Override
    public void deleteGenre(int genreId) throws Exception {
        String sql = "DELETE FROM genre WHERE id = ?;";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, genreId);

            stmt.executeUpdate();
        } catch (Exception e) {
            throw new Exception("Could not delete playlist", e);
        }
    }

    @Override
    public List<Movie> getMoviesInGenre(int genreId) throws Exception {
        List<Movie> movies = new ArrayList<>();

        String sql = "SELECT movie_filepaths FROM genre WHERE id = ?";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, genreId);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) return movies;

            String json = rs.getString("movie_filepath");
            if (json == null || json.isBlank()) return movies;

            // convert json to list of filepaths (simple parsing to match existing behaviour)
            json = json.replace("[", "").replace("]", "").replace("\"", "");
            String[] filepaths = json.split(",");

            String movieQuery = "SELECT id,title , personalRating, imbdRating, filePath, lastViewed " +
                    "FROM songs WHERE filepath = ?";

            for (String path : filepaths) {
                path = path.trim();
                if (path.isEmpty()) continue;

                try (PreparedStatement sStmt = conn.prepareStatement(movieQuery)) {
                    sStmt.setString(1, path);
                    ResultSet srs = sStmt.executeQuery();

                    if (srs.next()) {
                        Movie m = new Movie(
                                srs.getInt("id"),
                                srs.getString("title"),
                                srs.getFloat("personalRating"),
                                srs.getFloat("imbdRating"),
                                srs.getString("filepath"),
                                srs.getDate("lastViewed").toLocalDate()//check heeeeerrrreee

                        );

                        movies.add(m);
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception("Could not get movies in genre", e);
        }
        return movies;
    }
    @Override
    public void addMovieToGenre(int genreId, int movieId) throws Exception {
        String fetchSong = "SELECT filepath FROM movie WHERE id = ?";
        String filepath;

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(fetchSong)) {

            stmt.setInt(1, movieId);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) return;
            filepath = rs.getString("filepath");
        }


    }

    @Override
    public void deleteMovieFromGenre(int genretId, int movieId) throws Exception {
        String fetchMovie = "SELECT filepath FROM movie WHERE id = ?";
        String filepath;

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(fetchMovie)) {

            stmt.setInt(1, movieId);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) return;
            filepath = rs.getString("filepath");
        }
}
}