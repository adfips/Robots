package gui;

import saving.Savable;

import javax.swing.*;
import java.awt.*;
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
    public String getFrameId() {
        return "Game";
    }
}
