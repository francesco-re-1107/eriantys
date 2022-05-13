package it.polimi.ingsw.client.gui.customviews;

import it.polimi.ingsw.common.reducedmodel.ReducedCharacterCard;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Optional;

public class CharacterCardView extends ImageView {
    private Optional<ReducedCharacterCard> card = Optional.empty();

    private static final Image emptyImage;

    static {
        emptyImage = new Image(CharacterCardView.class.getResourceAsStream("/assets/character_cards/empty.png"));
    }

    public CharacterCardView() {
        super();

        setImage(getCurrentImage());
        setPreserveRatio(true);
        setId("card");
    }

    public CharacterCardView(ReducedCharacterCard card) {
        this();
        this.card = Optional.of(card);
    }

    private Image getCurrentImage() {
        return emptyImage;

        /*if(card.isPresent()){
            var path = "/assets/assistant_cards/" + card.getClass().getSimpleName() + ".png";
            return getClass().getResourceAsStream(path);
        }else{
            return emptyImage;
        }*/
    }
}
