package gui;


import locale.LocalizationListener;
import locale.LocalizationManager;
import model.Robot;
import saving.Savable;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Окно с координатами робота
 */
public class CoordinateRobotWindow extends JInternalFrame implements PropertyChangeListener, Savable,
        LocalizationListener {
    private final JLabel labelX;
    private final JLabel labelY;

    /**
     * Конструктор <br>
     * Добавляем модели прослушивание представления
     * Собираем окно
     */
    public CoordinateRobotWindow(Robot robot) {
        super(LocalizationManager.getString("coordinateRobotWindow"), true, true, true, true);
        robot.addListener(this);
        setSize(300, 200);

        JPanel panel = new JPanel();
        labelX = new JLabel("X: ");
        labelY = new JLabel("Y: ");
        panel.add(labelX);
        panel.add(labelY);

        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);
        LocalizationManager.addLocalizationListener(this);

    }

    /**
     * Изменение координат
     */
    private void setNumbers(double num1, double num2) {
        labelX.setText("x: " + String.format("%.2f", num1));
        labelY.setText("y: " + String.format("%.2f", num2));
    }

    /**
     * Получение и обновление новых координат
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("updateModel")) {
            Robot robot = (Robot) evt.getNewValue();
            this.setNumbers(robot.getMRobotPositionX(), robot.getMRobotPositionY());
        }
    }

    @Override
    public String getFrameId() {
        return "CoordinateWindow";
    }

    @Override
    public void localeChanged() {
        setTitle(LocalizationManager.getString("coordinateRobotWindow"));
        repaint();
    }
}
