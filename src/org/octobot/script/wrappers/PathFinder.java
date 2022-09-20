package org.octobot.script.wrappers;

import org.octobot.script.ScriptContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.octobot.script.methods.Movement.*;

/**
 * PathFinder
 *
 * @author Pat-ji
 */
public class PathFinder {
    private final Tile start, dest;
    private final int[][] flags;
    private final int baseX, baseY;
    private final boolean isObject;
    private final int destIndexX, destIndexY;

    private final Tile[] path;

    public PathFinder(final ScriptContext context, final Tile start, final Tile dest) {
        this(context, start, dest, false);
    }

    public PathFinder(final ScriptContext context, final Tile start, final Tile dest, final boolean isObject) {
        this(context, start, dest, context.movement.getCollisionFlags(context.game.getPlane()), isObject);
    }

    public PathFinder(final ScriptContext context, final Tile start, final Tile dest, final int[][] flags) {
        this(context, start, dest, flags, false);
    }

    public PathFinder(final ScriptContext context, final Tile start, final Tile dest, final int[][] flags, final boolean isObject) {
        this.start = start;
        this.dest = dest;
        this.flags = flags;
        this.isObject = isObject;

        baseX = context.game.getBaseX();
        baseY = context.game.getBaseY();
        destIndexX = dest.getX() - baseX;
        destIndexY = dest.getY() - baseY;

        final Node[][] nodes = new Node[flags.length][flags[0].length];
        for (int x = 0; x < flags.length; x++)
            for (int y = 0; y < flags[x].length; y++)
                nodes[x][y] = new Node(x + baseX, y + baseY);

        path = findPath(nodes);
    }

    /**
     * This method is used to get the starting {@link Tile} of the {@link Path}
     *
     * @return the starting {@link Tile} of the {@link Path}
     */
    public Tile getStart() {
        return start;
    }

    /**
     * This method is used to get to destination {@link Tile} of the {@link Path}
     *
     * @return the destination {@link Tile} of the {@link Path}
     */
    public Tile getDest() {
        return dest;
    }

    /**
     * This method is used to get all the {@link Tile}s of the {@link Path}
     *
     * @return an array with all the {@link Tile}s of the path
     */
    public Tile[] getPath() {
        return path;
    }

    private Tile[] findPath(final Node[][] nodes) {
        if (start.getPlane() != dest.getPlane()) return null;

        final LinkedList<Node> openList = new LinkedList<Node>();
        final LinkedList<Node> destList = new LinkedList<Node>();

        final Node startNode = new Node(start);
        final Node destNode = new Node(dest);

        final int start = 0x01, end = 0x02;
        final float sqrt = (float) Math.sqrt(2);

        startNode.opened = start;
        openList.push(startNode);

        destNode.opened = end;
        destList.push(destNode);

        int x, y;
        float distance;
        Node current;
        while (!openList.isEmpty() && !destList.isEmpty()) {
            current = lowestCost(openList);
            current.closed = true;

            Node[] neighbors = getNeighbors(nodes, current);
            for (final Node neighbor : neighbors) {
                if (neighbor.closed) continue;

                if (neighbor.opened == end) return path(neighbor, current);

                x = neighbor.x;
                y = neighbor.y;

                distance = current.g + ((x - current.x == 0 || y - current.y == 0) ? 1 : sqrt);
                if (neighbor.opened == 0 || distance < neighbor.g) {
                    neighbor.g = distance;
                    neighbor.f = distance + heuristic(sqrt, Math.abs(x - destNode.x), Math.abs(y - destNode.y));
                    neighbor.parent = current;

                    if (neighbor.opened == 0) {
                        neighbor.opened = start;
                        openList.push(neighbor);
                    }
                }
            }

            current = lowestCost(destList);
            current.closed = true;

            neighbors = getNeighbors(nodes, current);
            for (final Node neighbor : neighbors) {
                if (neighbor.closed) continue;

                if (neighbor.opened == start) return path(neighbor, current);

                x = neighbor.x;
                y = neighbor.y;

                distance = current.g + ((x - current.x == 0 || y - current.y == 0) ? 1 : sqrt);
                if (neighbor.opened == 0 || distance < neighbor.g) {
                    neighbor.g = distance;
                    neighbor.f = distance + heuristic(sqrt, Math.abs(x - startNode.x), Math.abs(y - startNode.y));
                    neighbor.parent = current;

                    if (neighbor.opened == 0) {
                        neighbor.opened = end;
                        destList.push(neighbor);
                    }
                }
            }
        }

        return null;
    }

    private Node lowestCost(final LinkedList<Node> list) {
        Node result = null;
        for (final Node node : list)
            if (result == null || node.f < result.f)
                result = node;

        list.remove(result);
        return result;
    }

    private LinkedList<Tile> backtrack(final Node node) {
        final LinkedList<Tile> path = new LinkedList<Tile>();
        Node next = node;
        while (next != null) {
            path.push(new Tile(next.x, next.y));
            next = next.parent;
        }

        return path;
    }

