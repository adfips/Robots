package gui;

import locale.LocalizationListener;
import locale.LocalizationManager;
import log.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * Системное меню
 */
public class MenuBar extends JMenuBar implements LocalizationListener{
    private final MainApplicationFrame frame;
    private final List<String> componentsMenu;

    /**
     * Заполняет меню полями
     */
    public MenuBar(MainApplicationFrame frame) {
        this.frame = frame;
        componentsMenu = new ArrayList<>();
        add(createLookAndFeelMenu());
        add(createTestMenu());
        add(createExitMenu());
        add(createLanguageMenu());
        LocalizationManager.addLocalizationListener(this);
    }

    /**
     * Создает поле меню отвечающее за режим отображение
     */
    private JMenu createLookAndFeelMenu() {
        JMenu lookAndFeelMenu = new JMenu(LocalizationManager.getString("lookAndFeel"));
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_D);
        lookAndFeelMenu.getAccessibleContext().setAccessibleDescription(
                LocalizationManager.getString("lookAndFeelDescription"));

        JMenuItem systemLookAndFeel = new JMenuItem(LocalizationManager.getString("lookAndFeelSystem"), KeyEvent.VK_S);
        systemLookAndFeel.addActionListener((event) -> {
            frame.setSystemLookAndFeel();
            this.invalidate();
        });
        lookAndFeelMenu.add(systemLookAndFeel);

        JMenuItem crossPlatformLookAndFeel = new JMenuItem(LocalizationManager.getString("lookAndFeelCross"), KeyEvent.VK_U);
        crossPlatformLookAndFeel.addActionListener((event) -> {
            frame.setCrossPlatformLookAndFeel();
            this.invalidate();
        });
        lookAndFeelMenu.add(crossPlatformLookAndFeel);
        componentsMenu.add("lookAndFeel");
        componentsMenu.add("lookAndFeelDescription");
        componentsMenu.add("lookAndFeelSystem");
        componentsMenu.add("lookAndFeelCross");
        return lookAndFeelMenu;
    }

    /**
     * Создает поле меню отвечающее за тесты
     */
    private JMenu createTestMenu() {
        JMenu testMenu = new JMenu(LocalizationManager.getString("test"));
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription(
                LocalizationManager.getString("testDescription"));

        JMenuItem addLogMessageItem = new JMenuItem(LocalizationManager.getString("testMessageItem"), KeyEvent.VK_M);
        addLogMessageItem.addActionListener((event) ->
                Logger.debug(LocalizationManager.getString("testMessage")));
        testMenu.add(addLogMessageItem);
        componentsMenu.add("test");
        componentsMenu.add("testDescription");
        componentsMenu.add("testMessageItem");
        return testMenu;
    }

    /**
     * Создает поле меню отвечающее за выход
     */
    private JMenu createExitMenu() {
        JMenu exitMenu = new JMenu(LocalizationManager.getString("exit"));
        exitMenu.setMnemonic(KeyEvent.VK_E);
        exitMenu.getAccessibleContext().setAccessibleDescription(
                LocalizationManager.getString("exitDescription"));
        JMenuItem exitMenuItem = new JMenuItem(LocalizationManager.getString("exitItem"), KeyEvent.VK_X);
        exitMenuItem.addActionListener((event) ->
                Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(
                        new WindowEvent(frame, WindowEvent.WINDOW_CLOSING))
        );

        exitMenu.add(exitMenuItem);
        componentsMenu.add("exit");
        componentsMenu.add("exitDescription");
        componentsMenu.add("exitItem");
        return exitMenu;
    }


    private JMenu createLanguageMenu(){
        JMenu languageMenu = new JMenu(LocalizationManager.getString("languageMenu"));
        languageMenu.getAccessibleContext().setAccessibleDescription(
                LocalizationManager.getString("languageMenuDescription"));
        JMenuItem enMenuItem = new JMenuItem(LocalizationManager.getString("en"), KeyEvent.VK_E);
        JMenuItem ruMenuItem = new JMenuItem(LocalizationManager.getString("ru"), KeyEvent.VK_R);
        enMenuItem.addActionListener((event)->
                LocalizationManager.setLocale(new Locale("en"))
        );
        ruMenuItem.addActionListener((event)->
                LocalizationManager.setLocale(new Locale("ru"))
        );
        languageMenu.add(enMenuItem);
        languageMenu.add(ruMenuItem);
        componentsMenu.add("languageMenu");
        componentsMenu.add("languageMenuDescription");
        componentsMenu.add("en");
        componentsMenu.add("ru");
        return languageMenu;
    }

    @Override
    public void localeChanged() {
        int index = 0;
        for(int i = 0; i<getMenuCount();i++){
            JMenu menu = getMenu(i);
            menu.setText(LocalizationManager.getString(componentsMenu.get(index++)));
            menu.getAccessibleContext().setAccessibleDescription(
                    LocalizationManager.getString(componentsMenu.get(index++))
            );
            for(int j = 0; j < menu.getItemCount();j++){
                JMenuItem menuItem = menu.getItem(j);
                menuItem.setText(LocalizationManager.getString(componentsMenu.get(index++)));
            }
        }
        repaint();
    }
}
