package locale;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class LocalizationManager {
    /**
     *
     */
    private static ResourceBundle language;
    /**
     * Слушатели подписанные на изменение локализации
     */
    private static final List<LocalizationListener> listeners = new ArrayList<>();

    /**
     * Установка языка
     */
    public static void setLocale(Locale locale) {
        language = ResourceBundle.getBundle("messages", locale);
        notifyListeners();
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

    /**
     * Добавление слушателя
     */
    public static void addLocalizationListener(LocalizationListener listener) {
        listeners.add(listener);
    }

    /**
     * Удаление слушателя
     */
    public static void removeLocalizationListener(LocalizationListener listener) {
        listeners.remove(listener);
    }

    /**
     * Оповещение всех слушателей о смене локали
     */
    private static void notifyListeners() {
        for (LocalizationListener listener : listeners) {
            listener.localeChanged();
        }
    }

}
