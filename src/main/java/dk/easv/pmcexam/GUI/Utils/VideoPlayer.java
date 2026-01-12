package dk.easv.pmcexam.GUI.Utils;

import javafx.application.HostServices;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

public class VideoPlayer {

    public static void playVideo(HostServices hostServices, String filePath) {
        try {
            File videoFile;

            // check if the path is a resource (starts with "videos/" or something)
            URL resourceUrl = VideoPlayer.class.getResource("/" + filePath);
            if (resourceUrl != null) {
                // resource inside JAR or classpath
                videoFile = new File(resourceUrl.toURI());  // convert URL to File
            } else {
                // regular file on disk
                videoFile = new File(filePath);
            }

            if (videoFile.exists()) {
                hostServices.showDocument(videoFile.getAbsolutePath());
            } else {
                AlertHelper.showError("Error", "Unable to read movie file: " + filePath);
            }
        } catch (URISyntaxException e) {
            AlertHelper.showError("Error", "Invalid movie path: " + filePath);
        }
    }
}
