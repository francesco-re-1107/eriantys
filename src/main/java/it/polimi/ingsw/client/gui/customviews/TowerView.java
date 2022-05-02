package it.polimi.ingsw.client.gui.customviews;

import it.polimi.ingsw.server.model.Tower;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.InputStream;

public class TowerView extends ImageView {

    private Tower towerColor;

    public TowerView() {
        this(Tower.WHITE);
    }

    public TowerView(Tower towerColor) {
        this.towerColor = towerColor;
        setImage(new Image(getImageStream()));
    }

    public Tower getTowerColor() {
        return towerColor;
    }

    public void setTowerColor(Tower towerColor) {
        this.towerColor = towerColor;
        setImage(new Image(getImageStream()));
    }

    private InputStream getImageStream() {
        var path = "/assets/towers/" + towerColor.name().toLowerCase() + ".png";
        return getClass().getResourceAsStream(path);
    }
}
