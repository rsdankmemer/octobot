package org.octobot.bot.internal.ui.component;

import org.octobot.bot.Environment;
import org.octobot.script.methods.Navigation;
import org.octobot.script.wrappers.Area;
import org.octobot.script.wrappers.NavigationInteractable;
import org.octobot.script.wrappers.NavigationNode;
import org.octobot.script.wrappers.Tile;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Joseph on 12/20/2014.
 */
public class WebPanel extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener, KeyListener {

    private Point lastMouseLocation;
    private Point dragStartLocation;

    private Image mapImage;
    private Point mapPosition; // Originated at top left [0, 0]
    private double mapZoom;

    private Tile interactableLocation;
    private Tile interactableCorner;

    private int connectionRadius = 6;

    private WebPanelMode mode = WebPanelMode.VIEW;

    public WebPanel() {
        super(true);
        setFocusable(true);
        try {
            File map = new File(Environment.getDataDirectory() + File.separator + "map.png");
            if (map.exists())
                mapImage = ImageIO.read(map);
            mapPosition = new Point(2000, 2000);
            mapZoom = 1.0d;
        } catch (IOException e) {
            e.printStackTrace();
        }
        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
        addKeyListener(this);
    }

    public void dispose() {
        mapImage = null;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        if (mapImage != null) {
            int adjustedSourceWidth = (int) (getWidth() / mapZoom);
            int adjustedSourceHeight = (int) (getHeight() / mapZoom);
            g2d.drawImage(mapImage, 0, 0, getWidth(), getHeight(),mapPosition.x, mapPosition.y,
                    mapPosition.x + adjustedSourceWidth, mapPosition.y + adjustedSourceHeight, null);
        } else {
            g.setFont(new Font("SansSerif", Font.PLAIN, 16));
            String message = "Map file not found in " + Environment.getDataDirectory() + File.separator + "map.png";
            g.setColor(Color.black);
            g.drawString(message, 11, 61);
            g.setColor(Color.white);
            g.drawString(message, 10, 60);
        }

        g.setFont(new Font("SansSerif", Font.PLAIN, 10));

        HashMap<Integer, NavigationNode> loadedNodes = Navigation.getLoadedNodes();
        for (int key : loadedNodes.keySet()) {
            NavigationNode node = loadedNodes.get(key);
            Point screenPoint = mapPointToScreenPoint(tileToMapPoint(node));
            int width = (int) Math.ceil(2*mapZoom);
            if (node instanceof NavigationInteractable) {
                NavigationInteractable door = (NavigationInteractable) node;
                g2d.setColor(new Color(0, 255, 0, 96));
                Area area = door.getActivationArea();
                Point areaMin = mapPointToScreenPoint(tileToMapPoint(new Tile(area.getMinX(), area.getMinY())));
                Point areaMax = mapPointToScreenPoint(tileToMapPoint(new Tile(area.getMaxX(), area.getMaxY())));
                g2d.fillRect(areaMin.x, areaMin.y, areaMax.x - areaMin.x, areaMax.y - areaMin.y);
                g2d.setColor(Color.green);
            } else {
                g2d.setColor(Color.blue);
            }
            g2d.fillRect(screenPoint.x - (width/2), screenPoint.y - (width/2), width, width);
            for (Integer id : node.getConnections()) {
                NavigationNode connectedNode = Navigation.getNode(id);
                Point connectedScreenPoint = mapPointToScreenPoint(tileToMapPoint(connectedNode));
                g.drawLine(screenPoint.x, screenPoint.y, connectedScreenPoint.x, connectedScreenPoint.y);
            }
            g.setColor(Color.white);
            if (mapZoom > 2.0d) {
                g.drawString(String.valueOf(key), screenPoint.x + 5, screenPoint.y);
            }
        }

        if (lastMouseLocation != null && (mode == WebPanelMode.CREATE_NODE || mode == WebPanelMode.CREATE_INTERACTABLE)) {
            int pixelRadius = (int) (connectionRadius * 3 *mapZoom);
            g.setColor(Color.green);
            g2d.drawArc(lastMouseLocation.x - pixelRadius, lastMouseLocation.y - pixelRadius, pixelRadius * 2, pixelRadius * 2, 0, 360);
            g.setColor(Color.black);
            g.drawString("r=" + connectionRadius, lastMouseLocation.x, lastMouseLocation.y);
            g.setColor(Color.white);
            g.drawString("r=" + connectionRadius, lastMouseLocation.x, lastMouseLocation.y);
        }

        g.setFont(new Font("SansSerif", Font.PLAIN, 16));
        g.setColor(Color.black);
        g.drawString("Editor Mode: ", 11, 31);
        g.setColor(Color.white);
        g.drawString("Editor Mode: ", 10, 30);
        if(lastMouseLocation != null) {
            Tile mouseTile = mapPointToTile(screenPointToMapPoint(lastMouseLocation));

        g.setColor(Color.black);
        g.drawString("Mouse Tile: " + mouseTile.toString(), 11, 101);
        g.setColor(Color.white);
        g.drawString("Mouse Tile: " + mouseTile.toString(), 10, 100);
        switch (mode) { // todo remove spaghetti by adding shit to enums
            case VIEW:
                g.setColor(Color.black);
                g.drawString("View", 106, 31);
                g.setColor(Color.yellow);
                g.drawString("View", 105, 30);
                break;
            case CREATE_NODE:
                g.setColor(Color.black);
                g.drawString("Create Node", 106, 31);
                g.setColor(Color.blue);
                g.drawString("Create Node", 105, 30);
                break;
            case CREATE_INTERACTABLE:
                g.setColor(Color.black);
                g.drawString("Create Interactable Node", 106, 31);
                g.setColor(Color.green);
                g.drawString("Create Interactable Node", 105, 30);
                if (interactableLocation == null) {
                    g.setColor(Color.black);
                    g.drawString("Click the exact location of the object.", 106, 51);
                    g.setColor(Color.yellow);
                    g.drawString("Click the exact location of the object.", 105, 50);
                } else {
                    if (interactableCorner == null) {
                        g.setColor(Color.black);
                        g.drawString("Click the first corner of the activation area.", 106, 51);
                        g.setColor(Color.green);
                        g.drawString("Click the first corner of the activation area.", 105, 50);
                    } else {
                        g.setColor(Color.black);
                        g.drawString("Click the second corner of the activation area.", 106, 51);
                        g.setColor(Color.magenta);
                        g.drawString("Click the second corner of the activation area.", 105, 50);
                    }
                }
            }
        }
    }

