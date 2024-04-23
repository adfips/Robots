package gui;

import controller.Controller;
import model.Robot;
import saving.Savable;

import javax.swing.*;
import java.awt.*;

public class GameWindow extends JInternalFrame implements Savable {
    private final GameVisualizer m_visualizer;

    public GameWindow(Controller controller, Robot robot) {
        super("Игровое поле", true, true, true, true);
        setBounds(0, 0, 400, 400);
        m_visualizer = new GameVisualizer(controller,robot);
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
