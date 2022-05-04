package it.polimi.ingsw.client.gui.customviews;

import it.polimi.ingsw.common.reducedmodel.ReducedCharacterCard;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.InputStream;
import java.util.Optional;

public class CharacterCardView extends ImageView {
    private Optional<ReducedCharacterCard> card = Optional.empty();

    public CharacterCardView() {
        super();

        setImage(new Image(getImageStream()));
        setPreserveRatio(true);
        setId("card");
    }

    public CharacterCardView(ReducedCharacterCard card) {
        this();
        this.card = Optional.of(card);
    }

    private InputStream getImageStream() {

        //if(card.isPresent()){
        if(false){
            var path = "/assets/assistant_cards/" + card.getClass().getSimpleName() + ".png";
            return getClass().getResourceAsStream(path);
        }else{
            return getClass().getResourceAsStream("/assets/character_cards/empty.png");
        }
    }
}
