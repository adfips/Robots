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
        JMenu lookAndFeelMenu = new JMenu("Режим отображения");
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
        lookAndFeelMenu.getAccessibleContext().setAccessibleDescription(
                "Управление режимом отображения приложения");

        JMenuItem systemLookAndFeel = new JMenuItem("Системная схема", KeyEvent.VK_S);
        systemLookAndFeel.addActionListener((event) -> {
            frame.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            this.invalidate();
        });
        lookAndFeelMenu.add(systemLookAndFeel);

        JMenuItem crossplatformLookAndFeel = new JMenuItem("Универсальная схема", KeyEvent.VK_S);
        crossplatformLookAndFeel.addActionListener((event) -> {
            frame.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            this.invalidate();
        });
        lookAndFeelMenu.add(crossplatformLookAndFeel);
        return lookAndFeelMenu;
    }

    /**
     * Создает поле меню отвечающее за тесты
     */
    private JMenu createTestMenu() {
        JMenu testMenu = new JMenu("Тесты");
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription(
                "Тестовые команды");

        JMenuItem addLogMessageItem = new JMenuItem("Сообщение в лог", KeyEvent.VK_S);
        addLogMessageItem.addActionListener((event) ->
                Logger.debug("Новая строка"));
        testMenu.add(addLogMessageItem);
        return testMenu;
    }

    /**
     * Создает поле меню отвечающее за выход
     */
    private JMenu createExitMenu() {
        JMenu exitMenu = new JMenu("Выход");
        exitMenu.setMnemonic(KeyEvent.VK_E);
        exitMenu.getAccessibleContext().setAccessibleDescription(
                "Выход из программы");
        JMenuItem exitMenuItem = new JMenuItem("Выход", KeyEvent.VK_S);
        exitMenuItem.addActionListener((event) ->
                Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(
                        new WindowEvent(frame, WindowEvent.WINDOW_CLOSING))
        );

        exitMenu.add(exitMenuItem);
        return exitMenu;
    }


}
