package org.octobot.bot.internal.ui;

import org.octobot.Application;
import org.octobot.bot.handler.TextHandler;
import org.octobot.bot.internal.Account;
import org.octobot.bot.internal.AccountStore;
import org.octobot.bot.internal.Reward;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.event.ActionEvent;
import java.util.Iterator;

/**
 * AccountFrame
 *
 * @author Pat-ji
 */
public class AccountFrame extends JFrame {
    private static final String[] REWARDS;

    static {
        REWARDS = new String[Reward.values.length];
        for (int i = 0; i < REWARDS.length; i++)
            REWARDS[i] = Reward.values[i].getName();
    }

    private final JTable table;
    private final JButton remove;

    public AccountFrame(final ScriptSelectorFrame parent) {
        setTitle(Application.TITLE);
        setLayout(null);
        setResizable(false);
        setSize(600, 168);
        setLocationRelativeTo(MainFrame.instance);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        table = new JTable(new AccountTableModel());
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(new TableSelectionListener());

        final JScrollPane scrollPane = new JScrollPane();
        final TableColumnModel model = table.getColumnModel();
        model.getColumn(model.getColumnIndex("Password")).setCellRenderer(new PasswordCellRenderer());
        model.getColumn(model.getColumnIndex("Password")).setCellEditor(new PasswordCellEditor());
        model.getColumn(model.getColumnIndex("Pin")).setCellRenderer(new PasswordCellRenderer());
        model.getColumn(model.getColumnIndex("Pin")).setCellEditor(new PasswordCellEditor());
        model.getColumn(model.getColumnIndex("Reward")).setCellEditor(new RewardEditor());
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setViewportView(table);
        scrollPane.setBounds(0, 0, 480, 140);
        add(scrollPane);

        final JButton add = new JButton("Add");
        add.setBounds(485, 5, 100, 24);
        add.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                String name = JOptionPane.showInputDialog(getParent(), "Enter the account username.", "New Account", JOptionPane.QUESTION_MESSAGE);
                if (name == null || name.isEmpty()) return;

                name = TextHandler.encode(name);
                AccountStore.accounts.put(name, new Account(name, ""));
                ((AccountTableModel) table.getModel()).fireTableRowsInserted(table.getRowCount(), table.getRowCount());
            }
        });
        add(add);

        remove = new JButton("Remove");
        remove.setBounds(485, 55, 100, 24);
        remove.setEnabled(false);
        remove.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final int row = table.getSelectedRow();
                final String user = ((AccountTableModel) table.getModel()).userForRow(row);
                if (user != null) {
                    AccountStore.accounts.remove(user);
                    ((AccountTableModel) table.getModel()).fireTableRowsDeleted(row, row);
                }
            }
        });
        add(remove);

        final JButton finish = new JButton("Finish");
        finish.setBounds(485, 105, 100, 24);
        finish.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                AccountStore.save();
                parent.updateAccounts();
                dispose();
            }
        });
        add(finish);

        setVisible(true);
    }

    /**
     * TableSelectionListener
     *
     * @author Pat-ji
     */
    private final class TableSelectionListener implements ListSelectionListener {

        @Override
        public void valueChanged(final ListSelectionEvent event) {
            final int row = table.getSelectedRow();
            if (!event.getValueIsAdjusting())
                remove.setEnabled(row >= 0 && row < table.getRowCount());
        }

    }

    /**
     * RewardEditor
     *
     * @author Pat-ji
     */
    private final class RewardEditor extends DefaultCellEditor {

        public RewardEditor() {
            super(new JComboBox<String>(REWARDS));
        }

    }

    /**
     * PasswordCellEditor
     *
     * @author Pat-ji
     */
    private final class PasswordCellEditor extends DefaultCellEditor {

        public PasswordCellEditor() {
            super(new JPasswordField());
        }

    }

    /**
     * PasswordCellRenderer
     *
     * @author Pat-ji
     */
    private final class PasswordCellRenderer extends DefaultTableCellRenderer {

        @Override
        protected void setValue(final Object value) {
            if (value == null || (value instanceof String && ((String) value).isEmpty())) {
                setText("none");
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
     * AccountTableModel
     *
     * @author Pat-ji
     */
    private final class AccountTableModel extends AbstractTableModel {

        @Override
        public int getRowCount() {
            return AccountStore.accounts.size();
        }

        @Override
        public int getColumnCount() {
            return 4;
        }

        @Override
        public Object getValueAt(final int row, final int column) {
            final String name = userForRow(row);
            switch (column) {
                case 0:
                    return TextHandler.decode(name);
                case 1:
                    return TextHandler.decode(AccountStore.accounts.get(name).password);
                case 2:
                    return TextHandler.decode(AccountStore.accounts.get(name).pin);
                case 3:
                    return AccountStore.accounts.get(name).reward;
            }

            return null;
        }

        @Override
        public String getColumnName(final int column) {
            switch (column) {
                case 0:
                    return "Username";
                case 1:
                    return "Password";
                case 2:
                    return "Pin";
                case 3:
                    return "Reward";
            }

            return null;
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return column > 0;
        }

        @Override
        public void setValueAt(final Object value, final int row, final int column) {
            final Account acc = AccountStore.accounts.get(userForRow(row));
            if (acc == null) return;

            if (column == 1) {
                acc.password = TextHandler.encode(String.valueOf(value));
            } else if (column == 2) {
                acc.pin = TextHandler.encode(String.valueOf(value));
            } else if (column == 3) {
                acc.reward = Reward.get(value.toString());
            }

            fireTableCellUpdated(row, column);
        }

        private String userForRow(final int row) {
            final Iterator<Account> iterator = AccountStore.accounts.values().iterator();
            for (int k = 0; iterator.hasNext() && k < row; k++)
                iterator.next();

            return iterator.hasNext() ? iterator.next().name : null;
        }

    }

}
