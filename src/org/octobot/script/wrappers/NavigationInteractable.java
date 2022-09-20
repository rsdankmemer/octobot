package org.octobot.script.wrappers;

import org.octobot.script.ScriptContext;
import org.octobot.script.collection.Filter;

/**
 * Created by Joseph on 12/26/2014.
 */
public class NavigationInteractable extends NavigationNode {

    protected String objectName;
    protected String objectOpenAction;
    protected String objectCloseAction;

    protected Area activationArea;

    public String getObjectName() {
        return objectName;
    }

    public String getObjectOpenAction() {
        return objectOpenAction;
    }

    public String getObjectCloseAction() {
        return objectCloseAction;
    }


    public Area getActivationArea() {
        return activationArea;
    }

    public NavigationInteractable(int x, int y, int plane, String objectName, String objectOpenAction, String objectCloseAction, Area activationArea) {
        super(x, y, plane);
        this.objectName = objectName;
        this.objectOpenAction = objectOpenAction;
        this.objectCloseAction = objectCloseAction;
        this.activationArea = activationArea;
    }

    private GameObject getObject(final ScriptContext context) {
        return context.objects.getNearest(this, new Filter<GameObject>() {
            @Override
            public boolean accept(GameObject gameObject) {
                return gameObject.getName().equals(objectName);
            }
        });
    }

    @Override
    public boolean isTraversable(final ScriptContext context) {
        Player localPlayer = context.players.getLocal();
        if (localPlayer == null)
            return false;
        Tile localPlayerLocation = localPlayer.getLocation();
        if (localPlayerLocation.getPlane() != getPlane())
            return false;
        if (localPlayerLocation.distance(this) > 18)
            return false;
        if (!activationArea.contains(localPlayerLocation))
            return false;
        GameObject object = getObject(context);
        if (object == null)
            return false;
        if (!context.calculations.arrayContains(object.getActions(), objectOpenAction))
            return false;
        return true;
    }

    @Override
    public boolean traverse(final ScriptContext context) {
        GameObject object = getObject(context);
        if (object == null)
            return false;
        if (object.isOnGameScreen()) {
            System.out.println("interacting");
            return object.interact(objectOpenAction);
        } else {
            return context.movement.walkTileOnMap(this);
        }
    }


}
