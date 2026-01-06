package dk.easv.pmcexam.DAL.DB;

import dk.easv.pmcexam.BE.Genre;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class GenreDAO implements IGenreDataAccess
{
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

            // bind parameters
            stmt.setString   (1, newGenre.getName());


            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            int id = 0;

            if (rs.next()) {
                id = rs.getInt(1);
            }

           Genre createdGenre = new Genre(id, newGenre.getName());
            return createdGenre;
        }
    }


    @Override
    public void updateGenre(Genre genre) throws Exception {
        String sql = "UPDATE dbo.genres SET name = ?,  WHERE id = ?";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            // bind parameters
            stmt.setString   (1, genre.getName());
            stmt.setInt(2, genre.getId());

            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteGenre(Genre genre) throws Exception {
        String sql = "DELETE FROM movies WHERE id = ?;";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, genre.getId());

            stmt.executeUpdate();
        }

    }
}