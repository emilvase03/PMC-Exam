package dk.easv.pmcexam.DAL.DB;

import dk.easv.pmcexam.BE.Genre;


import java.util.List;

public interface IGenreDataAccess {
    public List<Genre> getAllGenres() throws Exception;

    public Genre createGenre(Genre newGenre) throws Exception;

    public void updateGenre(Genre genre) throws Exception;

    public void deleteGenre(Genre genre) throws Exception;
}


