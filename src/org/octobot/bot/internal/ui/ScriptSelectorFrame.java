package org.octobot.bot.internal.ui;

import org.octobot.Application;
import org.octobot.bot.Environment;
import org.octobot.bot.GameDefinition;
import org.octobot.bot.game.loader.HttpClient;
import org.octobot.bot.game.script.GameScript;
import org.octobot.bot.game.script.ScriptManifest;
import org.octobot.bot.game.script.loader.LocalClassLoader;
import org.octobot.bot.game.script.loader.LocalScript;
import org.octobot.bot.game.script.loader.SDNScript;
import org.octobot.bot.game.script.loader.ScriptSource;
import org.octobot.bot.handler.TextHandler;
import org.octobot.bot.internal.AccountStore;
import org.octobot.bot.internal.User;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 * ScriptSelectorFrame
 *
 * @author Pat-ji
 */
@SuppressWarnings("all")
public class ScriptSelectorFrame extends JFrame {
    private static final File ROOT = new File(Environment.getScriptsDirectory());

    private final List<ScriptSource> cache;
    private final List<ScriptSource> sources;

    private final JTable table;
    private final JButton start;
    private final JComboBox<String> accountBox;

    public ScriptSelectorFrame(final GameDefinition definition) {
        setTitle(Application.TITLE);
        setResizable(false);
        setSize(745, 380);
        setLayout(null);
        setLocationRelativeTo(MainFrame.instance);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        cache = new ArrayList<ScriptSource>();
        sources = new ArrayList<ScriptSource>();

        table = new JTable(new ScriptTableModel());
        table.getTableHeader().setReorderingAllowed(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(new TableSelectionListener());
        table.setDragEnabled(false);

        final JScrollPane scrollPane = new JScrollPane();
        final TableColumnModel model = table.getColumnModel();
        model.getColumn(model.getColumnIndex("Name")).setMaxWidth(170);
        model.getColumn(model.getColumnIndex("Name")).setMinWidth(170);
        model.getColumn(model.getColumnIndex("Authors")).setMaxWidth(170);
        model.getColumn(model.getColumnIndex("Authors")).setMinWidth(170);
        model.getColumn(model.getColumnIndex("Version")).setMaxWidth(60);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setViewportView(table);
        scrollPane.setBounds(0, 0, 737, 300);
        add(scrollPane);

        final JTextField searchField = new JTextField();
        searchField.setBounds(20, 313, 180, 24);
        searchField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(final KeyEvent e) {
            }

            @Override
            public void keyPressed(final KeyEvent e) {
            }

            @Override
            public void keyReleased(final KeyEvent e) {
                sort(searchField.getText());
            }
        });
        add(searchField);

        accountBox = new JComboBox<String>();
        accountBox.setBounds(208, 313, 152, 24);
        updateAccounts();
        add(accountBox);

