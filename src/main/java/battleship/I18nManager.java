package battleship;

import java.util.Locale;
import java.util.ResourceBundle;

public class I18nManager {

    private static Locale currentLocale = new Locale("pt");
    private static ResourceBundle bundle = ResourceBundle.getBundle("messages", currentLocale);

    public static void setLanguage(String language) {
        currentLocale = new Locale(language);
        bundle = ResourceBundle.getBundle("messages", currentLocale);
    }

    public static String getMessage(String key) {
        return bundle.getString(key);
    }

    public static Locale getCurrentLocale() {
        return currentLocale;
    }
}