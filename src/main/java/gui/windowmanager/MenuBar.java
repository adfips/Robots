package gui.windowmanager;

import log.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;


/**
 * Системное меню
 */
public class MenuBar extends JMenuBar {
    private final MainApplicationFrame frame;

    /**
     * Заполняет меню полями
     */
    public MenuBar(MainApplicationFrame frame) {
        this.frame = frame;
        add(createLookAndFeelMenu());
        add(createTestMenu());
        add(createExitMenu());
    }

    /**
     * Создает поле меню отвечающее за режим отображение
     */
    private JMenu createLookAndFeelMenu() {
        JMenu lookAndFeelMenu = new JMenu("Display mode");
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_D);
        lookAndFeelMenu.getAccessibleContext().setAccessibleDescription(
                "Управление режимом отображения приложения");

        JMenuItem systemLookAndFeel = new JMenuItem("System scheme", KeyEvent.VK_S);
        systemLookAndFeel.addActionListener((event) -> {
            frame.setSystemLookAndFeel();
            this.invalidate();
        });
        lookAndFeelMenu.add(systemLookAndFeel);

        JMenuItem crossPlatformLookAndFeel = new JMenuItem("Universal scheme", KeyEvent.VK_U);
        crossPlatformLookAndFeel.addActionListener((event) -> {
            frame.setCrossPlatformLookAndFeel();
            this.invalidate();
        });
        lookAndFeelMenu.add(crossPlatformLookAndFeel);
        return lookAndFeelMenu;
    }

    /**
     * Создает поле меню отвечающее за тесты
     */
    private JMenu createTestMenu() {
        JMenu testMenu = new JMenu("Tests");
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription(
                "Тестовые команды");

        JMenuItem addLogMessageItem = new JMenuItem("Message in log", KeyEvent.VK_M);
        addLogMessageItem.addActionListener((event) ->
                Logger.debug("Новая строка"));
        testMenu.add(addLogMessageItem);
        return testMenu;
    }

    /**
     * Создает поле меню отвечающее за выход
     */
    private JMenu createExitMenu() {
        JMenu exitMenu = new JMenu("Exit");
        exitMenu.setMnemonic(KeyEvent.VK_E);
        exitMenu.getAccessibleContext().setAccessibleDescription(
                "Выход из программы");
        JMenuItem exitMenuItem = new JMenuItem("Exit", KeyEvent.VK_E);
        exitMenuItem.addActionListener((event) ->
                Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(
                        new WindowEvent(frame, WindowEvent.WINDOW_CLOSING))
        );

        exitMenu.add(exitMenuItem);
        return exitMenu;
    }


}
