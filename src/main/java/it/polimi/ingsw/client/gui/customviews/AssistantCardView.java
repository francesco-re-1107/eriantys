package it.polimi.ingsw.client.gui.customviews;

import it.polimi.ingsw.server.model.AssistantCard;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.InputStream;
import java.util.Optional;

public class AssistantCardView extends ImageView {

    private Optional<AssistantCard> card = Optional.empty();

    public AssistantCardView() {
        super();

        setImage(new Image(getImageStream()));
    }

    public AssistantCardView(AssistantCard card) {
        this();
        this.card = Optional.of(card);
    }

    public AssistantCardView(int index) {
        this(AssistantCard.getDefaultDeck().get(index));
    }

    private InputStream getImageStream() {

        if(card.isPresent()){
            var path = "/assets/assistant_cards/" + card.get().turnPriority() + "_" +  card.get().motherNatureMaxMoves() + ".png";
            return getClass().getResourceAsStream(path);
        }else{
            return getClass().getResourceAsStream("/assets/assistant_cards/empty.png");
        }
    }
}
