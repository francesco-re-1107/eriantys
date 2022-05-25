package it.polimi.ingsw.client.cli;

import java.util.Map;

/**
 * This class contains the layouts of the islands
 * In other words, it contains the coordinates of the top-left cell of every island that needs to be drawn in CLI.
 */
public class IslandsLayouts {
    /**
     * For every number of islands (from 3 to 12) there is a map containing the coordinates of the top-left cell of every island.
     * For example, if the current game has 8 islands, islandsLayouts.get(8) will contain the coordinates of the top-left cell for
     * all the 8 islands.
     *
     * Indices start at 0 and are relative to a 55x21 rectangle
     */
    private static final Map<Integer, Map<Integer, Point>> islandsLayouts = Map.ofEntries(
            //12 islands
            Map.entry(12, Map.ofEntries(
                    Map.entry(0, new Point(0, 0)),
                    Map.entry(1, new Point(11, 0)),
                    Map.entry(2, new Point(22, 0)),
                    Map.entry(3, new Point(33, 0)),
                    Map.entry(4, new Point(44, 0)),
                    Map.entry(5, new Point(44, 7)),
                    Map.entry(6, new Point(44, 14)),
                    Map.entry(7, new Point(33, 14)),
                    Map.entry(8, new Point(22, 14)),
                    Map.entry(9, new Point(11, 14)),
                    Map.entry(10, new Point(0, 14)),
                    Map.entry(11, new Point(0, 7))
            )),
            //11 islands
            Map.entry(11, Map.ofEntries(
                    Map.entry(0, new Point(0, 0)),
                    Map.entry(1, new Point(15, 0)),
                    Map.entry(2, new Point(29, 0)),
                    Map.entry(3, new Point(44, 0)),
                    Map.entry(4, new Point(44, 7)),
                    Map.entry(5, new Point(44, 14)),
                    Map.entry(6, new Point(33, 14)),
                    Map.entry(7, new Point(22, 14)),
                    Map.entry(8, new Point(11, 14)),
                    Map.entry(9, new Point(0, 14)),
                    Map.entry(10, new Point(0, 7))
            )),
            //10 islands
            Map.entry(10, Map.ofEntries(
                    Map.entry(0, new Point(0, 0)),
                    Map.entry(1, new Point(15, 0)),
                    Map.entry(2, new Point(29, 0)),
                    Map.entry(3, new Point(44, 0)),
                    Map.entry(4, new Point(44, 7)),
                    Map.entry(5, new Point(44, 14)),
                    Map.entry(6, new Point(29, 14)),
                    Map.entry(7, new Point(15, 14)),
                    Map.entry(8, new Point(0, 14)),
                    Map.entry(9, new Point(0, 7))
            )),
            //9 islands
            Map.entry(9, Map.ofEntries(
                    Map.entry(0, new Point(5, 0)),
                    Map.entry(1, new Point(22, 0)),
                    Map.entry(2, new Point(39, 0)),
                    Map.entry(3, new Point(44, 7)),
                    Map.entry(4, new Point(44, 14)),
                    Map.entry(5, new Point(29, 14)),
                    Map.entry(6, new Point(15, 14)),
                    Map.entry(7, new Point(0, 14)),
                    Map.entry(8, new Point(0, 7))
            )),
            //8 islands
            Map.entry(8, Map.ofEntries(
                    Map.entry(0, new Point(5, 0)),
                    Map.entry(1, new Point(22, 0)),
                    Map.entry(2, new Point(39, 0)),
                    Map.entry(3, new Point(44, 7)),
                    Map.entry(4, new Point(39, 14)),
                    Map.entry(5, new Point(22, 14)),
                    Map.entry(6, new Point(5, 14)),
                    Map.entry(7, new Point(0, 7))
            )),
            //7 islands
            Map.entry(7, Map.ofEntries(
                    Map.entry(0, new Point(11, 0)),
                    Map.entry(1, new Point(33, 0)),
                    Map.entry(2, new Point(44, 7)),
                    Map.entry(3, new Point(39, 14)),
                    Map.entry(4, new Point(22, 14)),
                    Map.entry(5, new Point(5, 14)),
                    Map.entry(6, new Point(0, 7))
            )),
            //6 islands
            Map.entry(6, Map.ofEntries(
                    Map.entry(0, new Point(11, 0)),
                    Map.entry(1, new Point(33, 0)),
                    Map.entry(2, new Point(44, 7)),
                    Map.entry(3, new Point(33, 14)),
                    Map.entry(4, new Point(11, 14)),
                    Map.entry(5, new Point(0, 7))
            )),
            //5 islands
            Map.entry(5, Map.ofEntries(
                    Map.entry(0, new Point(22, 0)),
                    Map.entry(1, new Point(44, 7)),
                    Map.entry(2, new Point(33, 14)),
                    Map.entry(3, new Point(11, 14)),
                    Map.entry(4, new Point(0, 7))
            )),
            //4 islands
            Map.entry(4, Map.ofEntries(
                    Map.entry(0, new Point(22, 0)),
                    Map.entry(1, new Point(44, 7)),
                    Map.entry(2, new Point(22, 14)),
                    Map.entry(3, new Point(0, 7))
            )),
            //3 islands
            Map.entry(3, Map.ofEntries(
                    Map.entry(0, new Point(22, 0)),
                    Map.entry(1, new Point(44, 7)),
                    Map.entry(2, new Point(0, 7))
            ))
    );

    /**
     * Get starting point (top-left cell) for a given island
     * @param numberOfIslands total number of islands to be drawn
     * @param islandIndex index of the island to be drawn
     * @return the starting point where the island should be drawn
     */
    public static Point getPointForIsland(int numberOfIslands, int islandIndex) {
        return islandsLayouts.get(numberOfIslands).get(islandIndex);
    }

    /**
     * Used to represent the coordinates of a cell
     * @param x
     * @param y
     */
    public record Point(int x, int y){}
}
