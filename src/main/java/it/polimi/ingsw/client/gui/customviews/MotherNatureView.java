package it.polimi.ingsw.client.gui.customviews;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.EnumMap;
import java.util.Map;

/**
 * This class shows mother nature on every island
 */
public class MotherNatureView extends ImageView {

    private State state;

    private static final Map<State, Image> motherNatureImages = new EnumMap<>(State.class);

    //load images statically
    static {
        motherNatureImages.put(
                State.ENABLED,
                new Image(MotherNatureView.class.getResourceAsStream("/assets/mother_nature/enabled.png"))
        );
        motherNatureImages.put(
                State.DISABLED,
                new Image(MotherNatureView.class.getResourceAsStream("/assets/mother_nature/disabled.png"))
        );
    }

    public MotherNatureView() {
        this(State.INVISIBLE);
    }

    public MotherNatureView(State state) {
        this.state = state;

        setPreserveRatio(true);
        updateState();
    }

    /**
     * @return the current state of mother nature
     */
    public State getState() {
        return state;
    }

    /**
     * Update the state of mother nature
     * @param state
     */
    public void setState(State state) {
        this.state = state;
        updateState();
    }

    private void updateState() {
        if (state == State.INVISIBLE){
            setVisible(false);
        }else {
            setVisible(true);
            setImage(motherNatureImages.get(state));
        }
    }

    /**
     * All possible states of mother nature
     */
    public enum State {
        INVISIBLE,
        ENABLED,
        DISABLED
    }
}
