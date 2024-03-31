package gui;

import log.LogChangeListener;
import log.LogEntry;
import log.LogWindowSource;
import saving.Savable;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyVetoException;
import java.util.Properties;

public class LogWindow extends JInternalFrame implements LogChangeListener, Savable {
    private final LogWindowSource m_logSource;
    private final TextArea m_logContent;

    public LogWindow(LogWindowSource logSource) {
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

    private void updateLogContent() {
        StringBuilder content = new StringBuilder();
        for (LogEntry entry : m_logSource.all()) {
            content.append(entry.getMessage()).append("\n");
        }
        m_logContent.setText(content.toString());
        m_logContent.invalidate();
    }

    @Override
    public void onLogChanged() {
        EventQueue.invokeLater(this::updateLogContent);
    }

    @Override
    public void save(Properties properties) {
        String name = "LogWindow";
        properties.setProperty(
                name + "_bounds",
                String.format("%d,%d,%d,%d", getBounds().x, getBounds().y, getBounds().width, getBounds().height)
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
                throw new RuntimeException("Неприемлемое значение: " + icon, e);
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
