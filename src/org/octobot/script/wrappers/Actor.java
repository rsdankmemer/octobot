package org.octobot.script.wrappers;

import org.octobot.bot.game.client.RSActor;
import org.octobot.script.Locatable;
import org.octobot.script.Nameable;
import org.octobot.script.ScriptContext;

import java.awt.*;

/**
 * Actor
 *
 * @author Pat-ji
 */
public abstract class Actor<T extends RSActor> extends Animable<T> implements RSActor, Nameable, Locatable {

    /**
     * ActorType
     *
     * @author pat-ji
     */
    public enum ActorType {
        PLAYER, NPC

    }

    public Actor(final ScriptContext context, final T accessor) {
        super(context, accessor);
    }

	/**
	 * This method is used to get the name of the {@link Actor}
	 *
	 * @return the {@link Actor}s name
	 */
    public abstract String getName();

	/**
	 * This method is used to get the animation of the {@link Actor}
	 *
	 * @return the {@link Actor}s animation
	 */
    @Override
    public int getAnimation() {
        return getAccessor() != null ? getAccessor().getAnimation() : -1;
    }

    /**
     * This method is used to get the walking queue of the {@link Actor}
     *
     * @return the {@link Actor}s walking queue
     */
    @Override
    public int getWalkingQueue() {
        return getAccessor() != null ? getAccessor().getWalkingQueue() : -1;
    }

    /**
     * This method is used to get the local x of the {@link Actor}
     *
     * @return the {@link Actor}s local x
     */
    public int getLocalX() {
        return getAccessor() != null ? getAccessor().getX() : -1;
    }

    /**
     * This method is used to get the x location of the {@link Actor}
     *
     * @return the {@link Actor}s x location
     */
    @Override
    public int getX() {
        return context.game.getBaseX() + (getLocalX() >> 7);
    }

    /**
     * This method is used to get the local y of the {@link Actor}
     *
     * @return the {@link Actor}s local y
     */
    public int getLocalY() {
        return getAccessor() != null ? getAccessor().getY() : -1;
    }

    /**
     * This method is used to get the y location of the {@link Actor}
     *
     * @return the {@link Actor}s y location
     */
    @Override
    public int getY() {
        return context.game.getBaseY() + (getLocalY() >> 7);
    }

	/**
	 * This method is used to get the location of the {@link Actor}
	 *
	 * @return the {@link Actor}s location
	 */
    @Override
    public Tile getLocation() {
        return new Tile(getX(), getY(), context.game.getPlane());
    }

	/**
	 * This method is used to get the overhead text of the {@link Actor}
	 *
	 * @return the {@link Actor}s overhead text as a {@link String}
	 */
    @Override
    public String getOverheadText() {
        return getAccessor() != null ? getAccessor().getOverheadText() : null;
    }

	/**
	 * This method is used to get the interacting index of the {@link Actor}
	 *
	 * @return the {@link Actor}s interaction index
	 */
    @Override
    public int getInteractingIndex() {
        return getAccessor() != null ? getAccessor().getInteractingIndex() : -1;
    }

	/**
	 * This method is used to see who the {@link Actor} is interacting with
	 *
	 * @return who the {@link Actor} is interacting with as an {@link Actor}
	 */
    public Actor getInteracting() {
        final int index = getInteractingIndex();
        if (index == -1) {
            return null;
        } else if (index < 32768) {
            return new NPC(context, context.client.getNpcArray()[index]);
        } else if (index - 32768 == context.client.getLocalPlayerIndex()) {
            return context.players.getLocal();
        } else {
            return new Player(context, context.client.getPlayerArray()[index - 32768]);
        }
    }

	/**
	 * This method is used to check if an {@link Actor} is being interacting with by given type
	 *
	 * @param type which type of actor to check for, either {@link NPC} or {@link Player}
	 * @return <code>true</code> if the {@link Actor} is interacting with given type
	 */
    public boolean isInteractedBy(final ActorType type) {
        switch (type) {
            case PLAYER:
                for (final Player player : context.players.getLoaded()) {
                    final Actor interacting;
                    if (player != null && (interacting = player.getInteracting()) != null && interacting.equals(this)) return true;
                }

                break;
            case NPC:
                for (final NPC npc : context.npcs.getLoaded()) {
                    final Actor interacting;
                    if (npc != null && (interacting = npc.getInteracting()) != null && interacting.equals(this)) return true;
                }

                break;
        }

        return false;
    }

