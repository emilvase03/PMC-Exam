package dk.easv.pmcexam.DAL;

import dk.easv.pmcexam.BE.GenreMovie;

public interface IGenreMovieDataAccess {

    void createGenreMovie(GenreMovie genreMovie) throws Exception;

    void deleteByMovieId(int movieId) throws Exception;

    void deleteByGenreId(int genreId) throws Exception;
}
