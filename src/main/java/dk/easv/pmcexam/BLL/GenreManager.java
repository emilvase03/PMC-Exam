package dk.easv.pmcexam.BLL;

import dk.easv.pmcexam.BE.Genre;
import dk.easv.pmcexam.BE.Movie;
import dk.easv.pmcexam.DAL.DB.GenreDAO;
import dk.easv.pmcexam.DAL.DB.IGenreDataAccess;

import java.io.IOException;
import java.util.List;

public class GenreManager {
    private final IGenreDataAccess genreDAO;

    public GenreManager() throws Exception {
        genreDAO = new GenreDAO();
    }

    public List<Genre> getAllGenres() throws Exception {
        return genreDAO.getAllGenres();
    }

    public Genre createGenre(Genre genre) throws Exception {
        return genreDAO.createGenre(genre);
    }

    public void updateGenre(Genre genre) throws Exception {
      genreDAO.updateGenre(genre);
    }

    public void deleteGenre(int genreId) throws Exception {
        genreDAO.deleteGenre(genreId);
    }

    public List<Movie> getMoviesInGenre(int genreId) throws Exception {
        return genreDAO.getMoviesInGenre(genreId);
    }

    public void addMovieToGenre(int genreId, int movieId) throws Exception {
        genreDAO.addMovieToGenre( genreId,movieId);
    }

    public void deleteMovieFromGenre(int genreId, int movieId) throws Exception {
        genreDAO.deleteMovieFromGenre(genreId, movieId);
    }
    }

