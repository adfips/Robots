package saving;

import java.awt.*;
import java.io.*;
import java.util.List;
import java.util.Properties;

/**
 * Класс сохраняющий и загружающий из файла свойства окна
 */
public class WindowSettings {
    /**
     * Файлы, где хранятся свойства окон
     */
    private final File configFile;

    /**
     * Конструктор создающий {@link #configFile путь к файлу}
     */
    public WindowSettings() {
        configFile = new File(
                System.getProperty("user.home") + "/config.txt"
        ).getAbsoluteFile();
    }

    /**
     * Сохраняет все переданные окна
     */
    public void saveProperties(List<Component> windows) {
        Properties properties = new Properties();
        for (Component component : windows)
            if (component instanceof Savable window) {
                window.save(properties);
            }
        try (OutputStream outputStream = new FileOutputStream(configFile, true)) {
            properties.store(outputStream, "Window properties");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Загружает свойства в переданные окна
     */
    public void loadProperties(List<Component> windows) {
        try {
            if (configFile.createNewFile())
                return;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (InputStream inputStream = new FileInputStream(configFile)) {
            Properties properties = new Properties();
            properties.load(inputStream);
            for (Component component : windows)
                if (component instanceof Savable window)
                    window.load(properties);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
