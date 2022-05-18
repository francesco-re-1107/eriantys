package it.polimi.ingsw.client.gui.customviews;

import it.polimi.ingsw.server.model.Character;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.EnumMap;
import java.util.Map;

/**
 * This class shows an assistant card if present or the empty card.
 */
public class CharacterCardView extends ImageView {
    private Character character;

    private static final Image emptyImage;

    private static final Map<Character, Image> characterImages = new EnumMap<>(Character.class);

    //load images statically
    static {
        emptyImage = new Image(CharacterCardView.class.getResourceAsStream("/assets/character_cards/empty.png"));

        for(var c : Character.values())
            characterImages.put(
                    c,
                    new Image(CharacterCardView.class.getResourceAsStream("/assets/character_cards/" + c.name().toLowerCase() + ".png"))
            );
    }

    public CharacterCardView() {
        this(null);
    }

    public CharacterCardView(Character character) {
        super();
        this.character = character;

        setPreserveRatio(true);
        setId("character_card");

        updateView();
    }

    /**
     * Set character to show
     * @param character
     */
    public void setCharacter(Character character) {
        this.character = character;
        updateView();
    }

    private void updateView() {
        setImage(character == null ? emptyImage : characterImages.get(character));
    }

    /**
     * Set the card as grayed out
     * @param grayedOut
     */
    public void setGrayedOut(boolean grayedOut) {
        var desaturate = new ColorAdjust();
        desaturate.setSaturation(-1);

        var original = new ColorAdjust();
        original.setSaturation(0);

        if (grayedOut) {
            setEffect(desaturate);
        } else {
            setEffect(original);
        }
    }
}
