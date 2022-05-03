package it.polimi.ingsw.client.gui.customviews;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.InputStream;

public class MotherNatureView extends ImageView {

    private State state;

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
            setImage(new Image(getImageStream()));
        }
    }

    private InputStream getImageStream() {
        if(state == State.ENABLED){
            return getClass().getResourceAsStream("/assets/mother_nature/enabled.png");
        }else{
            return getClass().getResourceAsStream("/assets/mother_nature/disabled.png");
        }
    }

    public enum State {
        INVISIBLE,
        ENABLED,
        DISABLED
    }
}
