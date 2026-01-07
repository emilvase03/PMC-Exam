package dk.easv.pmcexam.GUI.Utils;

public class ValidationHelper {

    private ValidationHelper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static boolean isValidRating(String rating) {
        try {
            double value = Double.parseDouble(rating);
            return value >= 0 && value <= 10;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}