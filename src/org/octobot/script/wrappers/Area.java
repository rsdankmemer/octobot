package org.octobot.script.wrappers;

import org.octobot.script.Locatable;
import org.octobot.script.util.Random;

import java.util.ArrayList;
import java.util.List;

/**
 * Area
 *
 * @author Pat-ji
 */
public class Area {
    private final int x0, y0, x1, y1, plane;

    private Tile[] tiles;

    public Area(final int x0, final int y0, final int x1, final int y1) {
        this(x0, y0, x1, y1, -2);
    }

    public Area(final int x0, final int y0, final int x1, final int y1, final int plane) {
        this.x0 = Math.min(x0, x1);
        this.y0 = Math.max(y0, y1);
        this.x1 = Math.max(x0, x1);
        this.y1 = Math.min(y0, y1);
        this.plane = plane;
    }

    /**
     * Translates a serialized string of data into the corresponding {@link Area} object.
     *
     * @param serializedData the serialized data, such as "3555, 1245, 3650, 1266, 0"
     * @return the newly created object
     */
    public static Area deserialize(String serializedData) {
        String[] data = serializedData.replace(" ", "").split(",");
        final int x0 = Integer.valueOf(data[0]);
        final int y0 = Integer.valueOf(data[1]);
        final int x1 = Integer.valueOf(data[2]);
        final int y1 = Integer.valueOf(data[3]);
        if (data.length > 4) {
            final int plane = Integer.valueOf(data[4]);
            return new Area(x0, y0, x1, y1, plane);
        }
        return new Area(x0, y0, x1, y1);
    }

    /**
     * Serializes an {@link Area} into a String that can be stored and transmitted.
     *
     * @return a string representing the serialized object
     */
    public String serialize() {
        return x0 + "," + y0 + "," + x1 + "," + y1 + "," + plane;
    }

    public int getMinX() {
        return x0;
    }

    public int getMinY() {
        return y0;
    }

    public int getMaxX() {
        return x1;
    }

    public int getMaxY() {
        return y1;
    }

	/**
	 * This method is used to check the plane of the {@link Area}
	 *
	 * @return the {@link Area}s plane
	 */
    public int getPlane() {
        return plane == -2 ? 0 : plane;
    }

	/**
	 * This method is used to get the {@link Tile} in the centre of the {@link Area}
	 *
	 * @return the {@link Area}s central {@link Tile}
	 */
    public Tile getCentralTile() {
        return new Tile((x0 + x1) / 2, (y0 + y1) / 2, plane == -2 ? 0 : plane);
    }

	/**
	 * This method is used to get a random {@link Tile} in the given {@link Area}
	 *
	 * @return a random {@link Tile} in the {@link Area}
	 */
    public Tile getRandomTile() {
        return new Tile(Random.nextInt(x0, x1), Random.nextInt(y1, y0), plane == -2 ? 0 : plane);
    }

	/**
	 * This method is used to check if the {@link Area} contains a given {@link Locatable}
	 *
	 * @param locatable the {@link Tile} you want to check for
	 * @return <code>true</code> if the {@link Area} contains given {@link Locatable}
	 */
    public boolean contains(final Locatable locatable) {
        return contains(locatable.getLocation());
    }

	/**
	 * This method is used to check if the {@link Area} contains a given {@link Tile}
	 *
	 * @param tile the {@link Tile} you want to check for
	 * @return <code>true</code> if the {@link Area} contains given {@link Tile}
	 */
    public boolean contains(final Tile tile) {
        return tile.getX() >= x0 && tile.getY() <= y0 && tile.getX() <= x1 && tile.getY() >= y1 && (plane == -2 || plane == tile.getPlane());
    }

	/**
	 * This method is used to get every {@link Tile} in the {@link Area}
	 *
	 * @return all the {@link Tile}s in the {@link Area}
	 */
    public Tile[] getTiles() {
        if (tiles == null) {
            final List<Tile> list = new ArrayList<Tile>();
            for (int x = x0; x <= x1; x++)
                for (int y = y1; y <= y0; y++)
                    list.add(new Tile(x, y, plane == -2 ? 0 : plane));

            tiles = list.toArray(new Tile[list.size()]);
        }

        return tiles;
    }

}
