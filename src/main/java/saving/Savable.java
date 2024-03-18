package saving;

import java.util.Properties;

/**
  Интерфейс сохранения и загрузки данных
 */
public interface Savable {

    /**
     * Сохраняет состояние окна в {@link  WindowSettings#properties}
     */
    void save();

    /**
     * Загружает состояние окна из {@link Properties}
     */
    void load();
}
