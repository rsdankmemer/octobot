package org.octobot.script.wrappers;

import org.octobot.script.ScriptContext;

import java.util.ArrayList;

/**
 * NetworkNode
 *
 * @author Joseph
 */
public class NavigationNode extends Tile {

//    1. generate a linear path of nodes that lead from the destination to the target.
//            2. find the most progressed node (nearest to the destination) that is within the minimap distance
//    3. if this tile is an object that is presently on the screen, interact with it.
//            4. otherwise, interpolate this point with the point that is more progressive than it, but is not on the screen. interpolate this new point such that the new point is on the screen and can be interacted with. if this operation is impossible, simply use the original point and do not bother with interpolation
//    5. walk to this new point

    public double distance, cost;
    public NavigationNode parent;

    protected int id;
    protected ArrayList<Integer> connections;

    public NavigationNode(int x, int y, int plane) {
        super(x, y, plane);
        connections = new ArrayList<Integer>();
    }

    public void setID(int id) {
        this.id = id;
    }

    public int getID() {
        return id;
    }

    public void addConnection(int id) {
        connections.add(id);
    }

    public void deleteConnection(int id) {
        for (int i = 0; i < connections.size(); i++) {
            if (connections.get(i) == id)
                connections.remove(i);
        }
    }

    public ArrayList<Integer> getConnections() {
        return connections;
    }

    public boolean isTraversable(final ScriptContext context) {
        Player localPlayer = context.players.getLocal();
        if (localPlayer == null)
            return false;
        Tile localPlayerLocation = localPlayer.getLocation();
        if (localPlayerLocation.getPlane() != getPlane())
            return false;
        if (localPlayerLocation.distance(this) > 18)
            return false;
        if (!context.movement.canReach(this))
            return false;
        return true;
    }

    /**
     * This method is used to traverse to a {@link NavigationNode}.
     *
     * @param context
     * @return
     */
    public boolean traverse(final ScriptContext context) {
        return context.movement.walkTileOnMap(this.randomize(2));
    }

    /**
     * This method is used to check if a {@link NavigationNode} is equal to this NavigationNode.
     *
     * @param obj the object you want to compare to the {@link WebNode}
     * @return true if the {@link WebNode} and the object are equal
     */
//    @Override
//    public boolean equals(final Object obj) {
//        if (obj == this) return true;
//
//        if (obj instanceof NavigationNode) {
//            final NavigationNode node = (NavigationNode) obj;
//            return node.getX() == getX() && node.getY() == getY() && node.getPlane() == getPlane();
//        }
//
//        return false;
//    }
}
