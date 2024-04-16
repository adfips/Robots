package controller;

import model.Robot;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Контроллер. Класс, управляющий работой модели.
 */
public class Controller {
    /**
     * Экземпляр робота, который управляется данным контроллером.
     */
    Robot robot;

    /**
     * Конструктор. <br>
     * Создает экземпляр робота для управления.
     */
    public Controller() {
        this.robot = new Robot();
    }

    /**
     * Начинает выполнение цикла обновления модели робота с определенной частотой.
     */
    public void start() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                robot.onModelUpdateEvent();
            }
        }, 0, 10);
    }


    public Robot getRobot() {
        return robot;
    }
}
