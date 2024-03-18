package gui;

import saving.WindowSettings;
import saving.Savable;

import java.awt.*;
import java.beans.PropertyVetoException;
import java.util.Properties;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

public class GameWindow extends JInternalFrame implements Savable {
    private final GameVisualizer m_visualizer;

    public GameWindow() {
        super("Игровое поле", true, true, true, true);
        m_visualizer = new GameVisualizer();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }

    @Override
    public void save() {
        Properties properties = WindowSettings.getProperty();
        String name = getClass().getName();
        properties.setProperty(
                name + "_size", getSize().width + "," + getSize().height);
        properties.setProperty(
                name + "_location", getLocation().x + "," + getLocation().y);
        properties.setProperty(
                name + "_isIcon", String.valueOf(isIcon()));
    }

    @Override
    public void load() {
        Properties properties = WindowSettings.getProperty();
        String sizeStr = properties.getProperty(getClass().getName() + "_size");
        String locationStr = properties.getProperty(getClass().getName() + "_location");
        String icon = properties.getProperty(getClass().getName() + "_isIcon");
        if (sizeStr != null && locationStr != null) {
            Dimension size = parseDimension(sizeStr);
            Point location = parsePoint(locationStr);
            setSize(size);
            setLocation(location);
            try {
                setIcon(Boolean.parseBoolean(icon));
            } catch (PropertyVetoException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Dimension parseDimension(String str) {
        String[] parts = str.split(",");
        int width = Integer.parseInt(parts[0].trim());
        int height = Integer.parseInt(parts[1].trim());
        return new Dimension(width, height);
    }

    private Point parsePoint(String str) {
        String[] parts = str.split(",");
        int x = Integer.parseInt(parts[0].trim());
        int y = Integer.parseInt(parts[1].trim());
        return new Point(x, y);
    }
}
