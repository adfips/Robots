package model;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Класс робот, модель
 */
public class Robot {
    private final PropertyChangeSupport propertyChangeSupport;

    private volatile double m_robotPositionX;
    private volatile double m_robotPositionY;
    private volatile double m_robotDirection;

    private volatile int m_targetPositionX;
    private volatile int m_targetPositionY;

    private static final double maxVelocity = 0.1;
    private static final double maxAngularVelocity = 0.001;

    public Robot() {
        m_robotPositionX = 100;
        m_robotPositionY = 100;
        m_robotDirection = 0;
        m_robotDirection = 0;
        m_targetPositionX = 150;
        m_targetPositionY = 100;
        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    public void setTargetPosition(Point p)
    {
        m_targetPositionX = p.x;
        m_targetPositionY = p.y;
    }

    private static double distance(double x1, double y1, double x2, double y2) {
        double diffX = x1 - x2;
        double diffY = y1 - y2;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    private static double angleTo(double fromX, double fromY, double toX, double toY) {
        double diffX = toX - fromX;
        double diffY = toY - fromY;

        return asNormalizedRadians(Math.atan2(diffY, diffX));
    }

    public void onModelUpdateEvent() {
        double distance = distance(m_targetPositionX, m_targetPositionY,
                m_robotPositionX, m_robotPositionY);
        if (distance < 0.5) {
            return;
        }
        double velocity = maxVelocity;
        double angleToTarget = angleTo(m_robotPositionX, m_robotPositionY, m_targetPositionX, m_targetPositionY);
        double angularVelocity = 0;
        if (asNormalizedRadians(angleToTarget - m_robotDirection) < Math.PI) {
            angularVelocity = maxAngularVelocity;
        }
        if (asNormalizedRadians(angleToTarget - m_robotDirection) > Math.PI) {
            angularVelocity = -maxAngularVelocity;
        }
        if (achievable(m_targetPositionX, m_targetPositionY)) {
            angularVelocity = 0;
        }

        moveRobot(velocity, angularVelocity, 10);
        notifyUpdate();
    }

    private static double applyLimits(double value, double min, double max) {
        if (value < min)
            return min;
        if (value > max)
            return max;
        return value;
    }

    private void moveRobot(double velocity, double angularVelocity, double duration) {
        velocity = applyLimits(velocity, 0, maxVelocity);
        angularVelocity = applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);
        double newX = m_robotPositionX + velocity / angularVelocity *
                (Math.sin(m_robotDirection + angularVelocity * duration) -
                        Math.sin(m_robotDirection));


        if (!Double.isFinite(newX)) {
            newX = m_robotPositionX + velocity * duration * Math.cos(m_robotDirection);
        }
        double newY = m_robotPositionY - velocity / angularVelocity *
                (Math.cos(m_robotDirection + angularVelocity * duration) -
                        Math.cos(m_robotDirection));
        if (!Double.isFinite(newY)) {
            newY = m_robotPositionY + velocity * duration * Math.sin(m_robotDirection);
        }
        m_robotPositionX = newX;
        m_robotPositionY = newY;
        double newDirection = asNormalizedRadians(m_robotDirection + angularVelocity * duration);
        m_robotDirection = newDirection;
    }

    private static double asNormalizedRadians(double angle) {
        while (angle < 0) {
            angle += 2 * Math.PI;
        }
        while (angle >= 2 * Math.PI) {
            angle -= 2 * Math.PI;
        }
        return angle;
    }

    /**
     *  Определяет, достижима цель для робота на основе его текущего положения и направления
     */
    private boolean achievable(double targetPositionX, double targetPositionY) {
        double dx = targetPositionX - m_robotPositionX;
        double dy = targetPositionY - m_robotPositionY;

        double newDX = Math.cos(m_robotDirection) * dx + Math.sin(m_robotDirection) * dy;
        double newDY = Math.cos(m_robotDirection) * dy - Math.sin(m_robotDirection) * dx;

        double maxCurve = maxVelocity / maxAngularVelocity;
        double dist1 = distance(newDX, newDY, 0, maxCurve);
        double dist2 = distance(newDX, newDY + maxCurve, 0, 0);

        return !(dist1 > maxCurve) || !(dist2 > maxCurve);
    }

    /**
     * Уведомление слушателей об изменении модели
     */
    public void notifyUpdate(){
        propertyChangeSupport.firePropertyChange("updateModel",null,this);
    }

    public void addListener(PropertyChangeListener listener){
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public double getMRobotPositionX() {
        return m_robotPositionX;
    }

    public double getMRobotPositionY() {
        return m_robotPositionY;
    }

    public double getMRobotDirection() {
        return m_robotDirection;
    }

    public int getMTargetPositionX() {
        return m_targetPositionX;
    }

    public int getMTargetPositionY() {
        return m_targetPositionY;
    }

}
