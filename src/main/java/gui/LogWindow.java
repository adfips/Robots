package gui;

import log.LogChangeListener;
import log.LogEntry;
import log.LogWindowSource;
import saving.Savable;
import saving.WindowSettings;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyVetoException;
import java.util.Properties;

public class LogWindow extends JInternalFrame implements LogChangeListener, Savable
{
    private final LogWindowSource m_logSource;
    private final TextArea m_logContent;

    public LogWindow(LogWindowSource logSource)
    {
        super("Протокол работы", true, true, true, true);
        m_logSource = logSource;
        m_logSource.registerListener(this);
        m_logContent = new TextArea("");
        m_logContent.setSize(200, 500);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_logContent, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
        updateLogContent();
    }

    private void updateLogContent()
    {
        StringBuilder content = new StringBuilder();
        for (LogEntry entry : m_logSource.all())
        {
            content.append(entry.getMessage()).append("\n");
        }
        m_logContent.setText(content.toString());
        m_logContent.invalidate();
    }

    @Override
    public void onLogChanged()
    {
        EventQueue.invokeLater(this::updateLogContent);
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
