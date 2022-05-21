package it.polimi.ingsw.client.cli;

import java.util.Map;

public class IslandsLayouts {
    //layout islands in a 55x21
    //indices starts at 1
    private static final Map<Integer, Map<Integer, Point>> islandsLayout = Map.ofEntries(
            Map.entry(12, Map.ofEntries(
                    Map.entry(0, new Point(1, 1)),
                    Map.entry(1, new Point(12, 1)),
                    Map.entry(2, new Point(23, 1)),
                    Map.entry(3, new Point(34, 1)),
                    Map.entry(4, new Point(45, 1)),
                    Map.entry(5, new Point(45, 8)),
                    Map.entry(6, new Point(45, 15)),
                    Map.entry(7, new Point(34, 15)),
                    Map.entry(8, new Point(23, 15)),
                    Map.entry(9, new Point(12, 15)),
                    Map.entry(10, new Point(1, 15)),
                    Map.entry(11, new Point(1, 8))
            )),
            Map.entry(11, Map.ofEntries(
                    Map.entry(0, new Point(1, 1)),
                    Map.entry(1, new Point(16, 1)),
                    Map.entry(2, new Point(30, 1)),
                    Map.entry(3, new Point(45, 1)),
                    Map.entry(4, new Point(45, 8)),
                    Map.entry(5, new Point(45, 15)),
                    Map.entry(6, new Point(34, 15)),
                    Map.entry(7, new Point(23, 15)),
                    Map.entry(8, new Point(12, 15)),
                    Map.entry(9, new Point(1, 15)),
                    Map.entry(10, new Point(1, 8))
            )),
            Map.entry(10, Map.ofEntries(
                    Map.entry(0, new Point(1, 1)),
                    Map.entry(1, new Point(16, 1)),
                    Map.entry(2, new Point(30, 1)),
                    Map.entry(3, new Point(45, 1)),
                    Map.entry(4, new Point(45, 8)),
                    Map.entry(5, new Point(45, 15)),
                    Map.entry(6, new Point(30, 15)),
                    Map.entry(7, new Point(16, 15)),
                    Map.entry(8, new Point(1, 15)),
                    Map.entry(9, new Point(1, 8))
            )),
            Map.entry(9, Map.ofEntries(
                    Map.entry(0, new Point(6, 1)),
                    Map.entry(1, new Point(23, 1)),
                    Map.entry(2, new Point(40, 1)),
                    Map.entry(3, new Point(45, 8)),
                    Map.entry(4, new Point(45, 15)),
                    Map.entry(5, new Point(30, 15)),
                    Map.entry(6, new Point(16, 15)),
                    Map.entry(7, new Point(1, 15)),
                    Map.entry(8, new Point(1, 8))
            )),
            Map.entry(8, Map.ofEntries(
                    Map.entry(0, new Point(6, 1)),
                    Map.entry(1, new Point(23, 1)),
                    Map.entry(2, new Point(40, 1)),
                    Map.entry(3, new Point(45, 8)),
                    Map.entry(4, new Point(40, 15)),
                    Map.entry(5, new Point(23, 15)),
                    Map.entry(6, new Point(6, 15)),
                    Map.entry(7, new Point(1, 8))
            )),
            Map.entry(7, Map.ofEntries(
                    Map.entry(0, new Point(12, 1)),
                    Map.entry(1, new Point(34, 1)),
                    Map.entry(2, new Point(45, 8)),
                    Map.entry(3, new Point(40, 15)),
                    Map.entry(4, new Point(23, 15)),
                    Map.entry(5, new Point(6, 15)),
                    Map.entry(6, new Point(1, 8))
            )),
            Map.entry(6, Map.ofEntries(
                    Map.entry(0, new Point(12, 1)),
                    Map.entry(1, new Point(34, 1)),
                    Map.entry(2, new Point(45, 8)),
                    Map.entry(3, new Point(34, 15)),
                    Map.entry(4, new Point(12, 15)),
                    Map.entry(5, new Point(1, 8))
            )),
            Map.entry(5, Map.ofEntries(
                    Map.entry(0, new Point(23, 1)),
                    Map.entry(1, new Point(45, 8)),
                    Map.entry(2, new Point(34, 15)),
                    Map.entry(3, new Point(12, 15)),
                    Map.entry(4, new Point(1, 8))
            )),
            Map.entry(4, Map.ofEntries(
                    Map.entry(0, new Point(23, 1)),
                    Map.entry(1, new Point(45, 8)),
                    Map.entry(2, new Point(23, 15)),
                    Map.entry(3, new Point(1, 8))
            )),
            Map.entry(3, Map.ofEntries(
                    Map.entry(0, new Point(23, 1)),
                    Map.entry(1, new Point(45, 8)),
                    Map.entry(2, new Point(1, 8))
            ))
    );

    public static Point getPointForIsland(int numberOfIslands, int islandIndex) {
        return islandsLayout.get(numberOfIslands).get(islandIndex);
    }

    public record Point(int x, int y){}
}
