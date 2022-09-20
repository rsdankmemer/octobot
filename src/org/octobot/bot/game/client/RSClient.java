package org.octobot.bot.game.client;

import java.util.Map;

/**
 * RSClient
 *
 * @author Pat-ji
 */
public interface RSClient {

    public java.awt.Canvas getCanvas();

    public int[][][] getTileHeights();

    public byte[][][] getTileSettings();

    public RSPlayer[] getPlayerArray();

    public RSPlayer getLocalPlayer();

    public RSNPC[] getNpcArray();

    public int getLocalPlayerIndex();

    public RSCollisionMap[] getCollisionMaps();

    public RSNodeList[][][] getGroundItems();

    public int[] getSettings();

    public int[] getWidgetSettings();

    public int getLoginIndex();

    public boolean isOnWorldSelector();

    public int getCameraX();

    public int getCameraY();

    public int getCameraZ();

    public int getCameraCurveX();

    public int getCameraCurveY();

    public int getGameState();

    public int getPlane();

    public int getBaseX();

    public int getBaseY();

    public RSSceneGraph getSceneGraph();

    public RSWidget[][] getWidgets();

    public int[] getRealLevels();

    public int[] getBoostedLevels();

    public int[] getExperience();

    public int getViewRotation();

    public int getMinimapRotation();

    public int getMinimapZoom();

    public int getOptionCount();

    public boolean isMenuOpen();

    public String[] getMenuActions();

    public String[] getMenuOptions();

    public int getMenuX();

    public int getMenuY();

    public int getMenuWidth();

    public int getMenuHeight();

    public int getDestinationX();

    public int getDestinationY();

    public RSCache getGroundItemDefinitionCache();

    public RSCache getGroundItemModelCache();

    public RSCache getGameObjectDefinitionCache();

    public boolean[] getValidWidgets();

    public RSHashTable getWidgetNodes();

    public int[] getWidgetPositionsX();

    public int[] getWidgetPositionsY();

    public int getItemSelected();

    public int getSelectedItemIndex();

    public int getCurrentCrosshair();

    public int getEnergy();

    public RSNodeList getProjectileList();

    public String getLastSelectedItemName();

    public int getCurrentCycle();

    public int getHintIconType();

    public int getHintIconX();

    public int getHintIconY();

    public int getHintIconActorId();

    public int getHintIconId();

    public int getCurrentWorld();

    public RSWorld[] getWorlds();

    public RSNodeIterable getMessages();

    public Map getChatboxChannels();

    public boolean isSpellSelected();

    public String getLastSpellName();

    public RSObjectDefinition getObjectDefinition(final int id);

    public RSNPCDefinition getNPCDefinition(final int id);

    public RSHashTable getItemContainerTable();

    public RSVarpbit[] getVarpbitCache();

}