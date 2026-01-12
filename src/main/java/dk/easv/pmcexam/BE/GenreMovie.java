package dk.easv.pmcexam.BE;

public class GenreMovie {

    private final int genreId;
    private final int movieId;

    public GenreMovie(int genreId, int movieId) {
        this.genreId = genreId;
        this.movieId = movieId;
    }

    public int getGenreId() {
        return genreId;
    }

    public int getMovieId() {
        return movieId;
    }
}
