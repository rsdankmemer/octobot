//package org.octobot.bot.internal.ui.component;
//
//import org.octobot.bot.GameDefinition;
//import org.octobot.bot.event.render.*;
//import org.octobot.bot.game.script.GameScript;
//import org.octobot.bot.game.script.ScriptManifest;
//import org.octobot.bot.internal.ui.*;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.net.URI;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//
///**
//* Created by Joseph on 12/20/2014.
//*/
//public class WebMenuBar extends JMenuBar {
//
//    public MenuBar() {
//
//        add(new Menu(MenuHolder.FILE, "Save Nodes", "Load Nodes"));
//        add(new Menu(MenuHolder.MODE, "Selection", "Creation", "Connection"));
//    }
//
//    /**
//     * MenuHolder
//     *
//     * @author Pat-ji
//     */
//    private enum MenuHolder {
//        FILE, MODE, DATA;
//
//        @Override
//        public String toString() {
//            final String name = name().toLowerCase();
//            return name.substring(0, 1).toUpperCase() + name.substring(1);
//        }
//
//    }
//
//    /**
//     * Menu
//     *
//     * @author Pat-ji
//     */
//    private final class Menu extends JMenu implements ActionListener {
//        private final MenuHolder menuHolder;
//
//        public Menu(final MenuHolder menuHolder, final String... items) {
//            super(menuHolder.toString());
//
//            this.menuHolder = menuHolder;
//
//            setBorderPainted(false);
//
//            for (final String item : items) {
//                final JMenuItem menuItem = new JMenuItem(item);
//                menuItem.setBorderPainted(false);
//                menuItem.addActionListener(this);
//                add(menuItem);
//            }
//        }
//
//        private int indexOf(final String item) {
//            int index = 0;
//            for (final Component component : getMenuComponents()) {
//                if (component instanceof JMenuItem && item.equals(((JMenuItem) component).getText()))
//                    return index;
//
//                index++;
//            }
//
//            return -1;
//        }
//
//        @Override
//        public void actionPerformed(final ActionEvent e) {
//            final GameDefinition definition = ContentPanel.instance.definition;
//            final int index = indexOf(e.getActionCommand());
//            switch (menuHolder) {
//                case SCRIPT:
//                    if (definition == null) return;
//
//                    switch (index) {
//                        case 0:
//                            definition.scriptHandler.paused = false;
//                            if (definition.scriptHandler.getScript() == null)
//                                new ScriptSelectorFrame(definition);
//
//                            break;
//                        case 1:
//                            definition.scriptHandler.paused = true;
//                            break;
//                        case 2:
//                            definition.scriptHandler.stopScript();
//                            break;
//                        case 3:
//                            final GameScript script = definition.scriptHandler.getScript();
//                            if (script != null) {
//                                final ScriptManifest manifest = script.manifest();
//                                if (manifest != null)
//                                    new ScriptInfoFrame(manifest).setVisible(true);
//                            }
//
//                            break;
//                    }
//
//                    break;
//                case DEBUG:
//                    if (definition == null) return;
//
//                    switch (index) {
//                        case 0:
//                            definition.renderEvent("NPCs", new NPCRenderEvent(definition.context));
//                            break;
//                        case 1:
//                            definition.renderEvent("Players", new PlayerRenderEvent(definition.context));
//                            break;
//                        case 2:
//                            definition.renderEvent("GameObjects", new GameObjectRenderEvent(definition.context));
//                            break;
//                        case 3:
//                            definition.renderEvent("GroundItems", new GroundItemRenderEvent(definition.context));
//                            break;
//                        case 4:
//                            definition.renderEvent("Projectiles", new ProjectileRenderEvent(definition.context));
//                            break;
//                        case 5:
//                            definition.renderEvent("Camera", new CameraRenderEvent(definition.context));
//                            break;
//                        case 6:
//                            definition.renderEvent("Items", new ItemRenderEvent(definition.context));
//                            break;
//                        case 7:
//                            definition.renderEvent("Region", new RegionRenderEvent(definition.context));
//                            break;
//                        case 8:
//                            definition.renderEvent("Mouse", new MouseRenderEvent(definition.context));
//                            break;
//                        case 9:
//                            definition.renderEvent("Menu", new MenuRenderEvent(definition.context));
//                            break;
//                        case 10:
//                            definition.renderEvent("Hints", new HintRenderEvent(definition.context));
//                            break;
//                        case 11:
//                            definition.renderEvent("Collision", new CollisionRenderEvent(definition.context));
//                            break;
//                        case 12:
//                            definition.renderEvent("Color", new ColorPickerRenderEvent(definition.context));
//                            break;
//                        case 13:
//                            definition.renderEvent("Navigation", new NavigationRenderEvent(definition.context));
//                            break;
//                    }
//
//                    break;
//                case VIEW:
//                    if (definition == null) return;
//
//                    switch (index) {
//                        case 0:
//                            new SettingsExplorerFrame(definition.context).setVisible(true);
//                            break;
//                        case 1:
//                            if (!definition.renderEvents.containsKey("widgets"))
//                                definition.renderEvent("widgets", new WidgetExplorerFrame(definition.context));
//                            break;
//                        case 2:
//                            new WebExplorerFrame(definition.context).setVisible(true);
//                    }
//
//                    break;
//                case OPTIONS:
//                    switch (index) {
//                        case 0:
//                            new OptionsFrame(definition);
//                            break;
//                        case 1:
//                            if (definition == null) break;
//
//                            final String name = new SimpleDateFormat("'Date'_yyyy_MM_dd_'Time'_HH_mm_ss").format(Calendar.getInstance().getTime());
//                            if (definition.context.environment.takeScreenShot(name))
//                                System.out.println("[MenuBar::Options] - Successfully took a screen shot under the name " + name + ".");
//
//                            break;
//                    }
//
//                    break;
//                case INFO:
//                    switch (index) {
//                        case 0:
//                            VersionFrame.instance.setVisible(true);
//                            break;
//                        case 1:
//                            final Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
//                            if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE))
//                                try {
//                                    desktop.browse(new URI("http://www.octobot.org/forum/"));
//                                } catch (final Exception ignored) { }
//
//                            break;
//                    }
//
//                    break;
//            }
//        }
//
//    }
//
//}
