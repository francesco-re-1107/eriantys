package it.polimi.ingsw.common.responses;

import it.polimi.ingsw.common.reducedmodel.GameListItem;

import java.util.List;

public class GamesListResponse extends Response {

    private final List<GameListItem> gamesList;

    public GamesListResponse(List<GameListItem> games) {
        gamesList = games;
    }

    public List<GameListItem> getGamesList() {
        return gamesList;
    }
}
