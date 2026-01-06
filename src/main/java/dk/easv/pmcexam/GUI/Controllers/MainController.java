package dk.easv.pmcexam.GUI.Controllers;

// Project imports
import dk.easv.pmcexam.BE.Genre;
import dk.easv.pmcexam.BE.Movie;
import dk.easv.pmcexam.GUI.Models.MainModel;

// Java imports
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    private MainModel mainModel;

    @FXML
    private TableView<Movie> movieList;
    @FXML
    private ListView<Genre> genreList;
    @FXML
    private Button addMovie;
    @FXML
    private Button addGenre;
    @FXML
    private MenuItem updateRating;
    @FXML
    private MenuItem deleteMovie;
    @FXML
    private TextField searchMovie;
    @FXML
    private Button deleteGenre;
    @FXML
    private TableColumn colPersonalRating;
    @FXML
    private TableColumn colIMDBRating;
    @FXML
    private TableColumn colTitle;
    @FXML
    private TableColumn colGenre;

    public MainController() {
        try {
            mainModel = MainModel.getInstance();
        } catch (Exception e) {
            // Display error to user
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            setupTables();
        } catch (Exception e) {
            // Display error to user
            throw new RuntimeException(e);
        }
    }

    private void setupTables() throws Exception {
        colPersonalRating.setCellValueFactory(new PropertyValueFactory<>("personalRating"));
        colIMDBRating.setCellValueFactory(new PropertyValueFactory<>("imdbRating"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colGenre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        movieList.setItems(mainModel.getObservableMovies());
    }

    @FXML
    private void onBtnClickAddMovie(ActionEvent actionEvent) {
    }

    @FXML
    private void onBtnClickAddGenre(ActionEvent actionEvent) {
    }

    @FXML
    private void onBtnClickUpdateRating(ActionEvent actionEvent) {
    }

    @FXML
    private void onBtnClickDeleteMovie(ActionEvent actionEvent) {
    }

    @FXML
    private void onBtnClickDeleteGenre(ActionEvent actionEvent) {
    }
}
