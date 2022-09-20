package org.octobot.script.methods;

import org.octobot.script.ContextProvider;
import org.octobot.script.Nameable;
import org.octobot.script.ScriptContext;
import org.octobot.script.wrappers.Component;

/**
 * Magic
 *
 * @author Pat-ji
 */
public class Magic extends ContextProvider {

    public Magic(final ScriptContext context) {
        super(context);
    }

    /**
     * BookType
     *
     * @author Pat-ji
     */
    public enum BookType {
        NORMAL, ANCIENT, LUNAR

    }

    /**
     * Spell
     *
     * @author Pat-ji
     */
    public interface Spell extends Nameable {

        public int getLevel();

        public int getWidget();

        public int getComponent();

    }

    /**
     * Ancient
     *
     * @author Pat-ji
     */
    public enum Ancient implements Spell {
        EDGEVILLE_HOME_TELEPORT("Edgeville Home Teleport", 0, 24),
        SMOKE_RUSH("Smoke Rush", 50, 8),
        SHADOW_RUSH("Shadow Rush", 52, 12),
        PADDEWWA_TELEPORT("Paddewwa Teleport",54, 16),
        BLOOD_RUSH("Blood Rush", 56, 4),
        ICE_RUSH("Ice Rush", 58, 0),
        SENNTISTEN_TELEPORT("Senntisten Teleport", 60, 17),
        SMOKE_BURST("Smoke Burst", 62, 10),
        SHADOW_BURST("Shadow Burst", 64, 14),
        KHARYRLL_TELEPORT("Kharyrll Teleport", 66, 18),
        BLOOD_BURST("Blood Burst", 68, 6),
        ICE_BURST("Ice Burst", 70, 2),
        LASSAR_TELEPORT("Lassar Teleport", 72, 19),
        SMOKE_BLITZ("Smoke Blitz", 74, 9),
        SHADOW_BLITZ("Shadow Blitz", 76, 13),
        DAREEYAK_TELEPORT("Dareeyak Teleport", 78, 20),
        BLOOD_BLITZ("Blood Blitz", 80, 5),
        ICE_BLITZ("Ice Blitz", 82, 1),
        CARRALLANGAR_TELEPORT("Carrallangar Teleport", 84, 21),
        SMOKE_BARRAGE("Smoke Barrage", 86, 11),
        SHADOW_BARRAGE("Shadow Barrage", 88, 15),
        ANNAKAHRL_TELEPORT("Annakarl Teleport", 90, 22),
        BLOOD_BARRAGE("Blood Barrage", 92, 7),
        ICE_BARRAGE("Ice Barrage", 94, 3),
        GHORROCK_TELEPORT("Ghorrock Teleport", 96, 23);

        private final String name;
        private final int level, component;

        private Ancient(final String name, final int level, final int component) {
            this.name = name;
            this.level = level;
            this.component = component;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public int getLevel() {
            return level;
        }

        @Override
        public int getWidget() {
            return 193;
        }

        @Override
        public int getComponent() {
            return component;
        }

    }

