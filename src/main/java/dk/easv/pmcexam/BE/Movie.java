package dk.easv.pmcexam.BE;

public class Movie {
    private int id = -1;
    private String title;
    private float rating = -1;
    private String[] genres;

    public Movie(int id, String title, float rating, String[] genre) {
        setId(id);
        setTitle(title);
        setRating(rating);
        addGenres(genre);
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

    private void addGenres(String[] genre) {
        this.genres = genre;
    }
    public String[] getGenres() {
        return genres;
    }
    public String getGenresAsString() {
        return genres.toString().replace("[","").replace("]","");
    }

}
