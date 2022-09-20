package org.octobot.script.methods;

import org.octobot.script.Condition;
import org.octobot.script.ContextProvider;
import org.octobot.script.ScriptContext;
import org.octobot.script.util.Random;
import org.octobot.script.util.Time;
import org.octobot.script.util.Timer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Menu
 *
 * @author Pat-ji
 */
public class Menu extends ContextProvider {
    private static final Pattern TAG = Pattern.compile("(^[^<]+>|<[^>]+>|<[^>]+$)");

    /**
     * Type
     *
     * @author Pat-ji
     */
    public enum Type {
        ACTION, OPTION

    }

    public Menu(final ScriptContext context) {
        super(context);
    }

    /**
     * This method is used to get the {@link Menu}s option count
     *
     * @return the {@link Menu}s option count
     */
    public int getOptionCount() {
        return context().client.getOptionCount();
    }

    /**
     * This method is used to check if the {@link Menu} is open
     *
     * @return <code>true</code> if the {@link Menu} is open
     */
    public boolean isOpen() {
        return context().client.isMenuOpen();
    }

    /**
     * This method is used to get all the actions in the {@link Menu}
     *
     * @return an array with all the actions in the {@link Menu}
     */
    public String[] getActions() {
        final List<String> result = new ArrayList<String>();

        final String[] actions = context().client.getMenuActions();
        if (actions == null) return null;

        for (int i = getOptionCount() - 1; i >= 0; i--) {
            String action = TAG.matcher(actions[i]).replaceAll("");
            if (action.endsWith(" "))
                action = action.substring(0, action.length() - 1);

            result.add(action);
        }

        return result.toArray(new String[result.size()]);
    }

    /**
     * This method is used to get all the options in the {@link Menu}
     *
     * @return an array with all the options in the {@link Menu}
     */
    public String[] getOptions() {
        final List<String> result = new ArrayList<String>();

        final String[] options = context().client.getMenuOptions();
        if (options == null) return null;

        for (int i = getOptionCount() - 1; i >= 0; i--) {
            String option = TAG.matcher(options[i]).replaceAll("");
            if (option.endsWith(" "))
                option = option.substring(0, option.length() - 1);

            result.add(option);
        }

        return result.toArray(new String[result.size()]);
    }

    /**
     * This method is used to get the {@link Menu}s x position
     *
     * @return the {@link Menu}s x position
     */
    public int getX() {
        return context().client.getMenuX();
    }

    /**
     * This method is used to get the {@link Menu}s y position
     *
     * @return the {@link Menu}s y position
     */
    public int getY() {
        return context().client.getMenuY();
    }

    /**
     * This method is used to get the {@link Menu}s width
     *
     * @return the {@link Menu}s width
     */
    public int getWidth() {
        return context().client.getMenuWidth();
    }

    /**
     * This method is used to get the {@link Menu}s height
     *
     * @return the {@link Menu}s height
     */
    public int getHeight() {
        return context().client.getMenuHeight();
    }

    /**
     * This method is used to close the {@link Menu}
     *
     * @return <code>true</code> if the {@link Menu} is not open
     */
    public boolean close() {
        if (!isOpen()) return true;

        if (Random.nextInt(0, 70) > 30) {
            int x = context().mouse.getX();
            int y = context().mouse.getY();
            if (x < 100) {
                x = Random.nextInt(580, 620);
            } else if (x > 600) {
                x = Random.nextInt(0, 20);
            }

            if (y > 400) {
                y = Random.nextInt(0, 20);
            } else if (y < 100) {
                y = Random.nextInt(380, 420);
            }

            if (context().mouse.move(x, y))
                Time.sleep(new Condition() {
                    @Override
                    public boolean validate() {
                        return isOpen();
                    }
                }, 1500);
        } else if (interact("Cancel")) {
            Time.sleep(new Condition() {
                @Override
                public boolean validate() {
                    return isOpen();
                }
            }, 1500);
        }

        return !isOpen();
    }

    /**
     * This method is used to get the index of an action and option in the {@link Menu}
     *
     * @param action the action to get the index from
     * @param option the option to get the index from
     * @return the index of an action and option in the {@link Menu}
     */
    public int getIndex(final String action, final String option) {
        final String[] actions = getActions();
        final String[] options = getOptions();
        for (int i = 0; i < Math.min(actions.length, options.length); i++)
            if ((action.equals("") || actions[i].equals(action)) && (option.equals("") || options[i].contains(option))) return i;

        return -1;
    }

    /**
     * This method is used to check if the {@link Menu} contains an action
     *
     * @param action the action to check for
     * @return <code>true</code> if the {@link Menu} contains the action
     */
    public boolean contains(final String action) {
        return contains(action, "");
    }

    /**
     * This method is used to check if the {@link Menu} contains an action and option
     *
     * @param action the action to check for
     * @param option the option to check for
     * @return <code>true</code> if the {@link Menu} contains the action and option
     */
    public boolean contains(final String action, final String option) {
        return getIndex(action, option) >= 0;
    }

    /**
     * This method is used to check if the {@link Menu} contains an action or option
     *
     * @param text the action or option to check for
     * @param type the {@link Type} of the text
     * @return <code>true</code> if the {@link Menu} contains the action or option
     */
    public boolean contains(final String text, final Type type) {
        return type.equals(Type.ACTION) ? contains(text, "") : contains("", text);
    }

    /**
     * This method is used to interact with an action in the {@link Menu}
     *
     * @param action the action to interact with
     * @return <code>true</code> if successfully interacted with the action
     */
    public boolean interact(final String action) {
        return interact(action, "");
    }

    /**
     * This method is used to interact with an action and option in the {@link Menu}
     *
     * @param action the action to interact with
     * @param option the option to interact with
     * @return <code>true</code> if successfully interacted with the action and option
     */
    public boolean interact(final String action, final String option) {
        return interact(action, option, true);
    }

    /**
     * This method is used to interact with an action and option in the {@link Menu}
     *
     * @param action the action to interact with
     * @param option the option to interact with
     * @param crosshair use <code>true</code> if the return should check for the cross hair as well
     * @return <code>true</code> if successfully interacted with the action and option
     */
    public boolean interact(final String action, final String option, final boolean crosshair) {
        int index = getIndex(action, option);
        if (!isOpen()) {
            if (index == -1) return false;

            if (index == 0) {
                return context().mouse.click(true) && (!crosshair || context().mouse.getCurrentCrosshair() == 2);
            } else {
                if (context().mouse.click(false))
                    Time.sleep(new Condition() {
                        @Override
                        public boolean validate() {
                            return !isOpen();
                        }
                    }, 1000);

                index = getIndex(action, option);
                return index != -1 && clickIndex(index, crosshair);
            }
        } else if (!contains(action, option)) {
            final Timer timer = new Timer(2000);
            do {
                close();
            } while (isOpen() && timer.isRunning());

            return false;
        }

        return clickIndex(index, crosshair);
    }

    /**
     * This method is used to click an index in the {@link Menu}
     *
     * @param index the index to click
     * @param crosshair use <code>true</code> if the return should check for the cross hair as well
     * @return <code>true</code> if successfully clicked the index
     */
    public boolean clickIndex(final int index, final boolean crosshair) {
        return context().mouse.click((getX() + getWidth() / 2) + Random.nextInt(-15, 15), getY() + Random.nextInt(25, 30) + index * 15, true)
                && (!crosshair || context().mouse.getCurrentCrosshair() == 2);
    }

}
