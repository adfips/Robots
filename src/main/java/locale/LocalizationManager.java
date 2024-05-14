package locale;

import java.util.Locale;
import java.util.ResourceBundle;

public class LocalizationManager {
    /**
     * Язык
     */
    private static ResourceBundle language;

    /**
     * Установка языка
     */
    public static void setLocale(Locale locale) {
        language = ResourceBundle.getBundle("messages", locale);
    }

    public static String getLocale(){
        return language.getLocale().getLanguage();
    }

    /**
     * Получение локализованной строки по ключу
     */
    public static String getString(String key) {
        if (language == null)
            setLocale(Locale.getDefault());
        return language.getString(key);
    }

}
