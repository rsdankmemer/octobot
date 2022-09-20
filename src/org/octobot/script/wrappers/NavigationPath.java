package org.octobot.script.wrappers;

import org.octobot.script.ScriptContext;

import java.util.LinkedList;

/**
 * Created by Joseph on 12/20/2014.
 */
public class NavigationPath {

    protected ScriptContext context;
    protected NavigationNode[] nodes;
    protected long generationTime;
    protected long lastInteraction;

    public NavigationPath(ScriptContext context, NavigationNode finalNode, long generationTime) {
        this.context = context;
        this.generationTime = generationTime;
        LinkedList<NavigationNode> path = new LinkedList<NavigationNode>();
        NavigationNode nextNode = finalNode;
        while (nextNode != null) {
            path.addFirst(nextNode);
            nextNode = nextNode.parent;
        }
        nodes = path.toArray(new NavigationNode[path.size()]);
    }

    public NavigationPath(ScriptContext context, NavigationNode[] nodes) {
        this.context = context;
        this.nodes = nodes;
    }

    /**
     * Returns the time in milliseconds that the bot took to generate this path.
     *
     * @return the time used for path generation
     */
    public long getGenerationTime() {
        return generationTime;
    }

    /**
     * Returns the total distance from the start point to the finish point, accounting for all nodes in between the two.
     *
     * @return the distance necessary to travel from the start to the finish
     */
    public double getTotalDistance() {
        return 0;
    }

    /**
     * Returns the most fit candidate for traversal. Generally, this corresponds to the {@link org.octobot.script.wrappers.NavigationNode}
     * that is nearest to the finish point while still being reachable and visible on the minimap.
     * <p>
     * If a suitable node cannot be found, this function will return null.
     *
     * @return the most fit node for traversal
     */
    public NavigationNode getNext() {
        for (int i = nodes.length - 1; i >= 0; i--) {
            if (nodes[i].isTraversable(context))
                return nodes[i];
        }
        return null;
    }

    /**
     * Produces a reversed version of this {@link NavigationPath}. This function produces a new object, and the object that this
     * function is called on will remain unchanged.
     *
     * @return a new reversed version of this path
     */
    // todo lies
    public void reverse() {
        NavigationNode[] reversed = new NavigationNode[nodes.length];
        for (int i = 0; i < nodes.length; i++) {
            reversed[nodes.length - 1 - i] = nodes[i];
        }
        nodes = reversed;
    }

    public boolean traverse() {
        NavigationNode target = getNext();
        if (target == null)
            return false;
        if (!shouldTraverse())
            return false;
        lastInteraction = System.currentTimeMillis();
        return target.traverse(context);
    }

    /**
     * Returns true if the bot should be allowed to make another traversal action, such as clicking on the map or interacting
     * with an object. If the bot has recently made an action, returns false. If the player is not moving or if the
     * distance to the destination is small, returns true. Otherwise, returns false.
     *
     * @return
     */
    private boolean shouldTraverse() {
        Player localPlayer = context.players.getLocal();
        Tile localPlayerLocation = localPlayer.getLocation();
        Tile destination = context.movement.getDestination();
        long deltaTime = System.currentTimeMillis() - lastInteraction;
        if (deltaTime < 1000)
            return false;
        if (!localPlayer.isMoving())
            return true;
        if (localPlayerLocation.distance(destination) < 7) {
            return true;
        }
        return false;
    }

}
