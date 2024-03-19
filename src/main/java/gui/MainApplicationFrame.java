package gui;

import log.Logger;
import saving.Savable;
import saving.WindowSettings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


/**
 * Главное окно программы
 */
public class MainApplicationFrame extends JFrame implements Savable {
    private final JDesktopPane desktopPane = new JDesktopPane();

    private final WindowSettings windowSettings = new WindowSettings();

    public MainApplicationFrame() {
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset, screenSize.width - inset * 2, screenSize.height - inset * 2);
        setContentPane(desktopPane);
        addWindow(createLogWindow());
        addWindow(createGameWindow());

        windowSettings.loadProperties(getAllComponents());

        setJMenuBar(new MenuBar(this));
        initWindowCloseListener();

    }

    /**
     * Создание окна "Протокол работы"
     */
    protected LogWindow createLogWindow() {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10, 10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug("Протокол работает");
        return logWindow;
    }

    /**
     * Создание окна с Роботом
     */
    protected GameWindow createGameWindow() {
        GameWindow gameWindow = new GameWindow();
        gameWindow.setSize(400, 400);
        gameWindow.setLocation(0, 0);
        return gameWindow;
    }

    /**
     * Добавление окон на главное окно программы
     */
    protected void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

    /**
     * Системный фон
     */
    public void setSystemLookAndFeel() {
        setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }

    /**
     * Универсальный фон
     */
    public void setCrossPlatformLookAndFeel() {
        setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    }

    /**
     * Изменение фона главного окна программы
     */
    protected void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (ClassNotFoundException | InstantiationException
                 | IllegalAccessException | UnsupportedLookAndFeelException e) {
            // just ignore
        }
    }

    public List<Component> getAllComponents() {
        List<Component> allComponents = new ArrayList<>(List.of(desktopPane.getAllFrames()));
        allComponents.add(this);
        return allComponents;
    }

    /**
     * Добавляет слушателя WindowListener к frame,
     * который реагирует на событие закрытия окна.
     */

    private void initWindowCloseListener() {
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                windowSettings.saveProperties(getAllComponents());
                handleWindowClosingEvent();
            }
        });
    }

    /**
     * При закрытии приложения, открывается окно для подтверждения
     */
    private void handleWindowClosingEvent() {
        int option = JOptionPane.showOptionDialog(
                this,
                "Вы хотите закрыть приложение?",
                "Подтверждение",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new String[]{"Да", "Нет"},
                "Да");
        if (option == JOptionPane.YES_OPTION)
            setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    @Override
    public void save(Properties properties) {
        String name = getClass().getName();
        properties.setProperty(
                name + "_bounds",
                String.format("%d,%d,%d,%d",getBounds().x,getBounds().y,getBounds().width,getBounds().height)
        );
    }


    @Override
    public void load(Properties properties) {
        String name = getClass().getName();
        String bounds = properties.getProperty(name + "_bounds");
        if (bounds != null) {
            Rectangle rectangle = parseRectangle(bounds);
            setBounds(rectangle);
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