    private void pan(int deltaX, int deltaY) {
        mapPosition.translate((int)Math.round(-deltaX / mapZoom), (int)Math.round(-deltaY / mapZoom));
        repaint();
    }

    private void zoom(double multiplier, Point zoomPoint) {
        Point mapCoordinates = screenPointToMapPoint(zoomPoint);
        mapZoom *= multiplier;
        if (mapZoom < 0.15d)
            mapZoom = 0.15d;
        if (mapZoom > 7.0d)
            mapZoom = 7.0d;
        Point newMapCoordinates = screenPointToMapPoint(zoomPoint);
        mapPosition.translate(mapCoordinates.x - newMapCoordinates.x, mapCoordinates.y - newMapCoordinates.y);

        repaint();
    }

    private Point screenPointToMapPoint(Point point) {
        return new Point(mapPosition.x + (int)(point.x/mapZoom), mapPosition.y + (int)(point.y/mapZoom));
    }

    private Point mapPointToScreenPoint(Point point) {
        return new Point((int)((point.x - mapPosition.x + 1)*mapZoom), (int)((point.y - mapPosition.y + 1)*mapZoom));
    }

    private Tile mapPointToTile(Point point) {
        return new Tile(point.x / 3 + 1984, point.y / -3 + 4223);
    }

