module dk.easv.pmcexam {
    requires javafx.controls;
    requires javafx.fxml;


    opens dk.easv.pmcexam to javafx.fxml;
    exports dk.easv.pmcexam;
}