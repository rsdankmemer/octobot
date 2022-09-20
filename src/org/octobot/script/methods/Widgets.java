package org.octobot.script.methods;

import org.octobot.bot.game.client.RSWidget;
import org.octobot.script.ContextProvider;
import org.octobot.script.ScriptContext;
import org.octobot.script.collection.Filter;
import org.octobot.script.wrappers.Component;
import org.octobot.script.wrappers.Widget;

import java.util.ArrayList;
import java.util.List;

/**
 * Widgets
 *
 * @author Pat-ji
 */
public class Widgets extends ContextProvider {

    public Widgets(final ScriptContext context) {
        super(context);
    }

    /**
     * This method is used to get the valid widgets flags
     *
     * @return the valid widgets flags
     */
    public boolean[] getValidWidgets() {
        return context().client.getValidWidgets();
    }

    /**
     * This method is used to click on the continue {@link Component}
     *
     * @return <code>true</code> if the continue {@link Component} has successfully been clicked
     */
    public boolean clickContinue() {
        final Component component = getContinue();
        return component != null && component.click(true);
    }

    /**
     * This method is used to get a {@link Widget}
     *
     * @param id the id of the {@link Widget} to get
     * @return a {@link Widget} with a specific id
     */
    public Widget get(final int id) {
        final RSWidget[][] widgets = context().client.getWidgets();
        if (widgets != null && id >= 0 && id < widgets.length)
            return widgets[id] != null && getValidWidgets()[id] ? new Widget(context(), widgets[id], id) : null;

        return null;
    }

    /**
     * This method is used to get a {@link Component}
     *
     * @param widget the {@link Widget} id
     * @param component the {@link Component} id
     * @return a {@link Component} from a {@link Widget}
     */
    public Component getComponent(final int widget, int component) {
        final Widget instance = get(widget);
        return instance != null ? instance.getChild(component) : null;
    }

    /**
     * This method is used to get a {@link Component}
     *
     * @param text the text of the {@link Component}
     * @return a {@link Component} with a specific text
     */
    public Component getComponent(final String text) {
        return getComponent(new Filter<Component>() {
            @Override
            public boolean accept(final Component component) {
                final String line = component.getText();
                return line != null && line.toLowerCase().contains(text.toLowerCase());
            }
        });
    }

    /**
     * This method is used to get a {@link Component}
     *
     * @param filter the {@link Filter} to use in the loading
     * @return a {@link Component} selected by a {@link Filter}
     */
    public Component getComponent(final Filter<Component> filter) {
        for (final Widget widget : getLoaded()) {
            final Component[] components = widget.getChildren();
            if (components != null)
                for (final Component component : components)
                    if (component != null && component.isVisible()) {
                        if (filter.accept(component)) return component;

                        final Component[] children = component.getChildren();
                        if (children != null)
                            for (final Component child : children)
                                if (child != null && child.isVisible() && filter.accept(child)) return child;
                    }
        }

        return null;
    }

    /**
     * This method is used to get the continue {@link Component}
     *
     * @return the continue {@link Component}
     */
    public Component getContinue() {
        return getComponent(new Filter<Component>() {
            @Override
            public boolean accept(final Component component) {
                return component.isVisible() && !component.isHidden() && component.getY() > 420 && component.getText() != null && component.getText().contains("here to continue");
            }
        });
    }

    /**
     * This method is used to get a closable {@link Component}
     *
     * @return a closable {@link Component}
     */
    public Component getClosableWidget() {
        return getComponent(new Filter<Component>() {
            @Override
            public boolean accept(final Component component) {
                return component.isVisible() && component.getX() > 460 && component.getY() < 32 && component.getWidth() < 30 && component.getHeight() == 23;
            }
        });
    }

    /**
     * This method is used to get all loaded {@link Widget}s
     *
     * @return an array with all loaded {@link Widget}s
     */
    public Widget[] getLoaded() {
        final List<Widget> result = new ArrayList<Widget>();

        final RSWidget[][] widgets = context().client.getWidgets();
        if (widgets == null) return null;

        for (int i = 0; i < widgets.length; i++)
            if (widgets[i] != null && getValidWidgets()[i])
                result.add(new Widget(context(), widgets[i], i));

        return result.toArray(new Widget[result.size()]);
    }

}
