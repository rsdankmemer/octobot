package org.octobot.bot.internal.ui;

import org.octobot.Application;
import org.octobot.bot.Environment;
import org.octobot.bot.GameDefinition;
import org.octobot.bot.game.loader.Crawler;
import org.octobot.bot.handler.OptionsHandler;
import org.octobot.bot.handler.TextHandler;
import org.octobot.bot.internal.BreakProfile;
import org.octobot.bot.internal.ProxyStore;
import org.octobot.script.event.BreakEvent;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * OptionsFrame
 *
 * @author Pat-ji
 */
@SuppressWarnings("all")
public class OptionsFrame extends JFrame {
    private final JComboBox<String> proxies;

    public OptionsFrame(final GameDefinition definition) {
        setTitle(Application.TITLE);
        setLayout(null);
        setResizable(false);
        setSize(800, 440);
        setLocationRelativeTo(MainFrame.instance);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        /*Confirm Button*/
        JButton button = new JButton("Confirm");
        button.setBounds(30, 380, 100, 24);
        button.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                for (final Component component : ((JPanel) ((JLayeredPane) getRootPane().getComponent(1)).getComponent(0)).getComponents())
                    if (component instanceof JCheckBox) {
                        final JCheckBox checkBox = (JCheckBox) component;
                        if (checkBox.getText().contains("Offline")) {
                            Environment.offline = checkBox.isSelected();
                        } else if (checkBox.getText().contains("rendering")) {
                            definition.canvas.renderingDisabled = checkBox.isSelected();
                        }
                    } else if (component instanceof JTextField) {
                        try {
                            Crawler.world = Integer.parseInt(((JTextField) component).getText());
                        } catch (final Exception ex) {
                            Crawler.world = 2;
                        }
                    } else if (component instanceof JComboBox) {
                        final String proxy = ((JComboBox<String>) component).getModel().getSelectedItem().toString();
                        if (definition != null) {
                            definition.proxy = ProxyStore.proxies.get(TextHandler.encode(proxy));
                        } else {
                            Environment.proxy = ProxyStore.proxies.get(TextHandler.encode(proxy));
                        }
                    }

                OptionsHandler.save();
                BreakProfile.save();
                dispose();
            }
        });
        add(button);

        /*Random Panel*/
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder("Randoms"));
        panel.setLayout(null);
        panel.setBounds(5, 5, 410, 365);

        JScrollPane pane = new JScrollPane();
        pane.setBounds(20, 20, 150, 320);

        final JList<String> addList = new JList<String>();
        addList.setModel(new DefaultListModel<String>());
        int index = 0;
        if (definition != null)
            for (final String event : definition.randomHandler.randomNames)
                if (!definition.randomHandler.disabled[index++])
                    ((DefaultListModel<String>) addList.getModel()).addElement(event);

        pane.setViewportView(addList);
        panel.add(pane);

        pane = new JScrollPane();
        pane.setBounds(235, 20, 150, 320);

        final JList<String> ignoreList = new JList<String>();
        ignoreList.setModel(new DefaultListModel<String>());
        index = 0;
        if (definition != null)
            for (final String event : definition.randomHandler.randomNames)
                if (definition.randomHandler.disabled[index++])
                    ((DefaultListModel<String>) ignoreList.getModel()).addElement(event);

        pane.setViewportView(ignoreList);
        panel.add(pane);

        button = new JButton(">>");
        button.setBounds(175, 155, 52, 25);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                if (definition == null) return;

                final DefaultListModel<String> model = (DefaultListModel<String>) addList.getModel();
                for (int i = addList.getMinSelectionIndex(); i < addList.getMaxSelectionIndex() + 1; i++)
                    if (addList.isSelectedIndex(i)) {
                        final String name = model.getElementAt(i);
                        int index = 0;
                        for (final String check : definition.randomHandler.randomNames) {
                            if (name.equals(check)) {
                                definition.randomHandler.disabled[index] = true;
                                System.out.println("[OptionsFrame] - Disabling random: " + name);
                                break;
                            }

                            index++;
                        }

                        ((DefaultListModel<String>) ignoreList.getModel()).addElement(name);
                        model.removeElementAt(i);
                    }
            }
        });
        panel.add(button);

        button = new JButton("<<");
        button.setBounds(175, 190, 52, 25);
        button.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                if (definition == null) return;

                final DefaultListModel<String> model = (DefaultListModel<String>) ignoreList.getModel();
                for (int i = ignoreList.getMinSelectionIndex(); i < ignoreList.getMaxSelectionIndex() + 1; i++)
                    if (ignoreList.isSelectedIndex(i)) {
                        final String name = model.getElementAt(i);
                        int index = 0;
                        for (final String check : definition.randomHandler.randomNames) {
                            if (name.equals(check)) {
                                definition.randomHandler.disabled[index] = false;
                                System.out.println("[OptionsFrame] - Enabling random: " + name);
                                break;
                            }

                            index++;
                        }

                        ((DefaultListModel<String>) addList.getModel()).addElement(name);
                        model.removeElementAt(i);
                    }
            }
        });
        panel.add(button);
        if (definition == null)
            for (final Component component : panel.getComponents()) {
                component.setEnabled(false);

                if (component instanceof JScrollPane)
                    for (final Component child : ((JScrollPane) component).getComponents())
                        child.setEnabled(false);
            }

        add(panel);

        /*Breaks*/
        panel = new JPanel();
        panel.setBorder(new TitledBorder("BreakHandler"));
        panel.setBounds(430, 5, 365, 365);
        panel.setLayout(null);

        final JPanel breakDataPanel = new JPanel();
        breakDataPanel.setBorder(new TitledBorder("Create Break Event"));
        breakDataPanel.setBounds(15, 20, 335, 125);
        breakDataPanel.setLayout(null);

        JTextField textField = new JTextField("Break Time");
        textField.setBounds(20, 30, 130, 25);
        breakDataPanel.add(textField);

        textField = new JTextField("Script Time");
        textField.setBounds(20, 70, 130, 25);
        breakDataPanel.add(textField);

        textField = new JTextField("Break Deviation");
        textField.setBounds(185, 30, 130, 25);
        breakDataPanel.add(textField);

        textField = new JTextField("Script Deviation");
        textField.setBounds(185, 70, 130, 25);
        breakDataPanel.add(textField);
        panel.add(breakDataPanel);

        final JList<String> eventList = new JList<String>();
        eventList.setModel(new DefaultListModel<String>());
        if (definition != null)
            for (final BreakEvent event : definition.breakHandler.breakEvents)
                ((DefaultListModel<String>) eventList.getModel()).addElement(event.toString());

        pane = new JScrollPane();
        pane.setBounds(15, 150, 335, 170);
        pane.setViewportView(eventList);
        panel.add(pane);

        button = new JButton("Add");
        button.setBounds(15, 330, 80, 28);
        button.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                if (definition == null) return;

                final java.util.List<String> items = new ArrayList<String>();
                for (final Component component : breakDataPanel.getComponents())
                    if (component instanceof JTextField) {
                        final String text = ((JTextField) component).getText();
                        for (final char c : text.toCharArray())
                            if (!Character.isDigit(c)) {
                                System.out.println("[OptionsFrame] - Please fill in valid information (in minutes) before trying to add a break event");
                                return;
                            }

                        items.add(text);
                    }

                try {
                    final BreakEvent event = new BreakEvent(Integer.parseInt(items.get(0)), Integer.parseInt(items.get(2)),
                            Integer.parseInt(items.get(1)), Integer.parseInt(items.get(3)));

                    definition.breakHandler.breakEvents.add(event);
                    ((DefaultListModel<String>) eventList.getModel()).addElement(event.toString());
                    int index = 0;
                    for (final Component component : breakDataPanel.getComponents())
                        if (component instanceof JTextField) {
                            switch (index++) {
                                case 0:
                                    ((JTextField) component).setText("Break Time");
                                    break;
                                case 1:
                                    ((JTextField) component).setText("Script Time");
                                    break;
                                case 2:
                                    ((JTextField) component).setText("Break Deviation");
                                    break;
                                case 3:
                                    ((JTextField) component).setText("Script Deviation");
                                    break;
                            }
                        }
                } catch (final Exception ex) {
                    System.out.println("[OptionsFrame] - Failed to add the event, be sure you've spelled the times (in minutes) correctly");
                    ex.printStackTrace();
                }
            }
        });
        panel.add(button);

        button = new JButton("Load");
        button.setBounds(100, 330, 80, 28);
        button.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                new LoadBreakFrame(definition, ((DefaultListModel<String>) eventList.getModel())).setVisible(true);
            }
        });
        panel.add(button);

        button = new JButton("Save");
        button.setBounds(185, 330, 80, 28);
        button.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                new SaveBreakFrame(definition).setVisible(true);
            }
        });
        panel.add(button);

        button = new JButton("Remove");
        button.setBounds(270, 330, 80, 28);
        button.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                for (int i = eventList.getMinSelectionIndex(); i < eventList.getMaxSelectionIndex() + 1; i++)
                    if (eventList.isSelectedIndex(i)) {
                        ((DefaultListModel<String>) eventList.getModel()).removeElementAt(i);
                        definition.breakHandler.breakEvents.remove(i);
                    }
            }
        });
        panel.add(button);
        if (definition == null)
            for (final Component component : panel.getComponents()) {
                component.setEnabled(false);

                if (component instanceof JPanel)
                    for (final Component child : ((JPanel) component).getComponents())
                        child.setEnabled(false);
            }

        add(panel);

        /*Utility*/
        JLabel label = new JLabel("Default world");
        label.setBounds(160, 382, 200, 22);
        add(label);

        textField = new JTextField("" + Crawler.world);
        textField.setBounds(240, 382, 57, 22);
        add(textField);

        JCheckBox checkBox = new JCheckBox("Offline mode");
        checkBox.setBounds(310, 382, 100, 22);
        add(checkBox);

        label = new JLabel("Proxy");
        label.setBounds(415, 382, 200, 22);
        add(label);

        proxies = new JComboBox<String>();
        updateProxies();

        boolean proxy = false;
        if (definition != null && definition.proxy != null) {
            final String name = TextHandler.decode(definition.proxy.name);
            for (int i = 0; i < proxies.getModel().getSize(); i++)
                if (proxies.getModel().getElementAt(i).equals(name)) {
                    proxies.setSelectedIndex(i);
                    proxy = true;
                    break;
                }
        }
        if (!proxy && Environment.proxy != null) {
            final String name = TextHandler.decode(Environment.proxy.name);
            for (int i = 0; i < proxies.getModel().getSize(); i++)
                if (proxies.getModel().getElementAt(i).equals(name)) {
                    proxies.setSelectedIndex(i);
                    break;
                }
        }

        proxies.setBounds(460, 382, 120, 22);
        add(proxies);

        final JButton show = new JButton("Show");
        show.setBounds(598, 382, 60, 22);
        show.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                new ProxyFrame((OptionsFrame) show.getParent().getParent().getParent().getParent());
            }
        });
        add(show);

        checkBox = new JCheckBox("Disable rendering");
        checkBox.setBounds(670, 382, 150, 22);
        if (definition == null) {
            checkBox.setEnabled(false);
        } else {
            checkBox.setSelected(definition.canvas.renderingDisabled);
        }

        add(checkBox);

        setVisible(true);
    }

    public void updateProxies() {
        final String[] items;
        if (ProxyStore.proxies.isEmpty()) {
            items = new String[] { "none" };
        } else {
            items = new String[ProxyStore.proxies.size() + 1];
        }

        int index = 1;
        items[0] = "none";
        for (final String name : ProxyStore.proxies.keySet())
            items[index++] = TextHandler.decode(name);

        proxies.setModel(new DefaultComboBoxModel<String>(items));
    }

}