    /**
     * Lunar
     *
     * @author Pat-ji
     */
    public enum Lunar implements Spell {
        BARBARIAN_TELEPORT("Barbarian Teleport", 75),
        CURE_OTHER("Cure Other", 68),
        FERTILE_SOIL("Fertile Soil", 83),
        CURE_GROUP("Cure Group", 74),
        NPC_CONTACT("NPC Contact", 67),
        ENERGY_TRANSFER("Energy Transfer", 91),
        MONSTER_EXAMINE("Monster Examine", 66),
        HUMIDIFY("Humidify", 68),
        HUNTER_KIT("Hunter Kit", 71),
        STAT_SPY("Stat Spy", 75),
        DREAM("Dream", 79),
        PLANK_MAKE("Plank Make", 86),
        SPELL_BOOK_SWAP("Spell Book Swap", 96),
        MAGIC_IMBUE("Magic Imbue", 82),
        VENGEANCE("Vengeance", 94),
        BAKE_PIE("Bake Pie", 65),
        LUNAR_HOME_TELEPORT("Lunar Home Teleport", 0),
        FISHING_GUILD_TELEPORT("Fishing Guild Teleport", 85),
        KHAZARD_TELEPORT("Khazard Teleport", 78),
        VENGEANCE_OTHER("Vengeance Other", 93),
        MOONCLAN_TELEPORT("Moonclan Teleport", 69),
        CATHERBY_TELEPORT("Catherby Teleport", 87),
        STRING_JEWELLERY("String Jewellery", 80),
        CURE_ME("Cure Me", 71),
        WATERBIRTH_TELEPORT("Waterbirth Teleport", 72),
        SUPERGLASS_MAKE("Superglass Make", 77),
        BOOST_POTION_SHARE("Boost Potion Share", 84),
        STAT_RESTORE_POT_SHARE("Stat Restore Pot Share", 81),
        ICE_PLATEAU_TELEPORT("Ice Plateau Teleport", 89),
        HEAL_OTHER("Heal Other", 92),
        HEAL_GROUP("Heal Group", 95),
        CURE_PLANT("Cure Plant", 66),
        TELE_GROUP_MOONCLAN("Tele Group Moonclan", 70),
        TELE_GROUP_WATERBIRTH("Tele Group Waterbirth", 73),
        TELE_GROUP_BARBARIAN("Tele Group Barbarian", 76),
        TELE_GROUP_KHAZARD("Tele Group Khazard", 79),
        TELE_GROUP_FISHING_GUILD("Tele Group Fishing Guild", 86),
        TELE_GROUP_CATHERBY("Tele Group Catherby", 88),
        TELE_GROUP_ICE_PLATEAU("Tele Group Ice Plateau", 90);

        private final String name;
        private final int level;

        private Lunar(final String name, final int level) {
            this.name = name;
            this.level = level;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public int getLevel() {
            return level;
        }

        @Override
        public int getWidget() {
            return 430;
        }

        @Override
        public int getComponent() {
            return ordinal();
        }

    }

    /**
     * Normal
     *
     * @author Pat-ji
     */
    public enum Normal implements Spell {
        LUMBRIDGE_HOME_TELEPORT("Lumbridge Home Teleport", 0),
        WIND_STRIKE("Wind Strike", 1),
        CONFUSE("Confuse", 3),
        ENCHANT_CROSSBOW_BOLT("Enchant Crossbow Bolt", 4),
        WATER_STRIKE("Water Strike", 5),
        LVL_1_ENCHANT("Lvl-1 enchant", 7),
        EARTH_STRIKE("Earth Strike", 9),
        WEAKEN("Weaken", 11),
        FIRE_STRIKE("Fire Strike", 13),
        BONES_TO_BANANAS("Bones to Bananas", 15),
        WIND_BOLT("Wind Bolt", 17),
        CURSE("Curse", 19),
        BIND("Bind", 20),
        LOW_LEVEL_ALCHEMY("Low Level Alchemy", 21),
        WATER_BOLT("Water Bolt", 23),
        VARROCK_TELEPORT("Varrock Teleport", 25),
        LVL_2_ENCHANT("Lvl-2 Enchant", 27),
        EARTH_BOLT("Earth Bolt", 29),
        LUMBRIDGE_TELEPORT("Lumbridge Teleport", 31),
        TELEKINETIC_GRAB("Telekinetic Grab", 33),
        FIRE_BOLT("Fire Bolt", 35),
        FALADOR_TELEPORT("Falador Teleport", 37),
        CRUMBLE_UNDEAD("Crumble Undead", 39),
        TELEPORT_TO_HOUSE("Teleport to House", 40),
        WIND_BLAST("Wind Blast", 41),
        SUPERHEAT_ITEM("Superheat Item", 43),
        CAMELOT_TELEPORT("Camelot Teleport", 45),
        WATER_BLAST("Water Blast", 47),
        LVL_3_ENCHANT("Lvl-3 Enchant", 49),
        IBAN_BLAST("Iban Blast", 50),
        SNARE("Snare", 50),
        MAGIC_DART("Magic Dart", 50),
        ARDOUGNE_TELEPORT("Ardougne Teleport", 51),
        EARTH_BLAST("Earth Blast", 53),
        HIGH_LEVEL_ALCHEMY("High Level Alchemy", 55),
        CHARGE_WATER_ORB("Charge Water Orb", 56),
        LVL_4_ENCHANT("Lvl-4 Enchant", 57),
        WATCHTOWER_TELEPORT("Watchtower Teleport", 58),
        FIRE_BLAST("Fire Blast", 59),
        CHARGE_EARTH_ORB("Charge Earth Orb", 60),
        BONES_TO_PEACHES("Bones to Peaches", 60),
        SARADOMIN_STRIKE("Saradomin Strike", 60),
        CLAWS_OF_GUTHIX("Claws of Guthix", 60),
        FLAMES_OF_ZAMORAK("Flames of Zamorak", 60),
        TROLLHEIM_TELEPORT("Trollheim Teleport", 61),
        WIND_WAVE("Wind Wave", 62),
        CHARGE_FIRE_ORB("Charge Fire Orb", 63),
        TELEPORT_TO_APE_ATOLL("Teleport to Ape Atoll", 64),
        WATER_WAVE("Water Wave", 65),
        CHARGE_AIR_ORB("Charge Air Orb", 66),
        VULNERABILITY("Vulnerability", 66),
        LVL_5_ENCHANT("Lvl-5 Enchant", 68),
        EARTH_WAVE("Earth Wave", 70),
        ENFEEBLE("Enfeeble", 73),
        TELEOTHER_LUMBRIDGE("Teleother Lumbridge", 74),
        FIRE_WAVE("Fire Wave", 75),
        ENTANGLE("Entangle", 79),
        STUN("Stun", 80),
        CHARGE("Charge", 80),
        TELEOTHER_FALADOR("Teleother Falador", 82),
        TELE_BLOCK("Tele Block", 85),
        LVL_6_ENCHANT("Lvl-6 Enchant", 87),
        TELEOTHER_CAMELOT("Teleother Camelot", 90);

