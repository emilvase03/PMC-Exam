package dk.easv.pmcexam.DAL.DB;

import dk.easv.pmcexam.BE.Genre;
import dk.easv.pmcexam.BE.Movie;


import java.util.List;

public interface IGenreDataAccess {
    public List<Genre> getAllGenres() throws Exception;

    public Genre createGenre(Genre newGenre) throws Exception;

    public void updateGenre(Genre genre) throws Exception;

    public void deleteGenre(int genreId) throws Exception;

    List<Movie> getMoviesInGenre(int playlistId) throws Exception;

    void addMovieToGenre(int genreId, int movieId) throws Exception;

    void deleteMovieFromGenre(int genreId, int movieId) throws Exception;
}


