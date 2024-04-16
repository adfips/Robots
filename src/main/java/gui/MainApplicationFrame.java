package gui;

import controller.Controller;
import log.Logger;
import saving.Savable;
import saving.WindowSettings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;


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

        Controller controller = new Controller();
        addWindow(createLogWindow());
        addWindow(new GameWindow(controller));
        addWindow(new CoordinateRobotWindow(controller));

        windowSettings.loadProperties(getAllComponents());

        setJMenuBar(new MenuBar(this));
        initWindowCloseListener();
        controller.start();

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
    private void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (ClassNotFoundException | InstantiationException
                 | IllegalAccessException | UnsupportedLookAndFeelException e) {
            // just ignore
        }
    }

    public List<? extends Component> getAllComponents() {
        return Stream.concat(
                Stream.of(this),
                Arrays.stream(desktopPane.getAllFrames())
        ).toList();
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
    public String getFrameId() {
        return "Main";
    }
}
