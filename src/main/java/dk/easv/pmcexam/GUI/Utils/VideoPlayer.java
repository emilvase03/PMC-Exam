package dk.easv.pmcexam.GUI.Utils;

// Java imports
import javafx.application.HostServices;
import java.io.File;

public class VideoPlayer {
    private HostServices hostServices;

    public void playVideo(String filePath) {
        File videoFile = new File(filePath);
        if (videoFile.exists()) {
            hostServices.showDocument(videoFile.getAbsolutePath());
        }
    }
}
