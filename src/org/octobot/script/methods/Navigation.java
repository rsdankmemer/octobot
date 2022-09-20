package org.octobot.script.methods;

import org.octobot.script.ContextProvider;
import org.octobot.script.ScriptContext;
import org.octobot.script.wrappers.*;

import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Joseph on 12/19/2014.
 */
public class Navigation extends ContextProvider {

    private static HashMap<Integer, NavigationNode> nodes = new HashMap<Integer, NavigationNode>();

    public Navigation(ScriptContext context) {
        super(context);
    }

    public static HashMap<Integer, NavigationNode> getLoadedNodes() {
        return nodes;
    }

    private static int lastHighest = -1;

    public static int addNode(NavigationNode node) {
        return addNode(lastHighest + 1, node);
    }

    public static int addNode(int id, NavigationNode node) {
        if (id > lastHighest)
            lastHighest = id;
        nodes.put(id, node);
        return id;
    }

    public static void deleteNode(int id) {
        NavigationNode deadNode = getNode(id);
        for (Integer connectionID : deadNode.getConnections()) {
            NavigationNode connectionNode = getNode(connectionID);
            connectionNode.deleteConnection(id);
        }
        nodes.remove(id);
    }

    public static NavigationNode getNode(int id) {
        return nodes.get(id);
    }

    public static boolean saveData() {
        try {
            String folder = org.octobot.bot.Environment.getDataDirectory() + File.separator + "web.dat";
            FileWriter fileWriter = new FileWriter(folder);
            BufferedWriter out = new BufferedWriter(fileWriter);
            for (Integer key : nodes.keySet()) {
                NavigationNode node = getNode(key);
                if (node instanceof NavigationInteractable) {
                    NavigationInteractable door = (NavigationInteractable) node;
                    out.write(key.toString());
                    out.write(";");
                    out.write("2");
                    out.write(";");
                    out.write(node.getX() + "," + node.getY() + "," + node.getPlane());
                    out.write(";");
                    int i = 1;
                    for (Integer connection : node.getConnections()) {
                        out.write(connection.toString());
                        if (i < node.getConnections().size()) {
                            out.write(",");
                        }
                        i++;
                    }
                    out.write(";");
                    out.write(door.getObjectName());
                    out.write(";");
                    out.write(door.getObjectOpenAction());
                    out.write(";");
                    out.write(door.getObjectCloseAction());
                    out.write(";");
                    out.write(door.getActivationArea().serialize());
                } else {
                    out.write(key.toString());
                    out.write(";");
                    out.write("1");
                    out.write(";");
                    out.write(node.getX() + "," + node.getY() + "," + node.getPlane());
                    out.write(";");
                    int i = 1;
                    for (Integer connection : node.getConnections()) {
                        out.write(connection.toString());
                        if (i < node.getConnections().size()) {
                            out.write(",");
                        }
                        i++;
                    }
                }
                out.newLine();
            }
            out.close();
            System.out.println("[Navigation] - Sucessfully saved " + nodes.size() + " navigation nodes.");
            return true;
        } catch (IOException e) {
            System.out.println("[Navigation] - Failed to save navigation data.");
            return false;
        }
    }

