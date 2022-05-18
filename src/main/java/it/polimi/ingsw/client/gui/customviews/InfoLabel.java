package it.polimi.ingsw.client.gui.customviews;

import it.polimi.ingsw.client.InfoString;
import javafx.geometry.Insets;
import javafx.scene.control.Label;

/**
 * This label is used to show the current information of the game
 */
public class InfoLabel extends Label {

    public InfoLabel() {
        super();

        setId("info_label");
        setInfoString(InfoString.EMPTY);
        setWrapText(true);
        setPadding(new Insets(20, 20, 20, 20));
    }

    public void setInfoString(InfoString info, Object... optionalArgs) {
        if (info == InfoString.EMPTY) {
            setVisible(false);
            setManaged(false);
        } else {
            setVisible(true);
            setManaged(true);
        }
        setText(String.format(info.toString(), optionalArgs));
    }
}
