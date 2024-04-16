package gui;


import controller.Controller;
import model.Robot;
import saving.Savable;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Окно с координатами робота
 */
public class CoordinateRobotWindow extends JInternalFrame implements PropertyChangeListener, Savable {
    private final JLabel labelX;
    private final JLabel labelY;

    /**
     * Конструктор <br>
     * Добавляем модели прослушивание представления
     * Собираем окно
     */
    public CoordinateRobotWindow(Controller controller) {
        super("Координаты робота", true, true, true, true);
        controller.getRobot().addListener(this);
        setSize(300, 200);

        JPanel panel = new JPanel();
        labelX = new JLabel("X: ");
        labelY = new JLabel("Y: ");
        panel.add(labelX);
        panel.add(labelY);

        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);

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
        if (evt.getPropertyName().equals("propertyName")) {
            Robot robot = (Robot) evt.getNewValue();
            this.setNumbers(robot.getM_robotPositionX(), robot.getM_robotPositionY());
        }
    }

    @Override
    public String getFrameId() {
        return "CoordinateWindow";
    }
}
