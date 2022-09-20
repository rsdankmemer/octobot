package org.octobot.script.methods;

import org.octobot.script.Condition;
import org.octobot.script.ContextProvider;
import org.octobot.script.ScriptContext;
import org.octobot.script.util.Time;
import org.octobot.script.wrappers.Component;
import org.octobot.script.wrappers.Widget;

import java.awt.event.KeyEvent;

/**
 * Tabs
 *
 * @author Pat-ji
 */
public class Tabs extends ContextProvider {

    /**
     * Tab
     *
     * @author Pat-ji
     */
    public static enum Tab {
        COMBAT("Combat Options", 44, KeyEvent.VK_F1),
        SKILLS("Stats", 45, KeyEvent.VK_F2),
        QUEST("Quest List", 46, KeyEvent.VK_F3),
        INVENTORY("Inventory", 47, KeyEvent.VK_ESCAPE),
        EQUIPMENT("Worn Equipment", 48, KeyEvent.VK_F4),
        PRAYER("Prayer", 49, KeyEvent.VK_F5),
        MAGIC("Magic", 50, KeyEvent.VK_F6),
        CLAN_CHAT("Clan Chat", 27, KeyEvent.VK_F7),
        FRIEND_LIST("Friends List", 28, KeyEvent.VK_F8),
        IGNORE_LIST("Ignore List", 29, KeyEvent.VK_F9),
        LOGOUT("Logout", 30, -1),
        SETTINGS("Options", 31, KeyEvent.VK_F10),
        EMOTES("Emotes", 32, KeyEvent.VK_F11),
        MUSIC("Music Player", 33, KeyEvent.VK_F12);

        public static final Tab[] values = values();

        private final String name;
        private final int index, keyCode;

        private Tab(final String name, final int index, final int keyCode) {
            this.name = name;
            this.index = index;
            this.keyCode = keyCode;
        }

        public String getName() {
            return name;
        }

        public int getIndex() {
            return index;
        }

        public int getKeyCode() {
            return keyCode;
        }

    }

    public Tabs(final ScriptContext context) {
        super(context);
    }

    /**
     * This method is used to open a {@link Tab}
     *
     * @param tab the {@link Tab} to open
     * @return <code>true</code> if the {@link Tab} has successfully been opened
     */
    public boolean open(final Tab tab) {
        return open(tab, false);
    }

    /**
     * This method is used to open a {@link Tab}
     *
     * @param tab the {@link Tab} to open
     * @param key <code>true</code> if the {@link Tab}s corresponding key should be clicked instead of the tab interface
     * @return <code>true</code> if the {@link Tab} has successfully been opened
     */
    public boolean open(final Tab tab, final boolean key) {
        if (isOpen(tab)) return true;

        if (key && tab.getKeyCode() != -1) {
            context().keyboard.pressKey((char) tab.getKeyCode());
            Time.sleep(10, 20);
            context().keyboard.releaseKey((char) tab.getKeyCode());
        } else {
            final Component component = getComponent(tab);
            if (component != null)
                component.click(true);
        }

        Time.sleep(new Condition() {
            @Override
            public boolean validate() {
                return !isOpen(tab);
            }
        }, 1000);

        return isOpen(tab);
    }

    /**
     * This method is used to check if a {@link Tab} is open
     *
     * @param tab the {@link Tab} to check
     * @return <code>true</code> if the {@link Tab} is open
     */
    public boolean isOpen(final Tab tab) {
        return getCurrent().equals(tab);
    }

    /**
     * This method is used to get the {@link Component} of a {@link Tab}
     *
     * @param tab the {@link Tab} to get the {@link Component} from
     * @return the {@link Component} of the {@link Tab}
     */
    public Component getComponent(final Tab tab) {
        return context().widgets.getComponent(548, tab.getIndex());
    }

    /**
     * This method is used to get the currently opened {@link Tab}
     *
     * @return the currently opened {@link Tab}
     */
    public Tab getCurrent() {
        final Widget widget;
        if ((widget = context().widgets.get(548)) != null)
            for (final Component component : widget.getChildren()) {
                String[] actions;
                if (component.getTextureId() != -1 && (actions = component.getActions()) != null)
                    for (final Tab tab : Tab.values)
                        for (final String action : actions)
                            if (tab.getName().equals(action))  return tab;
            }

        return null;
    }

}
