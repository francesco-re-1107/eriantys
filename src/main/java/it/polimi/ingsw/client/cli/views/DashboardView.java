package it.polimi.ingsw.client.cli.views;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.cli.Cursor;
import it.polimi.ingsw.client.cli.Palette;
import it.polimi.ingsw.common.reducedmodel.ReducedGame;

/**
 * This view is used to show the dashboard of the game.
 */
public class DashboardView extends BaseView{

    /**
     * Reference to the last game update
     */
    private final ReducedGame game;

    /**
     * x coordinate of the ending point of the rectangle where the dashboard is displayed
     */
    private static final int DIVIDER_X = 24;

    /**
     * Create a DashboardView for the given game update
     * @param game the game update
     */
    public DashboardView(ReducedGame game) {
        this.game = game;
    }

    @Override
    public void draw() {
        //paint background
        cursor.paintBackground(Palette.DASHBOARD_BACKGROUND, 1, 1, DIVIDER_X, Cursor.HEIGHT-3);

        var myPlayer = game.getMyPlayer(Client.getInstance().getNickname());
        var otherPlayers = game.getOtherPlayers(Client.getInstance().getNickname());

        //print my player board
        cursor.moveToXY(1, 2);
        new PlayerBoardView(myPlayer, game.currentRound(), game.currentProfessors(), game.expertMode())
                .draw();

        //print other players' board
        for(var p : otherPlayers) {
            cursor.moveRelative(0, 7);
            new PlayerBoardView(p, game.currentRound(), game.currentProfessors(), game.expertMode())
                    .draw();
        }

    }
}
