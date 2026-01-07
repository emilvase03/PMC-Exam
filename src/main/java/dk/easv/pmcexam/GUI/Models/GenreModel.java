package dk.easv.pmcexam.GUI.Models;

// Project imports
import dk.easv.pmcexam.BE.Genre;
import dk.easv.pmcexam.BLL.GenreManager;

// Java imports
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class GenreModel {
    private static GenreModel instance;
    private GenreManager genreManager = new GenreManager();
    private ObservableList<Genre> genresToBeViewed;

    private GenreModel() throws Exception {
        genresToBeViewed = FXCollections.observableArrayList();
        genresToBeViewed.addAll(genreManager.getAllGenres());
    }

    public static GenreModel getInstance() throws Exception {
        if (instance == null) {
            instance = new GenreModel();
        }
        return instance;
    }

    public ObservableList<Genre> getObservableGenres() throws Exception {
        return genresToBeViewed;
    }

    public Genre createGenre(Genre newGenre) throws Exception {
        Genre createdGenre = genreManager.createGenre(newGenre);
        genresToBeViewed.add(createdGenre);
        return createdGenre;
    }

    public void updateGenre(Genre genre) throws Exception {
        genreManager.updateGenre(genre);
    }

    public void deleteGenre(Genre genre) throws Exception {
        genreManager.deleteGenre(genre);
        genresToBeViewed.remove(genre);
    }

    public void refreshGenres() throws Exception {
        genresToBeViewed.clear();
        genresToBeViewed.addAll(genreManager.getAllGenres());
    }
}