        final JButton accounts = new JButton("Accounts");
        accounts.setBounds(369, 313, 100, 24);
        accounts.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                new AccountFrame((ScriptSelectorFrame) start.getParent().getParent().getParent().getParent());
            }
        });
        add(accounts);

        final JButton refresh = new JButton("Refresh");
        refresh.setBounds(478, 313, 100, 24);
        refresh.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                updateAccounts();
                load();
            }
        });
        add(refresh);

        start = new JButton("Start");
        start.setEnabled(false);
        start.setBounds(588, 313, 100, 24);
        start.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final boolean none = accountBox.getSelectedItem().equals("none");
                final String accountName = TextHandler.encode((String) accountBox.getSelectedItem());
                if (!none && !AccountStore.accounts.containsKey(accountName)) {
                    System.err.println("Please select a valid account first");
                } else {
                    if (!none)
                        definition.account = AccountStore.accounts.get(accountName);

                    startScript(definition);
                    dispose();
                }
            }
        });
        add(start);

        load();

        setVisible(true);
    }

    public void updateAccounts() {
        final String[] items;
        if (AccountStore.accounts.isEmpty()) {
            items = new String[] { "none" };
        } else {
            items = new String[AccountStore.accounts.size()];
            final Iterator<String> iterator = AccountStore.accounts.keySet().iterator();
            int index = 0;
            while (iterator.hasNext()) {
                items[index++] = TextHandler.decode(iterator.next());
            }
        }

        accountBox.setModel(new DefaultComboBoxModel<String>(items));
    }

    private void load() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    cache.clear();
                    loadLocalScripts();
                    loadSdnScripts();
                    sort("");
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void sort(String text) {
        text = text.toLowerCase();

        sources.clear();
        for (final ScriptSource source : cache)
            if (source.name().toLowerCase().contains(text))
                sources.add(source);

        ((ScriptTableModel) table.getModel()).fireTableRowsInserted(table.getRowCount(), table.getRowCount());
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
                start.setEnabled(row >= 0 && row < table.getRowCount());
        }

    }

    /**
     * ScriptTableModel
     *
     * @author Pat-ji
     */
    private final class ScriptTableModel extends AbstractTableModel {

        @Override
        public int getRowCount() {
            return sources.size();
        }

        @Override
        public int getColumnCount() {
            return 4;
        }

        @Override
        public Object getValueAt(final int row, final int column) {
            final ScriptSource source = sources.get(row);
            switch (column) {
                case 0:
                    return source.name();
                case 1:
                    return source.authors();
                case 2:
                    return source.version();
                case 3:
                    return source.description();
            }

            return null;
        }

        @Override
        public String getColumnName(final int column) {
            switch (column) {
                case 0:
                    return "Name";
                case 1:
                    return "Authors";
                case 2:
                    return "Version";
                case 3:
                    return "Description";
            }

            return null;
        }

    }

    private void startScript(final GameDefinition definition) {
        final ScriptSource source = sources.get(table.getSelectedRow());
        if (source == null || source.name().equals("none")) return;

        final GameScript script = source.getScript();
        if (script != null) {
            definition.scriptHandler.setScript(script);
            dispose();
        } else {
            System.err.println("[ScriptSelector] - Script source failed to load.");
        }
    }

    private void loadLocalScripts() throws Exception {
        ClassLoader loader = null;
        try {
            loader = new LocalClassLoader(ROOT.getCanonicalFile().toURI().toURL());
        } catch (final Exception e) {
            e.printStackTrace();
        }

        if (loader == null) {
            System.err.println("[ScriptSelector] - Failed to create local script loader");
            return;
        }

        final List<String> files = new ArrayList<String>();
        getFiles(ROOT, files);

        for (String file : files) {
            final String ext = ".class";
            System.out.println("file:" + file);
            if (file.endsWith(ext)) {
                try {
                    if (file.endsWith(ext) && !file.startsWith(".") && !file.contains("!")) {
                        file = file.substring(1, file.length() - ext.length()).replaceAll("\\\\", "/");
                        final Class<?> clazz = loader.loadClass(file);
                        if (clazz != null)
                            if (clazz.getSuperclass() != null && GameScript.class.isAssignableFrom(clazz) && clazz.getAnnotation(ScriptManifest.class) != null)
                                cache.add(new LocalScript(clazz.asSubclass(GameScript.class)));
                    }
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void loadSdnScripts() {
        if (Environment.offline) return;

        try {
            final URL url = new URL("https://octobot.org/sdn/bot/getscripts.php?u=1");
//                    + TextHandler.decode(User.user.name).replace(" ", "%20")
//                    + "&p="
//                    + TextHandler.decode(User.user.password));

            InputStreamReader inputStreamReader;
            try {
                inputStreamReader = new InputStreamReader(HttpClient.getHttpsInputStream(url));
            } catch (final Exception e) {
                System.out.println("[ScriptSelector] - Failed to make connection to the sdn.");
                return;
            }

            final BufferedReader reader = new BufferedReader(inputStreamReader);

            String read;
            while (reader.readLine() != null) {
                final String id = reader.readLine();
                final String name = reader.readLine();

                for (final ScriptSource source : cache)
                    if (source.name().equals(name)) continue;

                final String version = reader.readLine();
                final String desc = reader.readLine();
                final String authors = reader.readLine();
                cache.add(new SDNScript(Integer.parseInt(id), name, desc, Double.parseDouble(version), authors));
            }
        } catch (final Exception e) {
            e.printStackTrace();
            System.err.println("[ScriptSelector] - Failed to load SDN scripts: " + e);
        }
    }

    private void getFiles(final File current, final List<String> files) throws Exception {
        final File[] list = current.listFiles();
        if (list == null) return;

        for (final File file : list)
            if (file.isDirectory()) {
                getFiles(file, files);
            } else {
                final String name = file.getName();
                if (name.endsWith(".class")){
                    files.add(file.getAbsolutePath().replace(ROOT.getAbsolutePath(), ""));
                } else if (name.endsWith(".jar")) {
                    final JarInputStream jarFile = new JarInputStream(new FileInputStream(file.getAbsolutePath()));
                    JarEntry jarEntry;
                    while ((jarEntry = jarFile.getNextJarEntry()) != null) {
                        if (jarEntry.getName().endsWith(".class"))
                            files.add("/" + name + "/" + jarEntry.getName());
                    }
                }
            }
    }

}
