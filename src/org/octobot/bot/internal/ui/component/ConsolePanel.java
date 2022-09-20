package org.octobot.bot.internal.ui.component;

import sun.swing.DefaultLookup;

import javax.swing.*;
import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * ConsolePanel
 *
 * @author Pat-ji
 */
@SuppressWarnings("all")
public class ConsolePanel extends JScrollPane {
    public static ConsolePanel instance;

    public final DefaultListModel<String> info;
    public final Map<String, Color> colorMap;
    private final DateFormat format;
    private final JList<String> list;

    public ConsolePanel() {
        info = new DefaultListModel<String>();
        colorMap = new HashMap<String, Color>();
        format = new SimpleDateFormat("HH:mm:ss");

        final Dimension dimension = new Dimension(765, 90);
        setSize(dimension);
        setBounds(0, 577, 765, 105);

        list = new JList<String>(info);
        list.setCellRenderer(new ListRenderer());
        getViewport().setView(list);

        instance = this;
    }

    public void println(final String text) {
        println(text, Color.BLACK);
    }

    public void println(String text, final Color color) {
        text = "[" + format.format(Calendar.getInstance().getTime()) + "] - " + text;

        info.addElement(text);
        colorMap.put(text, color);

        /*if (MainFrame.instance.consoleDisplayed)
            list.ensureIndexIsVisible(list.getModel().getSize() - 1);*/
    }

    /**
     * ListRenderer
     *
     * @author Pat-ji
     */
    private final class ListRenderer extends DefaultListCellRenderer {

        @Override
        public Component getListCellRendererComponent(final JList<?> list, final Object value, final int index, boolean isSelected, final boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            Color fg = new Color(240, 240, 240, 100);
            final JList.DropLocation dropLocation = list.getDropLocation();
            if (dropLocation != null && !dropLocation.isInsert() && dropLocation.getIndex() == index) {
                fg = DefaultLookup.getColor(this, ui, "List.dropCellForeground");
                isSelected = true;
            }

            if (isSelected) {
                setBackground(new Color(24, 24, 24, 100));
                setForeground(fg == null ? list.getSelectionForeground() : Color.WHITE);
            } else {
                setBackground(list.getBackground());
                setForeground(colorMap.get(value));
            }

            return this;
        }
    }

}
