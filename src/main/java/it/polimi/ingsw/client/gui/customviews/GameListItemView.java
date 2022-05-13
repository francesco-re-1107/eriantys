package it.polimi.ingsw.client.gui.customviews;

import it.polimi.ingsw.common.reducedmodel.GameListItem;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;

import java.util.UUID;

public class GameListItemView extends HBox {

    private final GameListItem item;
    private final ImageView playerIcon;
    private final Label playersLabel;
    private final Label expertLabel;
    private final Button joinButton;

    public GameListItemView() {
        this(
                new GameListItem(UUID.randomUUID(), 3, 1, true)
        );
    }

    public GameListItemView(GameListItem item) {
        super();
        this.item = item;

        setId("game_list_item");
        setPadding(new Insets(20));
        setSpacing(35);
        setAlignment(Pos.CENTER);

        playerIcon = new ImageView(new Image(getClass().getResourceAsStream("/assets/player_icon.png")));
        playerIcon.setFitWidth(30);
        playerIcon.setPreserveRatio(true);

        playersLabel = new Label(item.currentNumberOfPlayers() + "/" + item.numberOfPlayers());
        expertLabel = new Label(item.expertMode() ? "ESPERTI" : "SEMPLICE");
        expertLabel.setTextFill(Paint.valueOf(item.expertMode() ? "#F00" : "#FFF"));
        joinButton = new Button("UNISCITI");

        getChildren().addAll(playerIcon, playersLabel, expertLabel, joinButton);
    }

    public void setOnJoinButtonClicked(EventHandler<MouseEvent> event) {
        joinButton.setOnMouseClicked(event);
    }

}
