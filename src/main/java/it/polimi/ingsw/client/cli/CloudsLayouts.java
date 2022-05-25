package it.polimi.ingsw.client.cli;

import java.util.Map;

/**
 * This class contains the layouts of the clouds
 * In other words, it contains the coordinates of the top-left cell of every cloud that needs to be drawn in CLI.
 */
public class CloudsLayouts {

    /**
     * For every number of clouds (from 1 to 3) there is a map containing the coordinates of the top-left cell of every cloud.
     * For example, if the current game has 2 clouds, cloudsLayouts.get(2) will contain the coordinates of the top-left cell for
     * both clouds.
     *
     * Indices start at 0 and are relative to a 33x11 rectangle
     */
    private static final Map<Integer, Map<Integer, Point>> cloudsLayouts = Map.ofEntries(
            Map.entry(3, Map.ofEntries(
                    Map.entry(0, new Point(1, 1)),
                    Map.entry(1, new Point(12, 1)),
                    Map.entry(2, new Point(23, 1))
            )),
            Map.entry(2, Map.ofEntries(
                    Map.entry(0, new Point(5, 1)),
                    Map.entry(1, new Point(19, 1))
            )),
            Map.entry(1, Map.ofEntries(
                    Map.entry(0, new Point(12, 1))
            ))
        );

    /**
     * Get starting point (top-left cell) for a given cloud
     * @param numberOfClouds total number of clouds to be drawn
     * @param cloudIndex index of the cloud to be drawn
     * @return the starting point where the cloud should be drawn
     */
    public static Point getPointForCloud(int numberOfClouds, int cloudIndex) {
        return cloudsLayouts.get(numberOfClouds).get(cloudIndex);
    }

    /**
     * Used to represent the coordinates of a cell
     * @param x
     * @param y
     */
    public record Point(int x, int y){}
}
