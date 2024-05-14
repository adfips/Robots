package gui;

import locale.LocalizationListener;
import locale.LocalizationManager;
import log.LogChangeListener;
import log.LogEntry;
import log.LogWindowSource;
import saving.Savable;

import javax.swing.*;
import java.awt.*;

public class LogWindow extends JInternalFrame implements LogChangeListener, Savable, LocalizationListener {
    private final LogWindowSource m_logSource;
    private final TextArea m_logContent;

    public LogWindow(LogWindowSource logSource) {
        super(LocalizationManager.getString("logWindow"), true, true, true, true);
        m_logSource = logSource;
        m_logSource.registerListener(this);
        m_logContent = new TextArea("");
        m_logContent.setSize(200, 500);

        LocalizationManager.addLocalizationListener(this);

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
    public String getFrameId() {
        return "Log";
    }

    @Override
    public void localeChanged() {
        setTitle(LocalizationManager.getString("logWindow"));
        repaint();
    }
}
