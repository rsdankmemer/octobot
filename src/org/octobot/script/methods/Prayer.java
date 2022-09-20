package org.octobot.script.methods;

import org.octobot.script.Condition;
import org.octobot.script.ContextProvider;
import org.octobot.script.ScriptContext;
import org.octobot.script.util.Time;
import org.octobot.script.wrappers.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Prayer
 *
 * @author Pat-ji
 */
public class Prayer extends ContextProvider {

    public Prayer(final ScriptContext context) {
        super(context);
    }

    /**
     * Effect
     *
     * @author Pat-ji
     */
    public enum Effect {
        THICK_SKIN(5, 0, 1),
        BURST_OF_STRENGTH(7, 1, 4),
        CLARITY_OF_THOUGHT(9, 2, 7),
        SHARP_EYE(11, 18, 8),
        MYSTIC_WILL(13, 19, 9),
        ROCK_SKIN(15, 3, 10),
        SUPERHUMAN_STRENGTH(17, 4, 13),
        IMPROVED_REFLEXES(19, 5, 16),
        RAPID_RESTORE(21, 6, 19),
        RAPID_HEAL(23, 7, 22),
        PROTECT_ITEM(25, 8, 25),
        HAWK_EYE(27, 20, 26),
        MYSTIC_LORE(29, 21, 27),
        STEEL_SKIN(31, 9, 28),
        ULTIMATE_STRENGTH(33, 10, 31),
        INCREDIBLE_REFLEXES(35, 11, 34),
        PROTECT_FROM_MAGIC(37, 12, 37),
        PROTECT_FROM_MISSILES(39, 13, 40),
        PROTECT_FROM_MELEE(41, 14, 43),
        EAGLE_EYE(43, 22, 44),
        MYSTIC_MIGHT(45, 23, 45),
        RETRIBUTION(47, 15, 46),
        REDEMPTION(49, 16, 49),
        SMITE(51, 17, 52),
        CHIVALRY(53, 24, 60),
        PIETY(55, 25, 70);

        public static final Effect[] values = values();

        private final int component, shift, level;

        private Effect(final int component, final int shift, final int level) {
            this.component = component;
            this.shift = shift;
            this.level = level;
        }

        public int getComponent() {
            return component;
        }

        public int getShift() {
            return shift;
        }

        public int getRequiredLevel() {
            return level;
        }

    }

    /**
     * This method is used to get the {@link Component} of an {@link Effect}
     *
     * @param effect the {@link Effect} to get the {@link Component} from
     * @return the {@link Component} of an {@link Effect}
     */
    public Component getComponent(final Effect effect) {
        return context().widgets.getComponent(271, effect.getComponent());
    }

    /**
     * This method is used to check if an {@link Effect} is active
     *
     * @param effect the {@link Effect} to check
     * @return <code>true</code> if the {@link Effect} is active
     */
    public boolean isActive(final Effect effect) {
        return ((context().settings.get(83) >> effect.getShift()) & 0x01) == 1;
    }

    /**
     * This method is used to get all active {@link Effect}s
     *
     * @return an array with all active {@link Effect}s
     */
    public Effect[] getActiveEffects() {
        final List<Effect> effects = new ArrayList<Effect>();
        for (final Effect effect : Effect.values)
            if (isActive(effect))
                effects.add(effect);

        return effects.toArray(new Effect[effects.size()]);
    }

    /**
     * This method is used to check if the level requirement for an {@link Effect} is reached
     *
     * @param effect the {@link Effect} to check
     * @return <code>true</code> if the level requirement for the {@link Effect} is reached
     */
    public boolean canActivate(final Effect effect) {
        return context().skills.getRealLevel(Skills.Skill.PRAYER) >= effect.getRequiredLevel();
    }

    /**
     * This method is used to get the current prayer points
     *
     * @return the current prayer points
     */
    public int getPrayerPoints() {
        return context().skills.getBoostedLevel(Skills.Skill.PRAYER);
    }

    /**
     * This method is used to get the current prayer percentage
     *
     * @return the current prayer percentage
     */
    public int getPrayerPercentage() {
        return (int) ((getPrayerPoints() * 100.0) / context().skills.getRealLevel(Skills.Skill.PRAYER));
    }

    /**
     * This method is used to activate or deactivate an {@link Effect}
     *
     * @param effect the {@link Effect} to set
     * @param active use <code>true</code> if the {@link Effect} should be active
     * @return <code>true</code> if the {@link Effect} has successfully been set to active boolean
     */
    public boolean setEffect(final Effect effect, final boolean active) {
        if (isActive(effect) == active) return true;

        if (context().tabs.isOpen(Tabs.Tab.PRAYER)) {
            final Component component = getComponent(effect);
            if (component != null && component.interact(active ? "Activate" : "Deactivate"))
                Time.sleep(new Condition() {
                    @Override
                    public boolean validate() {
                        return isActive(effect) != active;
                    }
                }, 1500);
        } else if (context().tabs.open(Tabs.Tab.PRAYER)) {
            Time.sleep(300, 500);
            return setEffect(effect, active);
        }

        return isActive(effect) == active;
    }

}
