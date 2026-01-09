package dk.easv.pmcexam.BLL;

// Project imports
import dk.easv.pmcexam.BE.Genre;
import dk.easv.pmcexam.DAL.GenreDAO;
import dk.easv.pmcexam.DAL.IGenreDataAccess;

// Java imports
import java.util.List;

public class GenreManager {

    private IGenreDataAccess iGenreDataAccess;

    public GenreManager() throws Exception {
        iGenreDataAccess = new GenreDAO();
    }

    public List<Genre> getAllGenres() throws Exception {
        return iGenreDataAccess.getAllGenres();
    }

    public List<String> getAllGenreNames() throws Exception {
        return iGenreDataAccess.getAllGenreNames();
    }

    public Genre createGenre(Genre newGenre) throws Exception {
        return iGenreDataAccess.createGenre(newGenre);
    }

    public void updateGenre(Genre genre) throws Exception {
        iGenreDataAccess.updateGenre(genre);
    }

    public void deleteGenre(Genre genre) throws Exception {
        iGenreDataAccess.deleteGenre(genre);
    }

}
