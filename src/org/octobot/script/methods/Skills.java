package org.octobot.script.methods;

import org.octobot.script.ContextProvider;
import org.octobot.script.ScriptContext;

/**
 * Skills
 *
 * @author Pat-ji
 */
public class Skills extends ContextProvider {
    public final int[] XP_TABLE = {0, 0, 83, 174, 276, 388, 512, 650, 801, 969, 1154, 1358, 1584, 1833, 2107,
            2411, 2746, 3115, 3523, 3973, 4470, 5018, 5624, 6291, 7028, 7842, 8740, 9730, 10824, 12031, 13363, 14833,
            16456, 18247, 20224, 22406, 24815, 27473, 30408, 33648, 37224, 41171, 45529, 50339, 55649, 61512, 67983,
            75127, 83014, 91721, 101333, 111945, 123660, 136594, 150872, 166636, 184040, 203254, 224466, 247886, 273742,
            302288, 333804, 368599, 407015, 449428, 496254, 547953, 605032, 668051, 737627, 814445, 899257, 992895,
            1096278, 1210421, 1336443, 1475581, 1629200, 1798808, 1986068, 2192818, 2421087, 2673114, 2951373, 3258594,
            3597792, 3972294, 4385776, 4842295, 5346332, 5902831, 6517253, 7195629, 7944614, 8771558, 9684577, 10692629,
            11805606, 13034431};

    /**
     * Skill
     *
     * @author Pat-ji
     */
    public static enum Skill {
        ATTACK, DEFENCE, STRENGTH, HEALTH, RANGED, PRAYER, MAGIC, COOKING, WOODCUTTING,
        FLETCHING, FISHING, FIREMAKING, CRAFTING, SMITHING, MINING, HERBLORE,
        AGILITY, THIEVING, SLAYER, FARMING, RUNECRAFTING, HUNTER, CONSTRUCTION;

        public static final Skill[] values = values();

    }

    public Skills(final ScriptContext context) {
        super(context);
    }

    /**
     * This method is used to get the real level from a {@link Skill}
     *
     * @param skill the {@link Skill} to get the real level from
     * @return the real level from a {@link Skill}
     */
    public int getRealLevel(final Skill skill) {
        return getRealLevels()[skill.ordinal()];
    }

    /**
     * This method is used to get the real levels
     *
     * @return an array with the real levels
     */
    public int[] getRealLevels() {
        return context().client.getRealLevels();
    }

    /**
     * This method is used to get the boosted level from a {@link Skill}
     *
     * @param skill the {@link Skill} to get the boosted level from
     * @return the boosted level from a {@link Skill}
     */
    public int getBoostedLevel(final Skill skill) {
        return getBoostedLevels()[skill.ordinal()];
    }

    /**
     * This method is used to get the boosted levels
     *
     * @return an array with the boosted levels
     */
    public int[] getBoostedLevels() {
        return context().client.getBoostedLevels();
    }

    /**
     * This method is used to get the experience from a {@link Skill}
     *
     * @param skill the {@link Skill} to get the experience from
     * @return the experience from a {@link Skill}
     */
    public int getExperience(final Skill skill) {
        return getExperience()[skill.ordinal()];
    }

    /**
     * This method is used to get the experiences
     *
     * @return an array with the experiences
     */
    public int[] getExperience() {
        return context().client.getExperience();
    }

    /**
     * This method is used to get the experience to the next level of a {@link Skill}
     *
     * @param skill the {@link Skill} to get the experience to the next level from
     * @return the experience to the next level from a {@link Skill}
     */
    public int getExperienceToLevel(final Skill skill) {
        return getExperienceToLevel(skill, getRealLevel(skill) + 1);
    }

    /**
     * This method is used to get the experience to a level of a {@link Skill}
     *
     * @param skill the {@link Skill} to get the experience to a level from
     * @param level the level to get the experience to
     * @return the experience to the level of a {@link Skill}
     */
    public int getExperienceToLevel(final Skill skill, final int level) {
        return XP_TABLE[level] - getExperience(skill);
    }

    /**
     * This method is used to get the percentage to the next level of a {@link Skill}
     *
     * @param skill the {@link Skill} to get the percentage to the next level from
     * @return the percentage to the next level of a {@link Skill}
     */
    public float getPercentageToLevel(final Skill skill) {
        return (float) getExperience(skill) / (float) XP_TABLE[getRealLevel(skill) + 1] * 100;
    }

}
