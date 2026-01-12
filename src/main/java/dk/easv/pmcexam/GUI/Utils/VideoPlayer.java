package dk.easv.pmcexam.GUI.Utils;

// Java imports
import javafx.application.HostServices;
import java.io.File;

public class VideoPlayer {

    public static void playVideo(HostServices hostServices, String filePath) {
        File videoFile = new File(filePath);
        if (videoFile.exists()) {
            hostServices.showDocument(videoFile.getAbsolutePath());
        } else {
            AlertHelper.showError("Error", "Unable to read movie file.");
        }
    }
}
