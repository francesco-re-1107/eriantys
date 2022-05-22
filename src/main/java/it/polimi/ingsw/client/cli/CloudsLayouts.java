package it.polimi.ingsw.client.cli;

import java.util.Map;

public class CloudsLayouts {

    //clouds layouts relative to a 33x11 recatngle
    //indices starts at 0
    private static final Map<Integer, Map<Integer, Point>> cloudsLayout = Map.ofEntries(
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

    public static Point getPointForCloud(int numberOfClouds, int ccloudIndex) {
        return cloudsLayout.get(numberOfClouds).get(ccloudIndex);
    }

    public record Point(int x, int y){}
}
