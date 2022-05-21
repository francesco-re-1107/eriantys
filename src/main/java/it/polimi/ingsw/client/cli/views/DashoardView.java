package it.polimi.ingsw.client.cli.views;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.cli.Cursor;
import it.polimi.ingsw.client.cli.Palette;
import it.polimi.ingsw.common.reducedmodel.ReducedGame;
import it.polimi.ingsw.common.reducedmodel.ReducedPlayer;

import java.util.Comparator;

public class DashoardView extends BaseView{

    private ReducedGame game;

    private static final int DIVIDER_X = 25;

    public DashoardView(ReducedGame game) {
        this.game = game;
    }

    @Override
    public void draw() {
        cursor.paintBackground(Palette.DASHBOARD_BACKGROUND, 1, 1, DIVIDER_X, Cursor.HEIGHT-3);

        var myPlayer = game.players()
                .stream()
                .filter(p -> p.nickname().equals(Client.getInstance().getNickname()))
                .findFirst()
                .orElse(null);

        var otherPlayers = game.players().stream()
                .filter(p -> !p.nickname().equals(Client.getInstance().getNickname()))
                .sorted(Comparator.comparing(ReducedPlayer::nickname))
                .toList();

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
