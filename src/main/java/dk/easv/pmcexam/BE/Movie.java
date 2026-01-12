package dk.easv.pmcexam.BE;

// Java imports
import java.time.LocalDate;
import java.util.List;

public class Movie {

    private int id = -1;
    private String title;
    private float personalRating = -1;
    private float imdbRating = -1;
    private String filePath;
    private LocalDate lastViewed;

    // derived from GenreMovie
    private List<String> genres;

    public Movie(int id, String title, float personalRating, float imdbRating,
                 String filePath, LocalDate lastViewed) {
        setId(id);
        setTitle(title);
        setPersonalRating(personalRating);
        setImdbRating(imdbRating);
        setFilePath(filePath);
        setLastViewed(lastViewed);
    }

    public Movie(int id, String title, float personalRating, float imdbRating,
                 String filePath) {
        setId(id);
        setTitle(title);
        setPersonalRating(personalRating);
        setImdbRating(imdbRating);
        setFilePath(filePath);
    }

    public Movie(String title, float personalRating, float imdbRating, String filePath) {
        setTitle(title);
        setPersonalRating(personalRating);
        setImdbRating(imdbRating);
        setFilePath(filePath);
    }

    // constructor with genres
    public Movie(String title, float personalRating, float imdbRating, List<String> genres, String filePath) {
        setTitle(title);
        setPersonalRating(personalRating);
        setImdbRating(imdbRating);
        setGenres(genres);
        setFilePath(filePath);
    }

    public void setId(int id) {
        if (id != -1) {
            this.id = id;
        }
    }

    public int getId() {
        return id;
    }

    private void setTitle(String title) {
        if (title != null && !title.isBlank()) {
            this.title = title;
        }
    }

    public String getTitle() {
        return title;
    }

    private void setPersonalRating(float rating) {
        if (rating != -1) {
            this.personalRating = rating;
        }
    }

    public float getPersonalRating() {
        return personalRating;
    }

    private void setImdbRating(float rating) {
        if (rating != -1) {
            this.imdbRating = rating;
        }
    }

    public float getImdbRating() {
        return imdbRating;
    }

    private void setFilePath(String filePath) {
        if (filePath != null) {
            this.filePath = filePath;
        }
    }

    public String getFilePath() {
        return filePath;
    }

    private void setLastViewed(LocalDate lastViewed) {
        if (lastViewed != null) {
            this.lastViewed = lastViewed;
        }
    }

    public LocalDate getLastViewed() {
        return lastViewed;
    }

    // genres (derived)

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public String getGenresAsString() {
        if (genres == null || genres.isEmpty()) {
            return "";
        }
        return String.join(", ", genres);
    }
}