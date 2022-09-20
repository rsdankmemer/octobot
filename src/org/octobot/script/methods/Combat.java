package org.octobot.script.methods;

import org.octobot.script.Condition;
import org.octobot.script.ContextProvider;
import org.octobot.script.ScriptContext;
import org.octobot.script.util.Time;
import org.octobot.script.wrappers.Component;

/**
 * Combat
 *
 * @author Pat-ji
 */
public class Combat extends ContextProvider {

    public Combat(final ScriptContext context) {
        super(context);
    }

    /**
     * This method is used to get the special attack energy
     *
     * @return the special attack energy
     */
    public int getSpecialAttackEnergy() {
        return context().settings.get(300);
    }

    /**
     * This method is used to check if special attack is enabled
     *
     * @return <code>true</code> if special attack is enabled
     */
    public boolean isSpecialAttackEnabled() {
        return context().settings.get(301) == 1;
    }

    /**
     * This method is used to get the time left to be poisoned
     *
     * @return the time left to be poisoned
     */
    public int getPoisonTime() {
        return context().settings.get(102);
    }

    /**
     * This method checks if the time left to be poisoned is greater then zero
     *
     * @return <code>true</code> if the time left to be poisoned is greater then zero
     */
    public boolean isPoisoned() {
        return getPoisonTime() > 0;
    }

    /**
     * This method is used to check if auto retaliate is enabled
     *
     * @return <code>true</code> if auto retaliate is enabled
     */
    public boolean isAutoRetaliateOn() {
        return context().settings.get(172) == 1;
    }

    /**
     * This method is used to get the health percentage
     *
     * @return the health percentage
     */
    public int getHealthPercentage() {
        return (int) ((getHealth() * 100.0) / context().skills.getRealLevel(Skills.Skill.HEALTH));
    }

    /**
     * This method is used to get the current health
     *
     * @return the current health
     */
    public int getHealth() {
        return context().skills.getBoostedLevel(Skills.Skill.HEALTH);
    }

    /**
     * This method is used to get the current fight mode
     *
     * @return get the current fight mode
     */
    public int getFightMode() {
        return context().settings.get(43);
    }

    /**
     * This method is used to set auto retaliate enabled or disabled
     *
     * @param on <code>true</code> to enable auto retaliate, <code>false</code> to disable
     * @return <code>true</code> is auto retaliate enabled is equal to on
     */
    public boolean setAutoRetaliate(final boolean on) {
        if (isAutoRetaliateOn() == on) return true;

        if (context().tabs.isOpen(Tabs.Tab.COMBAT)) {
            final Component component = context().widgets.getComponent(593, 27);
            if (component != null && component.click(true))
                Time.sleep(new Condition() {
                    @Override
                    public boolean validate() {
                        return isAutoRetaliateOn() != on;
                    }
                }, 1500);
        } else if (context().tabs.open(Tabs.Tab.COMBAT)) {
            return setAutoRetaliate(on);
        }

        return isAutoRetaliateOn() == on;
    }

    /**
     * This method is used to set the fight mode
     *
     * @param mode the fight mode to set to
     * @return <code>true</code> if the fight mode has successfully been set to mode
     */
    public boolean setFightMode(final int mode) {
        if (getFightMode() == mode) return true;

        if (context().tabs.isOpen(Tabs.Tab.COMBAT)) {
            final Component component = context().widgets.getComponent(593, 5 + mode * 4);
            if (component != null && component.click(true))
                Time.sleep(new Condition() {
                    @Override
                    public boolean validate() {
                        return getFightMode() != mode;
                    }
                }, 1500);
        } else if (context().tabs.open(Tabs.Tab.COMBAT)) {
            return setFightMode(mode);
        }

        return getFightMode() == mode;
    }

}
