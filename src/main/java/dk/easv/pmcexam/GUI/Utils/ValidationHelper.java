package dk.easv.pmcexam.GUI.Utils;

public class ValidationHelper {

    private ValidationHelper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static boolean isValidRating(String rating) {
        if (isNullOrEmpty(rating)) {
            return false;
        }
        try {
            float value = Float.parseFloat(rating);
            return value >= 0 && value <= 10;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidFloat(String value) {
        if (isNullOrEmpty(value)) {
            return false;
        }
        try {
            Float.parseFloat(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidYear(String year) {
        if (isNullOrEmpty(year)) {
            return false;
        }
        try {
            int value = Integer.parseInt(year);
            return value >= 1800 && value <= 2100;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Validates a file path exists and is accessible
     */
    public static boolean isValidFilePath(String filePath) {
        if (isNullOrEmpty(filePath)) {
            return false;
        }
        java.io.File file = new java.io.File(filePath);
        return file.exists() && file.isFile();
    }
}