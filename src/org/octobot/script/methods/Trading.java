package org.octobot.script.methods;

import org.octobot.script.ContextProvider;
import org.octobot.script.ScriptContext;
import org.octobot.script.wrappers.Actor;
import org.octobot.script.wrappers.Component;
import org.octobot.script.wrappers.Item;
import org.octobot.script.wrappers.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Trading
 *
 * @author Pat-ji
 */
public class Trading extends ContextProvider {
    private TradingItem[] ourOffer;
    private TradingItem[] theirOffer;

    public Trading(final ScriptContext context) {
        super(context);
    }

    /**
     * This method is used to check if the player is in a trade
     *
     * @return <code>true</code> if the player is in a trade
     */
    public boolean isInTrade() {
        return isOnItemSelect() || isOnItemCheck();
    }

    /**
     * This method is used to check if the player is at the item selection
     *
     * @return <code>true</code> if the player is at the item selection
     */
    public boolean isOnItemSelect() {
        return context().widgets.get(335) != null;
    }

    /**
     * This method is used to check if the player is at the item check
     *
     * @return <code>true</code> if the player is at the item check
     */
    public boolean isOnItemCheck() {
        return context().widgets.get(334) != null;
    }

    /**
     * This method is used to get our offer as {@link Item}s
     *
     * @return our offer as {@link Item}s
     */
    public Item[] getOurOffer() {
        final List<TradingItem> items = new ArrayList<TradingItem>();
        final Component container = context().widgets.getComponent(335, 48);
        if (container != null)
            for (int i = 0; i < 28; i++) {
                final Component component = container.getChild(i);
                if (component.getBorderThickness() != 0)
                    items.add(new TradingItem(context(), i, component));
            }

        return items.isEmpty() ? ourOffer : items.toArray(new TradingItem[items.size()]);
    }

    /**
     * This method is used to get their offer as {@link Item}s
     *
     * @return their offer as {@link Item}s
     */
    public Item[] getTheirOffer() {
        final List<TradingItem> items = new ArrayList<TradingItem>();
        final Component container = context().widgets.getComponent(335, 50);
        if (container != null)
            for (int i = 0; i < 28; i++) {
                final Component component = container.getChild(i);
                if (component.getBorderThickness() != 0)
                    items.add(new TradingItem(context(), i, component));
            }

        return items.isEmpty() ? theirOffer : items.toArray(new TradingItem[items.size()]);
    }

    /**
     * This method is used to click the accept button, and is required to cache the items for proper execution
     *
     * @return <code>true</code> if the accept button has been clicked
     */
    public boolean accept() {
        final Component component = isOnItemSelect() ? context().widgets.getComponent(335, 17) : context().widgets.getComponent(334, 35);
        ourOffer = (TradingItem[]) getOurOffer();
        theirOffer = (TradingItem[]) getTheirOffer();
        return component != null && component.click(true);
    }

    /**
     * This method is used to click the decline button
     *
     * @return <code>true</code> if the decline button has been clicked
     */
    public boolean decline() {
        final Component component = isOnItemSelect() ? context().widgets.getComponent(335, 18) : context().widgets.getComponent(334, 36);
        return component != null && component.click(true);
    }

    /**
     * This method is used to get the currently trading {@link Player}
     *
     * @return the currently trading {@link Player}
     */
    public Player getTradingPlayer() {
        Actor actor = context().players.getLocal();
        if (actor != null)
            actor = actor.getInteracting();

        return actor != null && actor instanceof Player ? (Player) actor : null;
    }

    /**
     * This method is used to check if the trading player has accepted
     *
     * @return <code>true</code> if the trading player has accepted
     */
    public boolean hasOtherAccepted() {
        final Component component = isOnItemSelect() ? context().widgets.getComponent(335, 56) : context().widgets.getComponent(334, 33);
        return component != null && "Other player has accepted.".equals(component.getText());
    }

    /**
     * This method is used to check if all items are in the offer
     *
     * @param our <code>true</code> for ours, <code>false</code> for there
     * @param ids the ids of the items to check for
     * @return <code>true</code> if all items are in the offer
     */
    public boolean allInOffer(final boolean our, final int... ids) {
        ourOffer = (TradingItem[])  getOurOffer();
        theirOffer = (TradingItem[]) getTheirOffer();

        final Item[] offer = our ? ourOffer : theirOffer;
        if (offer == null) return false;

        for (final Item check : offer)
            if (!context().calculations.arrayContains(ids, check.getId())) return false;

        return true;
    }

    /**
     * This method is used to check if all items are in the offer
     *
     * @param our <code>true</code> for ours, <code>false</code> for there
     * @param names the names of the items to check for
     * @return <code>true</code> if all items are in the offer
     */
    public boolean allInOffer(final boolean our, final String... names) {
        ourOffer = (TradingItem[]) getOurOffer();
        theirOffer = (TradingItem[]) getTheirOffer();

        final Item[] offer = our ? ourOffer : theirOffer;
        if (offer == null) return false;

        for (final Item check : offer)
            if (!context().calculations.arrayContains(names, check.getName())) return false;

        return true;
    }

    /**
     * TradingItem
     *
     * @author Pat-ji
     */
    private final class TradingItem extends Item {
        private final int id;
        private final String name;

        public TradingItem(final ScriptContext context, final int index, final Component component) {
            super(context, index, component);

            id = super.getId();
            name = super.getName();
        }

        @Override
        public int getId() {
            return id;
        }

        @Override
        public String getName() {
            return name;
        }

    }

}
