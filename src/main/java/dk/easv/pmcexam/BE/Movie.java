package dk.easv.pmcexam.BE;

// Java imports
import java.time.LocalDate;

public class Movie {
    private int id = -1;
    private String title;
    private float personalRating = -1;
    private float imdbRating = -1;
    //private String[] genres; // <---- HEY! look at this :)
    private String filePath;
    private LocalDate lastViewed;


    public Movie(int id, String title, float personalRating, float imdbRating, String filePath, LocalDate lastViewed) {
        setId(id);
        setTitle(title);
        setPersonalRating(personalRating);

        setFilePath(filePath);
        setLastViewed(lastViewed);
    }

    public Movie(int id, String title, float personalRating, float imdbRating, String filePath) {
        setId(id);
        setTitle(title);
        setPersonalRating(personalRating);
        setImdbRating(imdbRating);

        setFilePath(filePath);
    }

    // For creating new Movie object without ID
    public Movie(String title, float personalRating, float imdbRating, String genre, String filePath) {
        setTitle(title);
        setPersonalRating(personalRating);
        setImdbRating(imdbRating);

        setFilePath(filePath);
    }

    private void setId(int id) {
        if (id != -1)
            this.id = id;
    }
    public int getId() {
        return id;
    }

    private void setTitle(String title) {
        if (title != null && !title.isBlank())
            this.title = title;
    }
    public String getTitle() {
        return title;
    }

    private void setPersonalRating(float rating) {
        if (rating != -1)
            this.personalRating = rating;
    }
    public float getPersonalRating() {
        return personalRating;
    }

    private void setImdbRating(float rating) {
        if (rating != -1)
            this.imdbRating = rating;
    }
    public float getImdbRating() {
        return imdbRating;
    }

    private void setFilePath(String filePath) {
        if(filePath != null)
            this.filePath = filePath;
    }
    public String getFilePath() {
        return filePath;
    }

    private void setLastViewed(LocalDate lastViewed) {
        if (lastViewed != null)
            this.lastViewed = lastViewed;
    }
    public LocalDate getLastViewed() {
        return lastViewed;
    }


//    private void addGenres(String[] genre) {
//        this.genres = genre;
//    }
//    public String[] getGenres() {
//        return genres;
//    }
//    public String getGenresAsString() {
//        return genres.toString().replace("[","").replace("]","");
//    }

}
