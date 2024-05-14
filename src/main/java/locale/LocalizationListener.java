package locale;

/**
 * Контракт на который подписываются классы поддерживающие разные локали
 */
public interface LocalizationListener {
    /**
     * Метод изменения локали
     */
    void localeChanged();
}
