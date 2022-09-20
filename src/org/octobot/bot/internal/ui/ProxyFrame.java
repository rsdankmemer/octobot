package org.octobot.bot.internal.ui;

import org.octobot.Application;
import org.octobot.bot.handler.TextHandler;
import org.octobot.bot.internal.Proxy;
import org.octobot.bot.internal.ProxyStore;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.event.ActionEvent;
import java.util.Iterator;

/**
 * ProxyFrame
 *
 * @author Pat-ji
 */
public class ProxyFrame extends JFrame {

    public ProxyFrame(final OptionsFrame parent) {
        setTitle(Application.TITLE);
        setLayout(null);
        setResizable(false);
        setSize(500, 320);
        setLocationRelativeTo(MainFrame.instance);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        final JTable table = new JTable(new ProxyTableModel());

        /*Buttons*/
        JButton button = new JButton("Confirm");
        button.setBounds(50, 260, 100, 24);
        button.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                for (final Proxy proxy : ProxyStore.proxies.values())
                    if (proxy.host.equals("none") || proxy.port.equals("none")) {
                        System.out.println("[ProxyFrame] - Please fill in the required fields before confirming your proxies.");
                        System.out.println("[ProxyFrame] - If you have, be sure there is no selected field and try again.");
                        return;
                    }

                parent.updateProxies();
                ProxyStore.save();
                dispose();
            }
        });
        add(button);

        button = new JButton("Add");
        button.setBounds(250, 260, 100, 24);
        button.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                String name = JOptionPane.showInputDialog(getParent(), "Enter the proxy nickname.", "New Proxy", JOptionPane.QUESTION_MESSAGE);
                if (name == null || name.isEmpty()) return;

                name = TextHandler.encode(name);
                ProxyStore.proxies.put(name, new Proxy(name, "none", "none"));
                ((ProxyTableModel) table.getModel()).fireTableRowsInserted(table.getRowCount(), table.getRowCount());
            }
        });
        add(button);

        final JButton remove = new JButton("Remove");
        remove.setBounds(350, 260, 100, 24);
        remove.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final int row = table.getSelectedRow();
                final String proxy = ((ProxyTableModel) table.getModel()).proxyForName(row);
                if (proxy != null) {
                    ProxyStore.proxies.remove(proxy);
                    ((ProxyTableModel) table.getModel()).fireTableRowsDeleted(row, row);
                }
            }
        });
        add(remove);

        /*Lists*/
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(final ListSelectionEvent event) {
                final int row = table.getSelectedRow();
                if (!event.getValueIsAdjusting())
                    remove.setEnabled(row >= 0 && row < table.getRowCount());
            }
        });

        final TableColumnModel model = table.getColumnModel();
        model.getColumn(model.getColumnIndex("Host*")).setCellRenderer(new DefaultCellRenderer());
        model.getColumn(model.getColumnIndex("Port*")).setCellRenderer(new DefaultCellRenderer());
        model.getColumn(model.getColumnIndex("Username")).setCellRenderer(new DefaultCellRenderer());
        model.getColumn(model.getColumnIndex("Password")).setCellEditor(new DefaultCellEditor(new JPasswordField()));
        model.getColumn(model.getColumnIndex("Password")).setCellRenderer(new HiddenCellRenderer());

        final JScrollPane scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setViewportView(table);
        scrollPane.setBounds(0, 0, 500, 240);
        add(scrollPane);

        setVisible(true);
    }

    /**
     * DefaultCellRenderer
     *
     * @author Pat-ji
     */
    private final class DefaultCellRenderer extends DefaultTableCellRenderer {

        @Override
        protected void setValue(final Object value) {
            setText((value == null || (value instanceof String && ((String) value).isEmpty())) ? "none" : value.toString());
        }

    }

    /**
     * HiddenCellRenderer
     *
     * @author Pat-ji
     */
    private final class HiddenCellRenderer extends DefaultTableCellRenderer {

        @Override
        protected void setValue(final Object value) {
            if (value == null || (value instanceof String && ((String) value).isEmpty())) {
                setText("****");
            } else {
                final String text = value.toString();
                final StringBuilder builder = new StringBuilder();
                for (int i = 0; i < text.length(); ++i)
                    builder.append("*");

                setText(builder.toString());
            }
        }

    }

    /**
     * ProxyTableModel
     *
     * @author Pat-ji
     */
    private final class ProxyTableModel extends AbstractTableModel {

        @Override
        public int getRowCount() {
            return ProxyStore.proxies.size();
        }

        @Override
        public int getColumnCount() {
            return 5;
        }

        @Override
        public Object getValueAt(final int row, final int column) {
            final Proxy proxy = ProxyStore.proxies.get(proxyForName(row));
            switch (column) {
                case 0:
                    return TextHandler.decode(proxy.name);
                case 1:
                    return TextHandler.decode(proxy.host);
                case 2:
                    return TextHandler.decode(proxy.port);
                case 3:
                    return TextHandler.decode(proxy.username);
                case 4:
                    return TextHandler.decode(proxy.password);
            }

            return null;
        }

        @Override
        public String getColumnName(final int column) {
            switch (column) {
                case 0:
                    return "Name";
                case 1:
                    return "Host*";
                case 2:
                    return "Port*";
                case 3:
                    return "Username";
                case 4:
                    return "Password";
            }

            return null;
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return column > 0;
        }

        @Override
        public void setValueAt(final Object value, final int row, final int column) {
            final Proxy proxy = ProxyStore.proxies.get(proxyForName(row));
            if (column == 1) {
                proxy.host = TextHandler.encode(String.valueOf(value));
            } else if (column == 2) {
                proxy.port = TextHandler.encode(String.valueOf(value));
            } else if (column == 3) {
                proxy.username = TextHandler.encode(String.valueOf(value));
            } else if (column == 4) {
                proxy.password = TextHandler.encode(String.valueOf(value));
            }

            fireTableCellUpdated(row, column);
        }

        private String proxyForName(final int row) {
            final Iterator<Proxy> iterator = ProxyStore.proxies.values().iterator();
            for (int k = 0; iterator.hasNext() && k < row; k++)
                iterator.next();

            return iterator.hasNext() ? iterator.next().name : null;
        }

    }

}
