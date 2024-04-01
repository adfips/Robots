package saving;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyVetoException;
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


    /**
     * Сохраняет свойства окна типа Component
     */
    default void saveWindowBounds(Component frame, Properties properties, String name) {
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
    default void loadWindowBounds(Component frame, Properties properties, String name) {
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

