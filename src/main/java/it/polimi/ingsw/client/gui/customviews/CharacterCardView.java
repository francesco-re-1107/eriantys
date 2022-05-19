package it.polimi.ingsw.client.gui.customviews;

import it.polimi.ingsw.client.gui.CharacterCardInformationPopup;
import it.polimi.ingsw.server.model.Character;
import javafx.event.EventHandler;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.util.EnumMap;
import java.util.Map;

/**
 * This class shows an assistant card if present or the empty card.
 */
public class CharacterCardView extends ImageView implements EventHandler<MouseEvent> {
    private Character character;

    private int usedTimes;

    private CharacterCardInformationPopup infoPopup;

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
        setOnMouseEntered(this);
        setOnMouseExited(this);

        updateView();
    }

    /**
     * Set character to show
     * @param character
     */
    public void setCharacter(Character character, int usedTimes) {
        this.character = character;
        this.usedTimes = usedTimes;
        updateView();
    }

    private void updateView() {
        setImage(character == null ? emptyImage : characterImages.get(character));
        if(infoPopup != null)
            infoPopup.hide();
        infoPopup = null;
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
            setOpacity(0.6);
        } else {
            setEffect(original);
            setOpacity(1);
        }
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        if(character == null) return;

        if (mouseEvent.getEventType() == MouseEvent.MOUSE_ENTERED) {
            infoPopup = new CharacterCardInformationPopup(character, usedTimes);
            infoPopup.show(this, mouseEvent.getScreenX() + 10, mouseEvent.getScreenY() + 10);
        } else if (mouseEvent.getEventType() == MouseEvent.MOUSE_EXITED) {
            if(infoPopup != null)
                infoPopup.hide();
            infoPopup = null;
        }
    }
}