    public static boolean loadData() {
        nodes.clear();
        try {
            long startTime = System.currentTimeMillis();
            String folder = org.octobot.bot.Environment.getDataDirectory() + File.separator + "web.dat";
            FileReader fileReader = new FileReader(folder);
            BufferedReader in = new BufferedReader(fileReader);
            String line;
            while ((line = in.readLine()) != null) {
                String[] nodeData = line.split(";");
                int nodeID = Integer.valueOf(nodeData[0]);
                int nodeType = Integer.valueOf(nodeData[1]);
                String[] nodePosition = nodeData[2].split(",");
                int x = Integer.valueOf(nodePosition[0]);
                int y = Integer.valueOf(nodePosition[1]);
                int z = Integer.valueOf(nodePosition[2]);
                String[] connections = nodeData[3].split(",");
                if (nodeType == 1) {
                    NavigationNode node = new NavigationNode(x, y, z);
                    for (String item : connections) {
                        int connectionID = Integer.valueOf(item);
                        node.addConnection(connectionID);
                    }
                    addNode(nodeID, node);
                } else if (nodeType == 2) {
                    String objectName = nodeData[4];
                    String objectOpenAction = nodeData[5];
                    String objectCloseAction = nodeData[6];
                    Area area = Area.deserialize(nodeData[7]);
                    NavigationInteractable node = new NavigationInteractable(x, y, z, objectName, objectOpenAction, objectCloseAction, area);
                    for (String item : connections) {
                        int connectionID = Integer.valueOf(item);
                        node.addConnection(connectionID);
                    }
                    addNode(nodeID, node);
                }
            }
            fileReader.close();
            in.close();
            long deltaTime = System.currentTimeMillis() - startTime;
            System.out.println("[Navigation] - Successfully loaded " + nodes.size() + " navigation nodes (" + deltaTime + " ms).");
            return true;
        } catch (FileNotFoundException e) {
            System.out.println("[Navigation] - Failed to load navigation data - file not found.");
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("[Navigation] - Failed to load navigation data - an error occurred.");
            return false;
        }
    }

    public void render(final Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        final int plane = context().game.getPlane();
        for (NavigationNode node : nodes.values()) {
            if (node.getPlane() == plane) {
                if (node instanceof NavigationInteractable) {
                    node.render(context(), g2d, new Color(150, 255, 255, 128), true);
                } else {
                    node.render(context(), g2d, new Color(200, 200, 200, 128), true);
                }
            }
        }
    }

    public static int getNearestNodeKey(Tile tile) {
        int bestKey = -1;
        double bestDistance = Double.MAX_VALUE;
        for (Integer nodeKey : nodes.keySet()) {
            NavigationNode node = getNode(nodeKey);
            if (node.getPlane() != tile.getPlane())
                continue;

            double distance = tile.distance(node);
            if (distance < bestDistance) {
                bestKey = nodeKey;
                bestDistance = distance;
            }
        }
        return bestKey;
    }

    private NavigationNode getCheapestNode(Set<NavigationNode> set) {
        NavigationNode best = null;
        for (NavigationNode node : set) {
            if (best == null || node.cost < best.cost)
                best = node;
        }
        return best;
    }

    public NavigationPath generatePath(Tile source, Tile destination) {
        long startTime = System.currentTimeMillis();
        int startNodeKey = getNearestNodeKey(source);
        int endNodeKey = getNearestNodeKey(destination);
        NavigationNode startNode = getNode(startNodeKey);
        NavigationNode endNode = getNode(endNodeKey);

        Set<NavigationNode> openSet = new HashSet<NavigationNode>();
        Set<NavigationNode> closedSet = new HashSet<NavigationNode>();
        openSet.add(startNode);

        while (!openSet.isEmpty()) {
            NavigationNode activeNode = getCheapestNode(openSet);
            openSet.remove(activeNode);
            closedSet.add(activeNode);

            if (activeNode.equals(endNode)) {
                long deltaTime = System.currentTimeMillis() - startTime;
                NavigationPath path = new NavigationPath(context(), activeNode, deltaTime);
                for (NavigationNode clearNode : nodes.values()) {
                    clearNode.parent = null;
                    clearNode.distance = 0;
                    clearNode.cost = 0;
                }
                return path;
            }

            for (int connectionID : activeNode.getConnections()) {
                NavigationNode connectedNode = getNode(connectionID);
                if (!closedSet.contains(connectedNode)) {
                    if (!openSet.contains(connectedNode)) {
                        connectedNode.parent = activeNode;
                        connectedNode.distance = activeNode.distance + activeNode.distance(connectedNode);
                        connectedNode.cost = connectedNode.distance;
                        openSet.add(connectedNode);
                    }
                }
            }
        }
        return null;
    }

}
