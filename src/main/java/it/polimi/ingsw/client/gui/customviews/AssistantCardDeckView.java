package it.polimi.ingsw.client.gui.customviews;

import it.polimi.ingsw.server.model.AssistantCard;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class AssistantCardDeckView extends VBox {

    private Consumer<AssistantCard> listener;

    private Label selectionError;

    private final Map<AssistantCard, AssistantCardView> cardViews;

    public AssistantCardDeckView() {
        super();

        cardViews = new HashMap<>(AssistantCard.getDefaultDeck().size());

        setMaxWidth(800);
        setAlignment(Pos.CENTER);
        setSpacing(30);
        setId("card_selection");
        getStylesheets().add(getClass().getResource("/css/assistant_card_deck.css").toExternalForm());
        setupDeck();
    }

    private void setupDeck() {
        //error label
        selectionError = new Label();
        selectionError.setId("error_label");
        hideError();
        getChildren().add(selectionError);

        getChildren().add(new Label("Seleziona una carta"));

        var fp = new FlowPane();
        fp.setAlignment(Pos.CENTER);
        fp.setHgap(30);
        fp.setVgap(30);
        fp.setMaxWidth(780);

        getChildren().add(fp);

        for (var c : AssistantCard.getDefaultDeck()) {
            var acv = new AssistantCardView();
            acv.setCard(c);
            acv.setMaxWidth(120);
            acv.setOnMouseClicked(e -> {
                if (listener != null)
                    listener.accept(c);
            });

            cardViews.put(c, acv);

            fp.getChildren().add(acv);
        }
    }

    public void setDeck(Map<AssistantCard, Boolean> deck) {
        hideError();
        for(var c : AssistantCard.getDefaultDeck()) {
            boolean used = deck.get(c);
            var acv = cardViews.get(c);
            acv.setGrayedOut(used);
            acv.setDisable(used);
        }
    }

    public void setOnCardSelected(Consumer<AssistantCard> listener) {
        this.listener = listener;
    }

    public void showError(String message) {
        Platform.runLater(() -> {
            selectionError.setManaged(true);
            selectionError.setVisible(true);
            selectionError.setText(message);
        });
    }

    private void hideError() {
        Platform.runLater(() -> {
            selectionError.setVisible(false);
            selectionError.setManaged(false);
        });
    }
}
