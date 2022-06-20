package it.polimi.ingsw.common.responses.replies;

import it.polimi.ingsw.common.reducedmodel.GameListItem;
import it.polimi.ingsw.common.responses.Reply;

import java.io.Serial;
import java.util.List;
import java.util.UUID;

/**
 * This reply is sent by the server to the client in response to a ListGamesRequest.
 */
public class GamesListReply extends Reply {

    @Serial
    private static final long serialVersionUID = 6106487113861465645L;

    private final List<GameListItem> gamesList;

    public GamesListReply(UUID requestId, List<GameListItem> games) {
        super(requestId, true);
        gamesList = games;
    }

    public List<GameListItem> getGamesList() {
        return gamesList;
    }
}