	/**
	 * This method is used to get the rotation of the {@link Actor}
	 *
	 * @return the {@link Actor}s orientation
	 */
    @Override
    public int getOrientation() {
        return (180 + getRotation() * 45 / 2048) % 360;
    }

	/**
	 * This method is used to get the health of the {@link Actor}
	 *
	 * @return the {@link Actor}s health
	 */
    @Override
    public int getHealth() {
        return getAccessor() != null ? getAccessor().getHealth() : -1;
    }

	/**
	 * This method is used to get the maximum amount of health of the {@link Actor}
	 *
	 * @return the {@link Actor}s maximum health
	 */
    @Override
    public int getMaxHealth() {
        return getAccessor() != null ? getAccessor().getMaxHealth() : -1;
    }

	/**
	 * This method is used to check what damage is being dealt to the {@link Actor}
	 *
	 * @return the damage being dealt to the {@link Actor} as an <code>int</code> <code>Array</code>
	 */
    @Override
    public int[] getHitDamages() {
        return getAccessor() != null ? getAccessor().getHitDamages() : null;
    }

	/**
	 * This method is used to check what type of hits are being dealt to the {@link Actor} such as poison or normal damage
	 *
	 * @return the type of hit being dealth to the {@link Actor}
	 */
    @Override
    public int[] getHitTypes() {
        return getAccessor() != null ? getAccessor().getHitTypes() : null;
    }

	/**
	 * This method is used to get the hit cycles of an {@link Actor}
	 *
	 * @return the {@link Actor}s hit cycles
	 */
    @Override
    public int[] getHitCycles() {
        return getAccessor() != null ? getAccessor().getHitCycles() : null;
    }

	/**
	 * This method is used to check what the idle animation of the {@link Actor} is
	 *
	 * @return the {@link Actor}s idle animation
	 */
    @Override
    public int getIdleAnimation() {
        return getAccessor() != null ? getAccessor().getIdleAnimation() : -1;
    }

	/**
	 * This method is used to check what the walking animation of the {@link Actor} is
	 *
	 * @return the {@link Actor}s walking animation
	 */
    @Override
    public int getWalkAnimation() {
        return getAccessor() != null ? getAccessor().getWalkAnimation() : -1;
    }

	/**
	 * This method is used to check what the running animation of the {@link Actor} is
	 *
	 * @return the {@link Actor}s running animation
	 */
    @Override
    public int getRunAnimation() {
        return getAccessor() != null ? getAccessor().getRunAnimation() : -1;
    }

	/**
	 * This method is used to get the loop cycle of the {@link Actor}
	 *
	 * @return the {@link Actor}s loop cycle
	 */
    @Override
    public int getLoopCycle() {
        return getAccessor() != null ? getAccessor().getLoopCycle() : -1;
    }

	/**
	 * This method is used to get the {@link Actor}s rotation
	 *
	 * @return the {@link Actor}s rotation
	 */
    public int getRotation() {
        return getAccessor() != null ? getAccessor().getOrientation() : -1;
    }

	/**
	 * This method is used to check if an {@link Actor} is moving
	 *
	 * @return <code>true</code> if the actor is currently moving
	 */
    public boolean isMoving() {
        return getWalkingQueue() > 0;
    }

	/**
	 * This method is used to check if an {@link Actor} is currently idle
	 *
	 * @return <code>true</code> if the {@link Actor} is idle
	 */
    public boolean isIdle() {
        return !isMoving() && getAnimation() == -1 && getInteractingIndex() == -1;
    }

    /**
     * This method is used to render the {@link Actor}
     *
     * @param graphics the {@link java.awt.Graphics} to render with
     */
    public void render(final Graphics graphics) {
        final Model model = getModel();
        if (model != null) {
            final Point[] points = model.getPoints();
            if (points != null && points.length > 0)
                for (final Point point : points)
                    if (point != null)
                        graphics.fillRect(point.x, point.y, 2, 2);
        }
    }

	/**
	 * This method is used to check if the {@link Actor} is equal to given object
	 *
	 * @param obj the object you want to compare the {@link Actor} to
	 * @return <code>true</code> if the {@link Actor} is equal to given object
	 */
    @Override
    public boolean equals(final Object obj) {
        if (obj == this) return true;

        if (obj instanceof Actor) {
            final Actor actor = (Actor) obj;
            return actor.getAccessor() == getAccessor();
        } else if (obj instanceof RSActor) {
            return obj == getAccessor();
        }

        return false;
    }

}
