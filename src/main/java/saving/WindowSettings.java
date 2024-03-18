package saving;

import java.awt.*;
import java.io.*;
import java.util.Properties;

/**
 * Класс сохраняющий и загружающий из файла свойства окна
 */
public class WindowSettings {
    /**
     * Структура свойств окон
     */
    private static Properties properties;
    /**
     * Файл где хранятся {@link #properties свойства}
     */
    private final File configFile;

    /**
     * Конструктор добавляющий {@link #configFile путь к файлу}
     */
    public WindowSettings() {
        configFile = new File(
                System.getProperty("user.home") + "/config.txt"
        ).getAbsoluteFile();
        properties = new Properties();
    }

    /**
     * Сохраняет все переданные окна
     */
    public void saveProperties(Component[] windows) {
        for (Component component : windows)
            if (component instanceof Savable window)
                window.save();

        try (OutputStream outputStream = new FileOutputStream(configFile, true)) {
            properties.store(outputStream, "Window properties");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
         Загружает свойства в переданные окна
     */
    public void loadProperties(Component[] windows) {
        try {
            if (configFile.createNewFile())
                return;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (InputStream inputStream = new FileInputStream(configFile)) {
            properties.load(inputStream);
            for (Component component : windows)
                if (component instanceof Savable window)
                    window.load();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
        Получение свойств окон
     */
    public static Properties getProperty() {return properties;}

}
