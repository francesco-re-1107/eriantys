package it.polimi.ingsw.client.gui.customviews;

import it.polimi.ingsw.common.reducedmodel.ReducedCharacterCard;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CharacterCardView extends ImageView {
    private Optional<ReducedCharacterCard> card = Optional.empty();

    private static final Image emptyImage;

    private static final Map<String, Image> charactersImages = new HashMap<>();

    static {
        emptyImage = new Image(CharacterCardView.class.getResourceAsStream("/assets/character_cards/empty.png"));

    }

    public CharacterCardView() {
        this(null);
    }

    public CharacterCardView(ReducedCharacterCard card) {
        super();
        this.card = Optional.ofNullable(card);

        setImage(getCurrentImage());
        setPreserveRatio(true);
        setId("character_card");
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
