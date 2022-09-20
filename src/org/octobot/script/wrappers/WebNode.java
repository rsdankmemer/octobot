package org.octobot.script.wrappers;

import org.octobot.script.ScriptContext;

/**
 * WebNode
 *
 * @author Pat-ji
 */
public class WebNode extends Tile {
    public WebNode prev;
    public double distance, cost;

    public short[] edges;

    public WebNode(final Tile tile) {
        super(tile.getX(), tile.getY(), tile.getPlane());
    }

    public WebNode(final int x, final int y) {
        super(x, y, 0);
    }

    public WebNode(final int x, final int y, final int z) {
        super(x, y, z);
    }

    /**
     * This method is used to traverse this {@link WebNode}
     *
     * @param context the {@link ScriptContext} used for traversal
     * @return <code>true</code> if the client successfully traversed
     */
    public boolean traverse(final ScriptContext context) {
        return context.movement.walkTileOnMap(this);
    }

    /**
     * This method is used to reset the node
     *
     */
    public void reset() {
        prev = null;
        cost = distance = 0;
    }

    /**
     * This method is used to add an edge to this {@link WebNode}
     *
     * @param edge the edge for the {@link WebNode}
     */
    public void addEdge(final short edge) {
        final short[] newEdges = new short[edges.length + 1];
        System.arraycopy(edges, 0, newEdges, 0, edges.length);
        newEdges[newEdges.length - 1] = edge;
        edges = newEdges;
    }

    /**
     * This method is used to check if the {@link WebNode} is equal to this
     *
     * @param obj the object you want to compare to the {@link WebNode}
     * @return true if the {@link WebNode} and the object are equal
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj == this) return true;

        if (obj instanceof WebNode) {
            final WebNode node = (WebNode) obj;
            return node.getX() == getX() && node.getY() == getY() && node.getPlane() == getPlane();
        }

        return false;
    }

    /**
     * EventNode
     *
     * @author Pat-ji
     */
    public abstract class EventNode extends WebNode {

        public EventNode(final Tile tile) {
            super(tile);
        }

        public EventNode(final int x, final int y) {
            super(x, y, 0);
        }

        public EventNode(final int x, final int y, final int z) {
            super(x, y, z);
        }

        /**
         * Thisd method is used to check if the {@link EventNode} can be traversed
         *
         * @return <code>true</code> if the {@link EventNode} can be traversed
         */
        public abstract boolean traversable();

        /**
         * This method is used to traverse the {@link EventNode}
         *
         * @return true of the client has successfully traversed the {@link EventNode}
         */
        public abstract boolean traverse();

    }

}
