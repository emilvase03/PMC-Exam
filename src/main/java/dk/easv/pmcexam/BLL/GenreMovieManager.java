package dk.easv.pmcexam.BLL;

// Project imports
import dk.easv.pmcexam.BE.GenreMovie;
import dk.easv.pmcexam.DAL.IGenreMovieDataAccess;

import java.util.List;

public class GenreMovieManager {

    private final IGenreMovieDataAccess genreMovieDAO;

    public GenreMovieManager(IGenreMovieDataAccess genreMovieDAO) {
        this.genreMovieDAO = genreMovieDAO;
    }

    public void setGenresForMovie(int movieId, List<Integer> genreIds) throws Exception {

        // remove old relations -- not sure if we should do this
        //genreMovieDAO.deleteByMovieId(movieId);

        // add new relations
        for (int genreId : genreIds) {
            genreMovieDAO.createGenreMovie(new GenreMovie(genreId, movieId));
        }
    }
}
