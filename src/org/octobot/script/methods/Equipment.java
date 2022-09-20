package org.octobot.script.methods;

import org.octobot.bot.game.client.RSItemContainer;
import org.octobot.bot.game.client.RSNode;
import org.octobot.script.ContextProvider;
import org.octobot.script.ScriptContext;
import org.octobot.script.collection.Filter;
import org.octobot.script.wrappers.Component;
import org.octobot.script.wrappers.Item;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Equipment
 *
 * @author Pat-ji
 */
public class Equipment extends ContextProvider {

    public Equipment(final ScriptContext context) {
        super(context);
    }

    /**
     * Slot
     *
     * @author Pat-ji
     */
    public enum Slot {
        HELM(12, 0),
        CAPE(13, 1),
        AMULET(14, 2),
        ARROWS(15, 13),
        WEAPON(16, 3),
        BODY(17, 4),
        SHIELD(18, 5),
        LEGS(19, 7),
        HANDS(21, 9),
        FEET(20, 10),
        RING(22, 12);

        public static final Slot[] values = values();

        private final int widget, index;

        private Slot(final int widget, final int index) {
            this.widget = widget;
            this.index = index;
        }

        public int getWidget() {
            return widget;
        }

        public int getIndex() {
            return index;
        }

    }

    /**
     * EquipmentItem
     *
     * @author Pat-ji
     */
    public final class EquipmentItem extends Item {
        private final Component component;

        public EquipmentItem(final Slot slot, final Component component) {
            super(context(), slot.ordinal(), component);

            this.component = component;
        }

        @Override
        public Component getComponent() {
            return component.getChild(0);
        }

        @Override
        public int getId() {
            return component.getChild(1).getComponentId();
        }

        @Override
        public int getStackSize() {
            return component.getChild(1).getItemStackSize();
        }

        @Override
        public Point getCentralPoint() {
            return getComponent().getCentralPoint();
        }

    }

    /**
     * EquipmentInfo
     *
     * @author Pat-ji
     */
    public final class EquipmentInfo {
        public final int id, amount;

        public EquipmentInfo(final int id, final int amount) {
            this.id = id;
            this.amount = amount;
        }

    }

    /**
     * This method is used to check if the {@link Equipment} contains an {@link Item} with a given id
     *
     * @param ids the ids of {@link Item} to check
     * @return <code>true</code> if the {@link Equipment} contains an {@link Item} with a given id
     */
    public boolean contains(final int... ids) {
        return hasOneOf(ids);
    }

    /**
     * This method is used to check if the {@link Equipment} contains an {@link Item} with a given name
     *
     * @param names the names of {@link Item} to check
     * @return <code>true</code> if the {@link Equipment} contains an {@link Item} with a given name
     */
    public boolean contains(final String... names) {
        return getItem(names) != null;
    }

    /**
     * This method is used to get the {@link Item} in a given {@link Slot}
     *
     * @param slot the {@link Slot} to get the {@link Item} from
     * @return the {@link Item} in a given {@link Slot}
     */
    public Item getItem(final Slot slot) {
        final Component component = context().widgets.getComponent(387, slot.ordinal() + 6);
        return component == null || component.getChild(1).isHidden() ? null : new EquipmentItem(slot, component);
    }

    /**
     * This method is used to get an {@link Item} with a given id
     *
     * @param ids the ids to get the {@link Item} item with
     * @return an {@link Item} with a given id
     */
    public Item getItem(final int... ids) {
        return getItem(new Filter<Item>() {
            @Override
            public boolean accept(final Item item) {
                return context().calculations.arrayContains(ids, item.getId());
            }
        });
    }

    /**
     * This method is used to get an {@link Item} with a given name
     *
     * @param names the names to get the {@link Item} item with
     * @return an {@link Item} with a given name
     */
    public Item getItem(final String... names) {
        return getItem(new Filter<Item>() {
            @Override
            public boolean accept(final Item item) {
                return context().calculations.arrayContains(names, item.getName());
            }
        });
    }

    /**
     * This method is used to get an {@link Item} with a given {@link Filter}
     *
     * @param filter the {@link Filter} to get the {@link Item} item with
     * @return an {@link Item} with a given {@link Filter}
     */
    public Item getItem(final Filter<Item> filter) {
        for (final Item item : getItems())
            if (filter.accept(item)) return item;

        return null;
    }

    /**
     * This method is used to get all the {@link Item}s in the {@link Equipment}
     *
     * @return all the {@link Item}s in the {@link Equipment}
     */
    public Item[] getItems() {
        final List<Item> result = new ArrayList<Item>();
        for (final Slot slot : Slot.values) {
            final Item item = getItem(slot);
            if (item != null)
                result.add(item);
        }

        return result.toArray(new Item[result.size()]);
    }

    /**
     * This method is used to check if the {@link Equipment} contains an id
     *
     * @param ids the ids to check for
     * @return <code>true</code> if the {@link Equipment} contains an id
     */
    public boolean hasOneOf(final int... ids) {
        final Equipment.EquipmentInfo[] infos = context().equipment.getInfo();
        if (infos != null)
            for (final EquipmentInfo info : infos)
                if (context().calculations.arrayContains(ids, info.id))
                    return true;

        return false;
    }

    /**
     * This method is used to get all the {@link EquipmentInfo}s in the {@link Equipment}
     *
     * @return all the {@link EquipmentInfo}s in the {@link Equipment}
     */
    public EquipmentInfo[] getInfo() {
        final RSNode node = context().client.getItemContainerTable().getBuckets()[30].getNext();
        if (node instanceof RSItemContainer) {
            final RSItemContainer container = (RSItemContainer) node;
            final int[] ids = container.getIds();
            final int[] amounts = container.getQuantities();
            final EquipmentInfo[] infos = new EquipmentInfo[ids.length];
            for (int i = 0; i < infos.length; i++)
                infos[i] = new EquipmentInfo(ids[i], amounts[i]);

            return infos;
        }

        return null;
    }

}
