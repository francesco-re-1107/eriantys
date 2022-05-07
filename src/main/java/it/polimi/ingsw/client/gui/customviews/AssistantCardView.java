package it.polimi.ingsw.client.gui.customviews;

import it.polimi.ingsw.server.model.AssistantCard;
import javafx.animation.FadeTransition;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.InputStream;
import java.util.Optional;

public class AssistantCardView extends StackPane implements EventHandler<MouseEvent> {

    private Optional<AssistantCard> card = Optional.empty();

    private final ImageView image;

    private final VBox description;
    private final Label turnPriorityLabel;
    private final Label motherNatureLabel;

    public AssistantCardView() {
        super();

        setId("assistant_card");
        image = new ImageView();

        image.setImage(new Image(getImageStream()));
        image.setPreserveRatio(true);

        image.setFitHeight(150);
        setAlignment(Pos.TOP_CENTER);

        description = new VBox();
        description.setMaxHeight(150);
        description.setOpacity(0);
        description.setSpacing(15);
        description.setId("assistant_card_description");
        description.setAlignment(Pos.CENTER);
        turnPriorityLabel = new Label("");
        motherNatureLabel = new Label("");

        description.getChildren().add(turnPriorityLabel);
        description.getChildren().add(motherNatureLabel);

        getChildren().add(image);
        getChildren().add(description);

        setOnMouseEntered(this);
        setOnMouseExited(this);
    }

    //TODO: fix
    public AssistantCardView(AssistantCard card) {
        this();
        this.card = Optional.of(card);
    }

    public void setGrayedOut(boolean grayedOut) {
        var desaturate = new ColorAdjust();
        desaturate.setSaturation(-1);

        var original = new ColorAdjust();
        original.setSaturation(0);

        if (grayedOut) {
            image.setEffect(desaturate);
        } else {
            image.setEffect(original);
        }
    }

    public AssistantCardView(int index) {
        this(AssistantCard.getDefaultDeck().get(index));
    }

    public Optional<AssistantCard> getCard() {
        return card;
    }

    public void setEmpty() {
        this.card = Optional.empty();
        updateView();
    }

    public void setCard(AssistantCard card) {
        this.card = Optional.of(card);
        updateView();
    }

    private void updateView() {
        image.setImage(new Image(getImageStream()));

        if(card.isPresent()) {
            turnPriorityLabel.setText(String.valueOf(card.get().turnPriority()));
            motherNatureLabel.setText(String.valueOf(card.get().motherNatureMaxMoves()));
        }
    }

    private InputStream getImageStream() {

        if(card.isPresent()){
            var path = "/assets/assistant_cards/" + card.get().turnPriority() + "_" +  card.get().motherNatureMaxMoves() + ".png";
            return getClass().getResourceAsStream(path);
        }else{
            return getClass().getResourceAsStream("/assets/assistant_cards/empty.png");
        }
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        if(card.isEmpty()) return;

        if(mouseEvent.getEventType() == MouseEvent.MOUSE_ENTERED){
            FadeTransition ft = new FadeTransition(Duration.millis(150), description);
            ft.setFromValue(0);
            ft.setToValue(1);
            ft.play();
        }else if(mouseEvent.getEventType() == MouseEvent.MOUSE_EXITED){
            setCursor(Cursor.DEFAULT);
            FadeTransition ft = new FadeTransition(Duration.millis(150), description);
            ft.setFromValue(1);
            ft.setToValue(0);
            ft.play();
        }
    }
}
