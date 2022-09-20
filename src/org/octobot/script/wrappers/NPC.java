package org.octobot.script.wrappers;

import org.octobot.bot.game.client.RSModel;
import org.octobot.bot.game.client.RSNPC;
import org.octobot.script.Identifiable;
import org.octobot.script.ScriptContext;

/**
 * NPC
 *
 * @author pat-ji
 */
public class NPC extends Actor<RSNPC> implements RSNPC, Identifiable {

    public NPC(final ScriptContext context, final RSNPC accessor) {
        super(context, accessor);
    }

    /**
     * This method is used to get the model of the {@link NPC}
     *
     * @return the {@link NPC}s model
     */
    @Override
    public Model getModel() {
        final Model model = getModelInstance();
        if (model != null) {
            model.setContext(context);
            model.setX(getLocalX());
            model.setY(getLocalY());
            model.rotate(getRotation() & 0x3fff);
            return model;
        }

        return null;
    }

    /**
     * This method is used to get the definition of the {@link NPC}
     *
     * @return the {@link NPC}s definition
     */
    @Override
    public NPCDefinition getDefinition() {
        NPCDefinition definition = new NPCDefinition(getAccessor().getDefinition());
        if ("null".equals(definition.getName()))
            return new NPCDefinition(definition.getChildDefinition());

        if (definition.getActions() != null) {
            for (final String action : definition.getActions())
                if (action != null && action.equalsIgnoreCase("Hidden")) {
                    definition = new NPCDefinition(definition.getChildDefinition());
                    break;
                }
        }

        return definition;
    }

    /**
     * This method is used to get the name of the {@link NPC}
     *
     * @return the {@link NPC}s name
     */
    @Override
    public String getName() {
        final NPCDefinition definition = getDefinition();
        return definition.getAccessor() != null ? definition.getName() : "";
    }

    /**
     * This method is used to get the level of the {@link NPC}
     *
     * @return the {@link NPC}s level
     */
    public int getLevel() {
        final NPCDefinition definition = getDefinition();
        return definition.getAccessor() != null ? definition.getLevel() : -1;
    }

    /**
     * This method is used to get the id of the {@link NPC}
     *
     * @return the {@link NPC}s id
     */
    @Override
    public int getId() {
        final NPCDefinition definition = getDefinition();
        return definition.getAccessor() != null ? definition.getId() : -1;
    }

    /**
     * This method is used to get the actions of the {@link NPC}
     *
     * @return the {@link NPC}s actions
     */
    public String[] getActions() {
        final NPCDefinition definition = getDefinition();
        return definition.getAccessor() != null ? definition.getActions() : null;
    }

    /**
     * This method is used to get the animated model of the {@link NPC}, this shouldn't be used by scripters.
     *
     * @return the {@link NPC}s animated model
     */
    @Override
    public RSModel getAnimatedModel() {
        return getAccessor() != null ? getAccessor().getAnimatedModel() : null;
    }

    /**
     * This method is used to check if a {@link NPC} is valid
     *
     * @return {@code true} if the {@link NPC} is valid
     */
    @Override
    public boolean isValid() {
        for (final RSNPC npc : context.client.getNpcArray())
            if (equals(npc)) return true;

        return false;
    }

    /**
     * This method is used to check if the {@link NPC} is equal to this
     *
     * @param obj the object you want to compare to the {@link NPC}
     * @return true if the {@link NPC} and the object are equal
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj == this) return true;

        if (obj instanceof NPC) {
            final NPC npc = (NPC) obj;
            return npc.getAccessor() == getAccessor();
        } else if (obj instanceof RSNPC) {
            return obj == getAccessor();
        }

        return false;
    }

}
