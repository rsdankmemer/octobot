package org.octobot.script.methods;

import org.octobot.bot.handler.TextHandler;
import org.octobot.script.ContextProvider;
import org.octobot.script.Locatable;
import org.octobot.script.ScriptContext;
import org.octobot.script.event.listeners.PaintListener;
import org.octobot.script.wrappers.Tile;
import org.octobot.script.wrappers.WebNode;
import org.octobot.script.wrappers.WebPath;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.List;

/**
 * Landscape
 *
 * @author Pat-ji
 */
@Deprecated
public class Landscape extends ContextProvider implements PaintListener {
    private static final List<WebNode> NODE_LIST;

    static {
        NODE_LIST = new ArrayList<WebNode>();
    }

    public Landscape(final ScriptContext context) {
        super(context);
    }

    /**
     * This method is used to load all the {@link WebNode}s by the client.
     * This is automatically done by the client, and should not be used by users
     */
    @Deprecated
    public static void load() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/landscape/data.gtx")));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        final String[] idx = line.split("\\|");
                        if (idx.length == 3) {
                            NODE_LIST.add(new WebNode(Integer.parseInt(TextHandler.decode(idx[0])), Integer.parseInt(TextHandler.decode(idx[1])), Integer.parseInt(TextHandler.decode(idx[2]))));
                        } else {
                            NODE_LIST.add(new WebNode(Integer.parseInt(TextHandler.decode(idx[0])), Integer.parseInt(TextHandler.decode(idx[1]))));
                        }
                    }

                    reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/landscape/edges.xml")));
                    while ((line = reader.readLine()) != null) {
                        if (line.contains("<node-")) {
                            final int index = Integer.parseInt(line.substring(line.indexOf("-") + 1, line.indexOf(">")));
                            line = line.substring(line.indexOf(">") + 1);
                            final String[] edges = line.split("<edges>");

                            NODE_LIST.get(index).edges = new short[edges.length - 1];
                            for (int i = 0; i < edges.length - 1; i++) {
                                final String edge = edges[i + 1];
                                NODE_LIST.get(index).edges[i] = Short.parseShort(edge.substring(0, edge.indexOf("<")));
                            }
                        }
                    }

                    reader.close();
                    System.out.println("[Landscape] - Successfully stored the web data.");
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Deprecated
    private void resetNodes() {
        for (final WebNode node : NODE_LIST)
            node.reset();
    }

    /**
     * This method is used to generate a {@link WebPath}
     *
     * @param start the {@link Locatable} to start generating from
     * @param end the {@link Locatable} to generate to
     * @return a {@link WebPath} between the start and end {@link Locatable}
     */
    @Deprecated
    public WebPath getWebPath(final Locatable start, final Locatable end) {
        return new WebPath(context(), findWebPath(start, end));
    }

    /**
     * This method is used to generate a {@link WebPath}
     *
     * @param start the {@link Tile} to start generating from
     * @param end the {@link Tile} to generate to
     * @return a {@link WebPath} between the start and end {@link Tile}
     */
    @Deprecated
    public WebPath getWebPath(final Tile start, final Tile end) {
        return new WebPath(context(), findWebPath(start, end));
    }

    @Deprecated
    private WebNode[] findWebPath(final Locatable start, final Locatable end) {
        return findWebPath(start.getLocation(), end.getLocation());
    }

    @Deprecated
    private WebNode[] findWebPath(final Tile start, final Tile end) {
        if (start == null || end == null) return null;

        resetNodes();

        final Set<WebNode> open = new HashSet<WebNode>();
        final Set<WebNode> closed = new HashSet<WebNode>();

        final WebNode dest = getNearestTo(end);
        WebNode curr = getNearestTo(start);
        if (dest == null || curr == null) return null;

        curr.cost = heuristic(curr, dest);
        open.add(curr);

        while (!open.isEmpty()) {
            curr = lowestCost(open);
            if (curr.equals(dest)) return path(curr, new WebNode(end));

            open.remove(curr);
            closed.add(curr);
            for (final WebNode next : getEdges(curr)) {
                if (!closed.contains(next)) {
                    final double distance = curr.distance + distance(curr, next);
                    boolean add = false;
                    if (!open.contains(next)) {
                        open.add(next);
                        add = true;
                    } else if (distance < next.distance) {
                        add = true;
                    }

                    if (add) {
                        next.prev = curr;
                        next.distance = distance;
                        next.cost = distance + heuristic(next, dest);
                    }
                }
            }
        }

        return null;
    }

    @Deprecated
    private WebNode getNearestTo(final Tile tile) {
        WebNode best = null;
        double dist = Double.MAX_VALUE - 1;
        for (final WebNode node : NODE_LIST) {
            if (node.getPlane() != tile.getPlane()) continue;

            final double ddist = node.distance(tile);
            if (ddist < dist) {
                best = node;
                dist = ddist;
            }
        }

        return best;
    }

    @Deprecated
    private double heuristic(final WebNode start, final WebNode end) {
        if (start == null || end == null) return 0;

        final double dx = Math.abs(start.getX() - end.getX());
        final double dy = Math.abs(start.getY() - end.getY());
        final double diagonal = Math.min(dx, dy);
        return Math.sqrt(2.0) * diagonal + (dx + dy) - 2 * diagonal;
    }

    @Deprecated
    private double distance(final WebNode start, final WebNode end) {
        return start.distance(end);
    }

    @Deprecated
    private WebNode lowestCost(final Set<WebNode> open) {
        WebNode best = null;
        for (final WebNode scan : open)
            if (best == null || scan.cost < best.cost)
                best = scan;

        return best;
    }

    @Deprecated
    private WebNode[] path(final WebNode current, final WebNode end) {
        final LinkedList<WebNode> path = new LinkedList<WebNode>();
        WebNode next = current;
        while (next != null) {
            if (next.prev != null)
                path.addFirst(next.prev);

            next = next.prev;
        }

        path.addLast(current);
        path.addLast(end);
        return path.toArray(new WebNode[path.size()]);
    }

    @Deprecated
    private WebNode[] getEdges(final WebNode node) {
        if (node.edges == null) return null;

        final List<WebNode> edges = new ArrayList<WebNode>();
        for (final short index : node.edges)
            edges.add(NODE_LIST.get(index));

        return edges.toArray(new WebNode[edges.size()]);
    }

    /**
     * This method is used to render the {@link Landscape}
     *
     * @param g the {@link java.awt.Graphics} to render with
     */
    @Deprecated
    public void render(final Graphics g) {
        final int plane = context().game.getPlane();
        final int size = NODE_LIST.size();
        for (int i = 0; i < size; i++) {
            if (plane != NODE_LIST.get(i).getPlane()) continue;

            final WebNode[] edges = getEdges(NODE_LIST.get(i));
            if (edges == null) continue;

            final Point nodePoint = context().calculations.tileToMiniMap(NODE_LIST.get(i));
            for (final WebNode edge : edges) {
                final Point edgePoint = context().calculations.tileToMiniMap(edge);
                if (nodePoint != null && edgePoint != null) {
                    g.drawLine(nodePoint.x, nodePoint.y, edgePoint.x, edgePoint.y);
                    g.drawString("" + i, nodePoint.x, nodePoint.y);
                }
            }
        }
    }

}
