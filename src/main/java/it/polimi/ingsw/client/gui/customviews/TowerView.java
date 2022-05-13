package it.polimi.ingsw.client.gui.customviews;

import it.polimi.ingsw.server.model.Tower;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.HashMap;
import java.util.Map;

public class TowerView extends ImageView {

    private Tower towerColor;

    public TowerView() {
        this(Tower.WHITE);
    }

    private static final Map<Tower, Image> towersImages = new HashMap<>();

    static {
        for(Tower tower : Tower.values())
            towersImages.put(
                    tower,
                    new Image(TowerView.class.getResourceAsStream("/assets/towers/" + tower.name().toLowerCase() + ".png"))
            );

    }

    public TowerView(Tower towerColor) {
        this.towerColor = towerColor;
        setImage(getCurrentImage());
        setPreserveRatio(true);
    }

    public Tower getTowerColor() {
        return towerColor;
    }

    public void setTowerColor(Tower towerColor) {
        this.towerColor = towerColor;
        setImage(getCurrentImage());
    }

    private Image getCurrentImage() {
        return towersImages.get(towerColor);
    }
}
