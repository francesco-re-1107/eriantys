package it.polimi.ingsw.client.gui.customviews;

import it.polimi.ingsw.server.model.Tower;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.EnumMap;
import java.util.Map;

/**
 * This class shows a tower with the given color
 */
public class TowerView extends ImageView {

    private Tower towerColor;

    private static final Map<Tower, Image> towersImages = new EnumMap<>(Tower.class);

    //load images statically
    static {
        for(Tower tower : Tower.values())
            towersImages.put(
                    tower,
                    new Image(TowerView.class.getResourceAsStream("/assets/towers/" + tower.name().toLowerCase() + ".png"))
            );
    }

    /**
     * Create a TowerView as example
     */
    public TowerView() {
        this(Tower.WHITE);
    }

    /**
     * Set the tower color to the given one
     * @param towerColor
     */
    public TowerView(Tower towerColor) {
        this.towerColor = towerColor;
        setImage(towersImages.get(towerColor));
        setPreserveRatio(true);
    }

    /**
     * Get the tower color currently shown
     * @return the tower color
     */
    public Tower getTowerColor() {
        return towerColor;
    }

    /**
     * Set the tower color to the given one
     * @param towerColor
     */
    public void setTowerColor(Tower towerColor) {
        this.towerColor = towerColor;
        setImage(towersImages.get(towerColor));
    }

}