    private Point tileToMapPoint(Tile tile) {
        return new Point((tile.getX() - 1984) * 3, (tile.getY() - 4223) * -3);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (mode == WebPanelMode.CREATE_NODE) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                Tile tile = mapPointToTile(screenPointToMapPoint(e.getPoint()));
                NavigationNode newNode = new NavigationNode(tile.getX(), tile.getY(), 0);
                int index = Navigation.addNode(newNode);
                for (Integer key : Navigation.getLoadedNodes().keySet()) {
                    if (key == index)
                        continue;
                    NavigationNode node = Navigation.getNode(key);
                    if (node.distance(newNode) < connectionRadius) {
                        newNode.addConnection(key);
                        node.addConnection(index);
                    }
                }
                repaint();
            } else {
                Tile tile = mapPointToTile(screenPointToMapPoint(e.getPoint()));
                ArrayList<Integer> toDelete = new ArrayList<Integer>();
                for (Integer key : Navigation.getLoadedNodes().keySet()) {
                    NavigationNode node = Navigation.getNode(key);
                    if (node.distance(tile) < connectionRadius) {
                        toDelete.add(key);
                    }
                }
                for (Integer key : toDelete) {
                    Navigation.deleteNode(key);
                }
                repaint();
            }
        } else if (mode == WebPanelMode.CREATE_INTERACTABLE) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                if (interactableLocation == null) {
                    interactableLocation = mapPointToTile(screenPointToMapPoint(e.getPoint()));
                } else {
                    if (interactableCorner == null) {
                        interactableCorner = mapPointToTile(screenPointToMapPoint(e.getPoint()));
                    } else {
                        Tile secondCorner = mapPointToTile(screenPointToMapPoint(e.getPoint()));
                        NavigationInteractable newNode = new NavigationInteractable(interactableLocation.getX(), interactableLocation.getY(), 0, "ObjectName", "OpenDoor", "CloseDoor", new Area(interactableCorner.getX(), interactableCorner.getY(), secondCorner.getX(), secondCorner.getY()));
                        int index = Navigation.addNode(newNode);
                        for (Integer key : Navigation.getLoadedNodes().keySet()) {
                            if (key == index)
                                continue;
                            NavigationNode node = Navigation.getNode(key);
                            if (node.distance(newNode) < connectionRadius) {
                                newNode.addConnection(key);
                                node.addConnection(index);
                            }
                        }
                        repaint();

                        interactableLocation = null;
                        interactableCorner = null;
                    }
                }
                repaint();
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        dragStartLocation = null;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (dragStartLocation != null)
            pan(e.getX() - dragStartLocation.x, e.getY() - dragStartLocation.y);
        dragStartLocation = e.getPoint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        boolean shouldRepaint = (lastMouseLocation == null || e.getX() != lastMouseLocation.x || e.getY() != lastMouseLocation.getY());
        lastMouseLocation = e.getPoint();
        if (shouldRepaint)
            repaint();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        double point = e.getPreciseWheelRotation();
        if (mode == WebPanelMode.VIEW || mode == WebPanelMode.CREATE_INTERACTABLE) {
            if (point < 0) {
                zoom(1.1d, e.getPoint());
            } else {
                zoom(1/1.1d, e.getPoint());
            }
            repaint();
        } else if (mode == WebPanelMode.CREATE_NODE) {
            if (point < 0) {
                connectionRadius = Math.min(17, connectionRadius + 1);
            } else {
                connectionRadius = Math.max(1, connectionRadius - 1);
            }
            repaint();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case 49:
                mode = WebPanelMode.VIEW;
                repaint();
                break;
            case 50:
                mode = WebPanelMode.CREATE_NODE;
                repaint();
                break;
            case 51:
                mode = WebPanelMode.CREATE_INTERACTABLE;
                repaint();
                break;
            case 83:
                Navigation.saveData();
                repaint();
                break;
            case 76:
                Navigation.loadData();
                repaint();
                break;
            default:
                System.out.println(e.getKeyCode());
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public enum WebPanelMode {
        VIEW, CREATE_NODE, CREATE_INTERACTABLE
    }
}
