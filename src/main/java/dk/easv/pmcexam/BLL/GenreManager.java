package dk.easv.pmcexam.BLL;

// Project imports
import dk.easv.pmcexam.BE.Genre;
import dk.easv.pmcexam.DAL.GenreDAO;
import dk.easv.pmcexam.DAL.IGenreDataAccess;

// Java imports
import java.util.List;

public class GenreManager {

    private IGenreDataAccess IGenreDataAccess;

    public GenreManager() throws Exception {
        IGenreDataAccess = new GenreDAO();
    }

    public List<Genre> getAllGenres() throws Exception {
        return IGenreDataAccess.getAllGenres();
    }

    public Genre createGenre(Genre newGenre) throws Exception {
        return IGenreDataAccess.createGenre(newGenre);
    }

    public void updateGenre(Genre genre) throws Exception {
        IGenreDataAccess.updateGenre(genre);
    }

    public void deleteGenre(Genre genre) throws Exception {
        IGenreDataAccess.deleteGenre(genre);
    }

}
