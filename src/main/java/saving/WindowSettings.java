package saving;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyVetoException;
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
     * Конструктор задающий {@link #configFile путь к файлу}
     */
    public WindowSettings() {
        configFile = new File(
                System.getProperty("user.home") + "/config.txt"
        ).getAbsoluteFile();
    }

    /**
     * Сохраняет все переданные окна
     */
    public void saveProperties(List<? extends Component> windows) {
        try {
            configFile.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException("Не удалось обратиться к файлу: " + configFile, e);
        }
        Properties properties = new Properties();
        for (Component component : windows)
            if (component instanceof Savable window)
                saveWindowBounds(component, properties,window.getFrameId());

        try (OutputStream outputStream = new FileOutputStream(configFile)) {
            properties.store(outputStream, "Window properties");
        } catch (IOException e) {
            throw new RuntimeException("Не удалось обратиться к файлу: " + configFile, e);
        }
    }

    /**
     * Загружает свойства в переданные окна
     */
    public void loadProperties(List<? extends Component> windows) {
        if (!configFile.exists())
            return;
        try (InputStream inputStream = new FileInputStream(configFile)) {
            Properties properties = new Properties();
            properties.load(inputStream);
            for (Component component : windows)
                if (component instanceof Savable window)
                    loadWindowBounds(component, properties,window.getFrameId());

        } catch (IOException e) {
            throw new RuntimeException("Не удалось открыть файл: " + configFile, e);
        }
    }


    /**
     * Сохраняет свойства окна типа Component
     */
    private void saveWindowBounds(Component frame, Properties properties, String name) {
        properties.setProperty(
                name + "_bounds",
                String.format("%d,%d,%d,%d", frame.getBounds().x, frame.getBounds().y, frame.getBounds().width, frame.getBounds().height)
        );
        if (frame instanceof JInternalFrame internalFrame) {
            properties.setProperty(
                    name + "_isIcon", String.valueOf(internalFrame.isIcon()));
        }
    }

    /**
     * Загружает свойства окна типа Component
     */
    private void loadWindowBounds(Component frame, Properties properties, String name) {
        String bounds = properties.getProperty(name + "_bounds");
        String icon = properties.getProperty(name + "_isIcon");
        if (bounds != null) {
            Rectangle rectangle = parseRectangle(bounds);
            frame.setBounds(rectangle);
            if (frame instanceof JInternalFrame internalFrame) {
                try {
                    internalFrame.setIcon(Boolean.parseBoolean(icon));
                } catch (PropertyVetoException e) {
                    throw new RuntimeException("Неприемлемое значение: " + icon, e);
                }
            }
        }
    }

    private Rectangle parseRectangle(String str) {
        String[] parts = str.split(",");
        int x = Integer.parseInt(parts[0]);
        int y = Integer.parseInt(parts[1]);
        int width = Integer.parseInt(parts[2]);
        int height = Integer.parseInt(parts[3]);
        return new Rectangle(x, y, width, height);
    }
}
