package gui;

import saving.Savable;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyVetoException;
import java.util.Properties;

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
    public void save(Properties properties) {
        String name = getClass().getName();
        properties.setProperty(
                name + "_bounds",
                String.format("%d,%d,%d,%d",getBounds().x,getBounds().y,getBounds().width,getBounds().height)
        );
        properties.setProperty(
                name + "_isIcon", String.valueOf(isIcon()));
    }

    @Override
    public void load(Properties properties) {
        String name = getClass().getName();
        String bounds = properties.getProperty(name + "_bounds");
        String icon = properties.getProperty(name + "_isIcon");
        if (bounds != null) {
            Rectangle rectangle = parseRectangle(bounds);
            setBounds(rectangle);
            try {
                setIcon(Boolean.parseBoolean(icon));
            } catch (PropertyVetoException e) {
                throw new RuntimeException(e);
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
