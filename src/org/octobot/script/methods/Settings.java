package org.octobot.script.methods;

import org.octobot.script.ContextProvider;
import org.octobot.script.ScriptContext;

/**
 * Settings
 *
 * @author Pat-ji
 */
public class Settings extends ContextProvider {

    public Settings(final ScriptContext context) {
        super(context);
    }

    /**
     * This method is used to get all the settings
     *
     * @return an array with all the settings
     */
    public int[] get() {
        return context().client.getSettings();
    }

    /**
     * This method is used to get all the widget settings
     *
     * @return an array with all the widget settings
     */
    public int[] getWidgetSettings() {
        return context().client.getWidgetSettings();
    }

    /**
     * This method is used to get a setting
     *
     * @param index the index of the setting to get
     * @return a setting with a specific index
     */
    public int get(final int index) {
        final int[] settings = get();
        return index < settings.length ? settings[index] : -1;
    }

    /**
     * This method is used to get a setting
     *
     * @param index the index of the setting to get
     * @param mask the bit mask to add
     * @return a setting with a specific index and bit mask
     */
    public int get(final int index, final int mask) {
        return get(index, 0, mask);
    }

    /**
     * This method is used to get a setting
     *
     * @param index the index of the setting to get
     * @param shift the binary shift to add
     * @param mask the bit mask to add
     * @return a setting with a specific index and bit mask, shifted with a specific binary shift
     */
    public int get(final int index, final int shift, final int mask) {
        return (get(index) >> shift) & mask;
    }

    /**
     * This method is used to get a widget setting
     *
     * @param index the index of the setting to get
     * @return a widget setting with a specific index
     */
    public int getWidgetSetting(final int index) {
        final int[] settings = getWidgetSettings();
        return index < settings.length ? settings[index] : -1;
    }

    /**
     * This method is used to get a widget setting
     *
     * @param index the index of the setting to get
     * @param mask the bit mask to add
     * @return a widget setting with a specific index and bit mask
     */
    public int getWidgetSetting(final int index, final int mask) {
        return getWidgetSetting(index, 0, mask);
    }

    /**
     * This method is used to get a widget setting
     *
     * @param index the index of the setting to get
     * @param shift the binary shift to add
     * @param mask the bit mask to add
     * @return a widget setting with a specific index and bit mask, shifted with a specific binary shift
     */
    public int getWidgetSetting(final int index, final int shift, final int mask) {
        return (getWidgetSetting(index) >> shift) & mask;
    }

}
