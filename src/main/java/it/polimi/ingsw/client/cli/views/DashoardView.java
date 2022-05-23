package it.polimi.ingsw.client.cli.views;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.cli.Cursor;
import it.polimi.ingsw.client.cli.Palette;
import it.polimi.ingsw.common.reducedmodel.ReducedGame;

public class DashoardView extends BaseView{

    private final ReducedGame game;

    private static final int DIVIDER_X = 25;

    public DashoardView(ReducedGame game) {
        this.game = game;
    }

    @Override
    public void draw() {
        cursor.paintBackground(Palette.DASHBOARD_BACKGROUND, 1, 1, DIVIDER_X, Cursor.HEIGHT-3);

        var myPlayer = game.getMyPlayer(Client.getInstance().getNickname());
        var otherPlayers = game.getOtherPlayers(Client.getInstance().getNickname());

        cursor.moveToXY(1, 2);
        new PlayerBoardView(myPlayer, game.currentRound(), game.currentProfessors(), game.expertMode())
                .draw();

        for(var p : otherPlayers) {
            cursor.moveRelative(0, 7);
            new PlayerBoardView(p, game.currentRound(), game.currentProfessors(), game.expertMode())
                    .draw();
        }

    }
}
