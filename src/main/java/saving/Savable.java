package saving;

import java.util.Properties;

/**
 * Интерфейс сохранения и загрузки данных
 */
public interface Savable {

    /**
     * Сохраняет состояние окна
     */
    void save(Properties properties);

    /**
     * Загружает состояние окна
     */
    void load(Properties properties);
}
