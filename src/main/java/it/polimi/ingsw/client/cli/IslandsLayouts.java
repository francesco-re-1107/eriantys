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
            //TODO from here
            Map.entry(11, Map.ofEntries(
                    Map.entry(0, new Point(1, 0)),
                    Map.entry(1, new Point(1, 1)),
                    Map.entry(2, new Point(1, 2)),
                    Map.entry(3, new Point(1, 3)),
                    Map.entry(4, new Point(1, 4)),
                    Map.entry(5, new Point(1, 5)),
                    Map.entry(6, new Point(1, 6)),
                    Map.entry(7, new Point(1, 7)),
                    Map.entry(8, new Point(1, 8)),
                    Map.entry(9, new Point(1, 9)),
                    Map.entry(10, new Point(1, 10))
            )),
            Map.entry(10, Map.ofEntries(
                    Map.entry(0, new Point(2, 0)),
                    Map.entry(1, new Point(2, 1)),
                    Map.entry(2, new Point(2, 2)),
                    Map.entry(3, new Point(2, 3)),
                    Map.entry(4, new Point(2, 4)),
                    Map.entry(5, new Point(2, 5)),
                    Map.entry(6, new Point(2, 6)),
                    Map.entry(7, new Point(2, 6)),
                    Map.entry(8, new Point(2, 6)),
                    Map.entry(9, new Point(2, 6))
            )),
            Map.entry(9, Map.ofEntries(
                    Map.entry(0, new Point(2, 0)),
                    Map.entry(1, new Point(2, 1)),
                    Map.entry(2, new Point(2, 2)),
                    Map.entry(3, new Point(2, 3)),
                    Map.entry(4, new Point(2, 4)),
                    Map.entry(5, new Point(2, 5)),
                    Map.entry(6, new Point(2, 6)),
                    Map.entry(7, new Point(2, 6)),
                    Map.entry(8, new Point(2, 6))
            )),
            Map.entry(8, Map.ofEntries(
                    Map.entry(0, new Point(2, 0)),
                    Map.entry(1, new Point(2, 1)),
                    Map.entry(2, new Point(2, 2)),
                    Map.entry(3, new Point(2, 3)),
                    Map.entry(4, new Point(2, 4)),
                    Map.entry(5, new Point(2, 5)),
                    Map.entry(6, new Point(2, 6)),
                    Map.entry(7, new Point(2, 6))
            )),
            Map.entry(7, Map.ofEntries(
                    Map.entry(0, new Point(2, 0)),
                    Map.entry(1, new Point(2, 1)),
                    Map.entry(2, new Point(2, 2)),
                    Map.entry(3, new Point(2, 3)),
                    Map.entry(4, new Point(2, 4)),
                    Map.entry(5, new Point(2, 5)),
                    Map.entry(6, new Point(2, 6))
            )),
            Map.entry(6, Map.ofEntries(
                    Map.entry(0, new Point(2, 0)),
                    Map.entry(1, new Point(2, 1)),
                    Map.entry(2, new Point(2, 2)),
                    Map.entry(3, new Point(2, 3)),
                    Map.entry(4, new Point(2, 4)),
                    Map.entry(5, new Point(2, 5))
            )),
            Map.entry(5, Map.ofEntries(
                    Map.entry(0, new Point(2, 0)),
                    Map.entry(1, new Point(2, 1)),
                    Map.entry(2, new Point(2, 2)),
                    Map.entry(3, new Point(2, 3)),
                    Map.entry(4, new Point(2, 4))
            )),
            Map.entry(4, Map.ofEntries(
                    Map.entry(0, new Point(2, 0)),
                    Map.entry(1, new Point(2, 1)),
                    Map.entry(2, new Point(2, 2)),
                    Map.entry(3, new Point(2, 3))
            )),
            Map.entry(3, Map.ofEntries(
                    Map.entry(0, new Point(2, 0)),
                    Map.entry(1, new Point(2, 1)),
                    Map.entry(2, new Point(2, 2))
            ))
    );

    public static Point getPointForIsland(int numberOfIslands, int islandIndex) {
        return islandsLayout.get(numberOfIslands).get(islandIndex);
    }

    public record Point(int x, int y){}
}