        private final String name;
        private final int level;

        private Normal(final String name, final int level) {
            this.name = name;
            this.level = level;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public int getLevel() {
            return level;
        }

        @Override
        public int getWidget() {
            return 192;
        }

        @Override
        public int getComponent() {
            return ordinal();
        }

    }

    /**
     * This method is used to get the {@link Component} from a {@link Spell}
     *
     * @param spell the {@link Spell} to get the {@link Component} from
     * @return the {@link Component} from a {@link Spell}
     */
    public Component getComponent(final Spell spell) {
        return context().widgets.getComponent(spell.getWidget(), spell.getComponent());
    }

    /**
     * This method is used to check if a {@link Spell} is selected
     *
     * @param spell the {@link Spell} to check for
     * @return <code>true</code> if the {@link Spell} is selected
     */
    public boolean isSpellSelected(final Spell spell) {
        if (!hasSelectedSpell()) return false;

        final String lastSpellName = context().client.getLastSpellName();
        return lastSpellName != null && lastSpellName.contains(spell.getName());
    }

    /**
     * This method is used to check if there is a selected {@link Spell}
     *
     * @return <code>true</code> if there is a selected {@link Spell}
     */
    public boolean hasSelectedSpell() {
        return context().client.isSpellSelected();
    }

    /**
     * This method is used to get the currently selected {@link Spell}
     *
     * @return the currently selected {@link Spell}
     */
    public Spell getSelectedSpell() {
        for (final Spell spell : Normal.values())
            if (isSpellSelected(spell)) return spell;

        for (final Spell spell : Ancient.values())
            if (isSpellSelected(spell)) return spell;

        for (final Spell spell : Lunar.values())
            if (isSpellSelected(spell)) return spell;

        return null;
    }

    /**
     * This method is used to check if a {@link BookType} is selected
     *
     * @param book the {@link BookType} to check for
     * @return <code>true</code> if the {@link BookType} is selected
     */
    public boolean isBookSelected(final BookType book) {
        return context().settings.get(439) == book.ordinal();
    }

    /**
     * This method is used to check if the level requirement is reached for a {@link Spell}
     *
     * @param spell the {@link Spell} to check the level requirement for
     * @return <code>true</code> if the level requirement is reached for the {@link Spell}
     */
    public boolean hasLevelRequirement(final Spell spell) {
        return context().skills.getBoostedLevel(Skills.Skill.MAGIC) >= spell.getLevel();
    }

}
