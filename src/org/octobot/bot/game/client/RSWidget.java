package org.octobot.bot.game.client;

/**
 * RSWidget
 *
 * @author Pat-ji
 */
public interface RSWidget {

    public int getId();

    public int getParentId();

    public RSWidget[] getChildren();

    public int[] getItems();

    public int[] getItemsStacks();

    public int getX();

    public int getY();

    public int getWidth();

    public int getHeight();

    public int getModelType();

    public int getTextureId();

    public int getModelId();

    public String[] getActions();

    public int getBorderThickness();

    public boolean isHidden();

    public int[][] getChildPositions();

    public int getBoundsIndex();

    public int getType();

    public String getText();

    public int getTextColor();

    public int getOpacity();

    public int getScrollBarH();

    public int getScrollBarV();

    public String getComponentName();

    public int getComponentId();

    public int getItemStackSize();

    public int getMasterX();

    public int getMasterY();

}