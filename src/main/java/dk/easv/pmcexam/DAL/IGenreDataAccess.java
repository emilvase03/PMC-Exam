package dk.easv.pmcexam.DAL;

// Project imports
import dk.easv.pmcexam.BE.Genre;

// Java imports
import java.util.List;

public interface IGenreDataAccess {
    public List<Genre> getAllGenres() throws Exception;

    public List<String> getAllGenreNames() throws Exception;

    public Genre createGenre(Genre newGenre) throws Exception;

    public void updateGenre(Genre genre) throws Exception;

    public void deleteGenre(Genre genre) throws Exception;
}


