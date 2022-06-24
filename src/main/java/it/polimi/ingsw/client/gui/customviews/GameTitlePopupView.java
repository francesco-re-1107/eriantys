package it.polimi.ingsw.client.gui.customviews;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * This class is used to display win, lose, pause and other game states
 */
public class GameTitlePopupView extends VBox {

    private Label stateLabel;

    private Label descriptionLabel;

    public GameTitlePopupView() {
        super();

        setId("game_title_popup");
        setSpacing(30);
        setAlignment(Pos.CENTER);

        setupTitle();
        setupDescription();

        getStylesheets().add(getClass().getResource("/css/game_title_popup.css").toExternalForm());
    }

    private void setupTitle() {
        stateLabel = new Label();
        stateLabel.setId("title");
        getChildren().add(stateLabel);
    }

    private void setupDescription() {
        descriptionLabel = new Label();
        descriptionLabel.setId("description");
        getChildren().add(descriptionLabel);
    }

    /**
     * Set state of the title
     * @param state the GameTitlePopupView.State to set
     * @param description the description to set for this title
     */
    public void setState(State state, String description){
        switch (state) {
            case WIN -> {
                stateLabel.setText("Hai vinto!");
                stateLabel.setTextFill(Color.LIMEGREEN);
            }
            case LOSE -> {
                stateLabel.setText("Hai perso!");
                stateLabel.setTextFill(Color.FIREBRICK);
            }
            case TIE -> {
                stateLabel.setText("Pareggio!");
                stateLabel.setTextFill(Color.STEELBLUE);
            }
            case PAUSED -> {
                stateLabel.setText("In pausa!");
                stateLabel.setTextFill(Color.GOLD);
            }
            case TERMINATED -> {
                stateLabel.setText("Partita terminata!");
                stateLabel.setTextFill(Color.STEELBLUE);
            }
        }
        descriptionLabel.setText(description);

    }

    public void show() {
        setVisible(true);
        setManaged(true);
    }

    public void hide() {
        setVisible(false);
        setManaged(false);
    }

    /**
     * Possible states of the title
     */
    public enum State {
        WIN,
        LOSE,
        TIE,
        PAUSED,
        TERMINATED
    }
}
