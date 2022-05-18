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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * This class shows an assistant card if present or the empty card.
 */
public class AssistantCardView extends StackPane implements EventHandler<MouseEvent> {

    private Optional<AssistantCard> card = Optional.empty();

    private ImageView image;

    private VBox description;
    private Label turnPriorityLabel;
    private Label motherNatureLabel;

    private static final Image emptyImage;
    private static final Map<String, Image> assistantCardImages = new HashMap<>();

    //load images statically
    static {
        emptyImage = new Image(AssistantCardView.class.getResourceAsStream("/assets/assistant_cards/empty.png"));

        for(AssistantCard c : AssistantCard.getDefaultDeck()) {
            var name = c.getName();
            var path = "/assets/assistant_cards/" + name + ".png";
            assistantCardImages.put(name, new Image(AssistantCardView.class.getResourceAsStream(path)));
        }
    }

    public AssistantCardView() {
        this(null);
    }

    public AssistantCardView(AssistantCard card) {
        super();

        setId("assistant_card");
        setAlignment(Pos.TOP_CENTER);
        getStylesheets().add(getClass().getResource("/css/assistant_card.css").toExternalForm());
        setupElements();

        setOnMouseEntered(this);
        setOnMouseExited(this);

        setCard(card);
    }

    private void setupElements() {
        image = new ImageView();
        image.setPreserveRatio(true);
        image.setFitHeight(150);

        description = new VBox();
        description.setMaxHeight(150);
        description.setOpacity(0);
        description.setSpacing(15);
        description.setId("assistant_card_description");
        description.setAlignment(Pos.CENTER);

        turnPriorityLabel = new Label("");
        motherNatureLabel = new Label("");

        description.getChildren().addAll(turnPriorityLabel, motherNatureLabel);

        getChildren().addAll(image, description);
    }


    /**
     * Set the card as greyed out
     * @param grayedOut
     */
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

    /**
     * Set the assistant card to show
     * @param card the card to show, null for empty
     */
    public void setCard(AssistantCard card) {
        this.card = Optional.ofNullable(card);

        if(card != null) {
            image.setImage(assistantCardImages.get(card.getName()));
            turnPriorityLabel.setText(String.valueOf(card.turnPriority()));
            motherNatureLabel.setText(String.valueOf(card.motherNatureMaxMoves()));
        } else {
            image.setImage(emptyImage);
        }
    }

    /**
     * Get the current card
     * @return the current card
     */
    public Optional<AssistantCard> getCard() {
        return card;
    }

    /**
     * This method is called when the mouse enters and exits this view
     * It is used to show and hide the description
     * @param mouseEvent
     */
    @Override
    public void handle(MouseEvent mouseEvent) {
        if(card.isEmpty()) return;

        if(mouseEvent.getEventType() == MouseEvent.MOUSE_ENTERED){
            var ft = new FadeTransition(Duration.millis(150), description);
            ft.setFromValue(0);
            ft.setToValue(1);
            ft.play();
        }else if(mouseEvent.getEventType() == MouseEvent.MOUSE_EXITED){
            setCursor(Cursor.DEFAULT);
            var ft = new FadeTransition(Duration.millis(150), description);
            ft.setFromValue(1);
            ft.setToValue(0);
            ft.play();
        }
    }
}
