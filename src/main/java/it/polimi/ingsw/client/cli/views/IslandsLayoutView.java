package it.polimi.ingsw.client.cli.views;

import it.polimi.ingsw.client.cli.Cursor;
import it.polimi.ingsw.client.cli.IslandsLayouts;
import it.polimi.ingsw.client.cli.Palette;
import it.polimi.ingsw.common.reducedmodel.ReducedIsland;

import java.util.List;

/**
 * This class shows all the islands of the game
 */
public class IslandsLayoutView extends BaseView{

    /**
     * The list of the islands
     */
    private final List<ReducedIsland> islands;

    /**
     * Index of mother nature
     */
    private final int motherNaturePosition;

    /**
     * x coordinate of the starting point of the rectangle where islands are displayed
     */
    private static final int DIVIDER_X = 24;

    /**
     * Create a new IslandsLayoutView
     * @param islands the list of the islands to be displayed
     * @param motherNaturePosition index of mother nature
     */
    public IslandsLayoutView(List<ReducedIsland> islands, int motherNaturePosition) {
        this.islands = islands;
        this.motherNaturePosition = motherNaturePosition;
    }

    @Override
    public void draw() {
        cursor.paintBackground(Palette.ISLAND_BACKGROUND, DIVIDER_X, 1, Cursor.WIDTH - 1, Cursor.HEIGHT-3);

        //print every island using the IslandsLayouts data
        for(int i = 0; i < islands.size(); i++) {
            int x = IslandsLayouts.getPointForIsland(islands.size(), i).x() + DIVIDER_X + 1;
            int y = IslandsLayouts.getPointForIsland(islands.size(), i).y() + 1;
            cursor.moveToXY(x, y);
            new IslandView(islands.get(i), i, i == motherNaturePosition).draw();
        }
    }
}
