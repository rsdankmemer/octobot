package org.octobot.script.wrappers;

import org.octobot.bot.game.client.RSWidget;
import org.octobot.script.ScriptContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Widget
 *
 * @author Pat-ji
 */
public class Widget {
    private final ScriptContext context;

    private final int index;
    private final RSWidget[] widgets;

    public Widget(final ScriptContext context, final RSWidget[] widgets, final int index) {
        this.context = context;
        this.widgets = widgets;
        this.index = index;
    }

    /**
     * This method is used to get the {@link Widget}s index
     *
     * @return the index of the {@link Widget}
     */
    public int getIndex() {
        return index;
    }

    /**
     * This method is used to get the child of the {@link Widget} via the index
     *
     * @param index the index of the child
     * @return the {@link Widget}s child
     */
    public Component getChild(final int index) {
        return index < widgets.length && widgets[index] != null ? new Component(context, widgets[index], index) : null;
    }

    /**
     * This method is used to get the children
     *
     * @return the {@link Widget}s children
     */
    public Component[] getChildren() {
        if (widgets == null) return null;

        int index = 0;
        final List<Component> children = new ArrayList<Component>();
        for (final RSWidget widget : widgets)
            if (widget != null && widgets[index] != null)
                children.add(new Component(context, widget, index++));

        return children.toArray(new Component[children.size()]);
    }

    /**
     * This method is used to check if the {@link Widget} is equal to this
     *
     * @param obj the object you want to compare to the {@link Widget}
     * @return true if the {@link Widget} and the object are equal
     */
    @Override
    public boolean equals(final Object obj) {
        return obj == this || obj instanceof Widget && ((Widget) obj).index == index;
    }

}
