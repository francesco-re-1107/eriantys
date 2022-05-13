package it.polimi.ingsw.client.gui.customviews;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.HashMap;
import java.util.Map;

public class MotherNatureView extends ImageView {

    private State state;

    private static final Map<State, Image> motherNatureImages = new HashMap<>();

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

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
        updateState();
    }

    private void updateState() {
        if (state == State.INVISIBLE){
            setVisible(false);
        }else {
            setVisible(true);
            setImage(getCurrentImage());
        }
    }

    private Image getCurrentImage() {
        return motherNatureImages.get(state);
    }

    public enum State {
        INVISIBLE,
        ENABLED,
        DISABLED
    }
}