    private Tile[] path(final Node neighbor, final Node current) {
        final LinkedList<Tile> path = backtrack(current);
        final LinkedList<Tile> neighborPath = backtrack(neighbor);
        Collections.reverse(neighborPath);
        path.addAll(neighborPath);

        if (!path.get(0).equals(start))
            Collections.reverse(path);

        return path.toArray(new Tile[path.size()]);
    }

    private float heuristic(final float sqrt, final int x, final int y) {
        final float diag = Math.min(x, y);
        return sqrt * diag + (x + y) - 2 * diag;
    }

    private Node[] getNeighbors(final Node[][] nodes, final Node node) {
        final List<Node> neighbors = new ArrayList<Node>();
        final int x = node.x - baseX;
        final int y = node.y - baseY;
        if (!valid(x, y)) return neighbors.toArray(new Node[neighbors.size()]);

        final int here = flags[x][y];
        if (isObject) {
            if (destIndexX == x - 1 && destIndexY == y)
                neighbors.add(nodes[x - 1][y]);

            if (destIndexX == x + 1 && destIndexY == y)
                neighbors.add(nodes[x + 1][y]);

            if (destIndexX == x && destIndexY == y + 1)
                neighbors.add(nodes[x][y + 1]);

            if (destIndexX == x && destIndexY == y - 1)
                neighbors.add(nodes[x][y - 1]);
        }

        if (valid(x - 1, y - 1) && (here & (Flags.WALL_SOUTHWEST | Flags.WALL_SOUTH | Flags.WALL_WEST)) == 0
                && (flags[x - 1][y - 1] & (Flags.BLOCKED | Flags.WATER)) == 0
                && (flags[x][y - 1] & (Flags.BLOCKED | Flags.WALL_WEST | Flags.WATER)) == 0
                && (flags[x - 1][y] & (Flags.BLOCKED | Flags.WALL_SOUTH | Flags.WATER)) == 0)
            neighbors.add(nodes[x - 1][y - 1]);
        if (valid(x, y - 1) && (here & Flags.WALL_SOUTH) == 0 && (flags[x][y - 1] & (Flags.BLOCKED | Flags.WATER)) == 0)
            neighbors.add(nodes[x][y - 1]);
        if (valid(x + 1, y - 1) && (here & (Flags.WALL_SOUTHEAST | Flags.WALL_SOUTH | Flags.WALL_EAST)) == 0
                && (flags[x + 1][y - 1] & (Flags.BLOCKED | Flags.WATER)) == 0
                && (flags[x][y - 1] & (Flags.BLOCKED | Flags.WALL_EAST | Flags.WATER)) == 0
                && (flags[x + 1][y] & (Flags.BLOCKED | Flags.WALL_SOUTH | Flags.WATER)) == 0)
            neighbors.add(nodes[x + 1][y - 1]);
        if (valid(x + 1, y) && (here & Flags.WALL_EAST) == 0 && (flags[x + 1][y] & (Flags.BLOCKED | Flags.WATER)) == 0)
            neighbors.add(nodes[x + 1][y]);
        if (valid(x + 1, y + 1) && (here & (Flags.WALL_NORTHEAST | Flags.WALL_NORTH | Flags.WALL_EAST)) == 0
                && (flags[x + 1][y + 1] & (Flags.BLOCKED | Flags.WATER)) == 0
                && (flags[x][y + 1] & (Flags.BLOCKED | Flags.WALL_EAST | Flags.WATER)) == 0
                && (flags[x + 1][y] & (Flags.BLOCKED | Flags.WALL_NORTH | Flags.WATER)) == 0)
            neighbors.add(nodes[x + 1][y + 1]);
        if (valid(x, y + 1) && (here & Flags.WALL_NORTH) == 0 && (flags[x][y + 1] & (Flags.BLOCKED | Flags.WATER)) == 0)
            neighbors.add(nodes[x][y + 1]);
        if (valid(x - 1, y + 1) && (here & (Flags.WALL_NORTHWEST | Flags.WALL_NORTH | Flags.WALL_WEST)) == 0
                && (flags[x - 1][y + 1] & (Flags.BLOCKED | Flags.WATER)) == 0
                && (flags[x][y + 1] & (Flags.BLOCKED | Flags.WALL_WEST | Flags.WATER)) == 0
                && (flags[x - 1][y] & (Flags.BLOCKED | Flags.WALL_NORTH | Flags.WATER)) == 0)
            neighbors.add(nodes[x - 1][y + 1]);
        if (valid(x - 1, y) && (here & Flags.WALL_WEST) == 0 && (flags[x - 1][y] & (Flags.BLOCKED | Flags.WATER)) == 0)
            neighbors.add(nodes[x - 1][y]);

        return neighbors.toArray(new Node[neighbors.size()]);
    }

    private boolean valid(final int x, final int y) {
        return !(x < 0 || y < 0 || x >= flags.length || y >= flags[x].length);
    }

    /**
     * Node
     *
     * @author Pat-ji
     */
    public final class Node {
        public final int x, y;

        public int opened;
        public boolean closed;
        public float g, f;
        public Node parent;

        public Node(final Tile tile) {
            this(tile.getX(), tile.getY());
        }

        public Node(final int x, final int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj == this) return true;

            if (obj instanceof Node) {
                final Node node = (Node) obj;
                return node.x == x && node.y == y;
            }

            return false;
        }

    }

}
