package dk.easv.pmcexam.BE;

import java.time.LocalDate;
import java.util.Date;

public class Movie {
    private int id = -1;
    private String title;
    private float rating = -1;
    //private String[] genres; // <---- HEY! look at this :)
    private String filePath;
    private LocalDate lastViewed;


    public Movie(int id, String title, float rating, String filePath, LocalDate lastViewed) {
        setId(id);
        setTitle(title);
        setRating(rating);
        setFilePath(filePath);
        setLastViewed(lastViewed);
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

    private void setRating(float rating) {
        if (rating != -1)
            this.rating = rating;
    }
    public float getRating() {
        return rating;
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
