package org.octobot.bot.internal;

/**
 * Reward
 *
 * @author Pat-ji
 */
public enum Reward {
    ATTACK("Attack", 27, 0x1),
    DEFENCE("Defence", 31, 0x5),
    STRENGTH("Strength", 28, 0x2),
    HEALTH("Health", 32, 0x6),
    RANGED("Ranged", 29, 0x3),
    PRAYER("Prayer", 33, 0x7),
    MAGIC("Magic", 30, 0x4),
    COOKING("Cooking", 42, 0x10),
    WOODCUTTING("Woodcutting", 44, 0x12),
    FLETCHING("Fletching", 45, 0x13),
    FISHING("Fishing", 41, 0xF),
    FIREMAKING("Firemaking", 43, 0x11),
    CRAFTING("Crafting", 37, 0xB),
    SMITHING("Smithing", 40, 0xE),
    MINING("Mining", 39, 0xD),
    HERBLORE("Herblore", 35, 0x9),
    AGILITY("Agility", 34, 0x8),
    THIEVING("Thieving", 36, 0xA),
    SLAYER("Slayer", 46, 0x14),
    FARMING("Farming", 47, 0x15),
    RUNECRAFTING("Runecrafting", 38, 0xC),
    HUNTER("Hunter", 49, 0x17),
    CONSTRUCTION("Construction", 48, 0x18);

    public static final Reward[] values = values();

    private final String name;
    private final int widget, setting;

    private Reward(final String name, final int widget, final int setting) {
        this.name = name;
        this.widget = widget;
        this.setting = setting;
    }

    public String getName() {
        return name;
    }

    public int getWidget() {
        return widget;
    }

    public int getSetting() {
        return setting;
    }

    public static Reward get(final String name) {
        for (final Reward reward : Reward.values)
            if (name.equals(reward.getName())) return reward;

        return null;
    }

}