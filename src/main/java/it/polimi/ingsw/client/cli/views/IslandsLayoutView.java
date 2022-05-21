package it.polimi.ingsw.client.cli.views;

import it.polimi.ingsw.client.cli.Cursor;
import it.polimi.ingsw.client.cli.IslandsLayouts;
import it.polimi.ingsw.client.cli.Palette;
import it.polimi.ingsw.common.reducedmodel.ReducedIsland;

import java.util.List;

public class IslandsLayoutView extends BaseView{

    private final List<ReducedIsland> islands;
    private final int motherNaturePosition;
    private static final int DIVIDER_X = 26;

    public IslandsLayoutView(List<ReducedIsland> islands, int motherNaturePosition) {
        this.islands = islands;
        this.motherNaturePosition = motherNaturePosition;
    }

    @Override
    public void draw() {
        cursor.paintBackground(Palette.ISLAND_BACKGROUND, DIVIDER_X, 1, Cursor.WIDTH, Cursor.HEIGHT-3);

        for(int i = 0; i < islands.size(); i++) {
            int x = IslandsLayouts.getPointForIsland(islands.size(), i).x() + DIVIDER_X;
            int y = IslandsLayouts.getPointForIsland(islands.size(), i).y();
            cursor.moveToXY(x, y);
            new IslandView(islands.get(i), i, i == motherNaturePosition).draw();
        }
    }
}
