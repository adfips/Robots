package controller;

import model.Robot;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Контроллер. Класс, управляющий работой модели.
 */
public class Controller {
    /**
     * Экземпляр робота, который управляется данным контроллером.
     */
    private final Robot robot;

    /**
     * Конструктор. <br>
     * Создает экземпляр робота для управления.
     */
    public Controller(Robot robot) {
        this.robot = robot;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                robot.onModelUpdateEvent();
            }
        }, 0, 10);
    }

    public void setTargetPosition(Point point) {
        robot.setTargetPosition(point);
    }
}
