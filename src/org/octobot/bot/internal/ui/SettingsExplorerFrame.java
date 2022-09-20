package org.octobot.bot.internal.ui;

import org.octobot.Application;
import org.octobot.script.ScriptContext;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * SettingsExplorerFrame
 *
 * @author Pat-ji
 */
public class SettingsExplorerFrame extends JFrame implements Runnable {
    private final Map<Integer, Integer> cache = new HashMap<Integer, Integer>();

    private final ScriptContext context;
    private final DefaultListModel<String> info;
    private final DefaultListModel<String> settings;
    private final JList<String> changelog;

    private JTextField txtGotoSetting;

    public SettingsExplorerFrame(final ScriptContext context) {
        this.context = context;

        setTitle(Application.TITLE);
        setLayout(null);
        setResizable(false);
        setSize(725, 475);
        setLocationRelativeTo(MainFrame.instance);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        final JLabel lblSettingsList = new JLabel("Settings list");
        lblSettingsList.setFont(new Font("Tahoma", Font.BOLD, 11));
        lblSettingsList.setHorizontalAlignment(SwingConstants.LEFT);
        lblSettingsList.setBounds(10, 11, 90, 14);
        add(lblSettingsList);

        final DefaultListModel<String> details = new DefaultListModel<String>();
        final JList<String> lstSettings = new JList<String>((settings = new DefaultListModel<String>()));
        lstSettings.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (lstSettings.getSelectedIndex() == -1) {
                    final int[] collection = context.settings.get();
                    for (int i = 0; i < collection.length; i++) {
                        cache.put(i, collection[i]);

                        if (settings.size() > i) {
                            settings.set(i, String.format("[%s] = %s", i, collection[i]));
                        } else {
                            settings.addElement(String.format("[%s] = %s", i, collection[i]));
                        }
                    }

                    lstSettings.revalidate();
                    lstSettings.repaint();
                } else {
                    details.clear();
                    final String data = settings.getElementAt(lstSettings.getSelectedIndex());
                    details.addElement(String.format("Setting: %s", data.substring(1, data.indexOf("]"))));
                    details.addElement(String.format("Decimal: %s", data.substring(data.indexOf("=") + 2)));

                    final int value = Integer.parseInt(data.substring(data.indexOf("=") + 2));
                    details.addElement(String.format("Binary: %s", Integer.toBinaryString(value)));
                    details.addElement(String.format("Hex: 0x%s", Integer.toHexString(value)));
                }
            }
        });
        final JScrollPane scrollLstSettings = new JScrollPane(lstSettings, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollLstSettings.setBounds(10, 30, 200, 350);
        add(scrollLstSettings);

        final JLabel lblGotoSetting = new JLabel("Goto Setting:");
        lblGotoSetting.setBounds(10, 397, 90, 14);
        add(lblGotoSetting);
        lblGotoSetting.setFont(new Font("Tahoma", Font.BOLD, 11));

        txtGotoSetting = new JTextField();
        txtGotoSetting.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(final DocumentEvent e) {
                refresh();
            }

            @Override
            public void insertUpdate(final DocumentEvent e) {
                refresh();
            }

            @Override
            public void removeUpdate(final DocumentEvent e) {
                refresh();
            }

            private void refresh(){
                final String text = txtGotoSetting.getText();
                if (validNumber(text)){
                    final int index = Integer.parseInt(text);
                    if(index <= lstSettings.getModel().getSize()){
                        lstSettings.setSelectedIndex(index);
                        lstSettings.ensureIndexIsVisible(index);
                    }
                }
            }
        });
        txtGotoSetting.setBounds(98, 392, 112, 25);
        add(txtGotoSetting);
        txtGotoSetting.setColumns(10);

        final JLabel lblSettingsDetails = new JLabel("Setting detail");
        lblSettingsDetails.setHorizontalAlignment(SwingConstants.LEFT);
        lblSettingsDetails.setFont(new Font("Tahoma", Font.BOLD, 11));
        lblSettingsDetails.setBounds(240, 11, 90, 14);
        add(lblSettingsDetails);

        final JList<String> lstSettingDetail = new JList<String>(details);
        final JScrollPane scrollLstSettingDetail = new JScrollPane(lstSettingDetail, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollLstSettingDetail.setBounds(240, 30, 470, 100);
        add(scrollLstSettingDetail);

        final JLabel lblChangeLog = new JLabel("Change log");
        lblChangeLog.setHorizontalAlignment(SwingConstants.LEFT);
        lblChangeLog.setFont(new Font("Tahoma", Font.BOLD, 11));
        lblChangeLog.setBounds(240, 141, 90, 14);
        add(lblChangeLog);

        changelog = new JList<String>((info = new DefaultListModel<String>()));
        final JScrollPane scrollChangeLog = new JScrollPane(changelog, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollChangeLog.setBounds(240, 166, 470, 243);
        add(scrollChangeLog);

        final JButton btnClearLog = new JButton("Clear log");
        btnClearLog.setBorderPainted(false);
        btnClearLog.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                info.removeAllElements();
            }
        });
        btnClearLog.setBounds(621, 137, 89, 23);
        add(btnClearLog);

        setVisible(true);

        new Thread(this).start();
    }

    private boolean validNumber(final String string) {
        if (string.length() <= 0) return false;

        for (int i = 0; i < string.length(); i++) {
            final int n = (int) string.charAt(i);
            if (n < 48 || n > 57) return false;
        }

        return true;
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (!isVisible()) break;
                final int[] settings = context.settings.get();
                for (int i = 0; i < settings.length; i++)
                    if (cache.containsKey(i)) {
                        if (cache.get(i) != settings[i]) {
                            info.addElement("[" + i + "] - " + cache.get(i) + " (0x" + Integer.toHexString(cache.get(i)) + " - " + Integer.toBinaryString(cache.get(i)) + ") -> "
                                    + settings[i] + " (0x" + Integer.toHexString(settings[i]) + " - " + Integer.toBinaryString(settings[i]) + ")");
                            cache.put(i, settings[i]);

                            if (this.settings.size() > i) {
                                this.settings.set(i, String.format("[%s] = %s", i, settings[i]));
                            } else {
                                this.settings.addElement(String.format("[%s] = %s", i, settings[i]));
                            }
                        }
                    } else {
                        cache.put(i, settings[i]);

                        if (this.settings.size() > i) {
                            this.settings.set(i, String.format("[%s] = %s", i, settings[i]));
                        } else {
                            this.settings.addElement(String.format("[%s] = %s", i, settings[i]));
                        }
                    }

                Thread.sleep(500);
            } catch (final Throwable ignored) { }
        }
    }

}
