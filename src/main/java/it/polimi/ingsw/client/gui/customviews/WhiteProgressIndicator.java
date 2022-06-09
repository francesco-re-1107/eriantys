package it.polimi.ingsw.client.gui.customviews;

import javafx.scene.control.ProgressIndicator;
import javafx.scene.effect.ColorAdjust;

public class WhiteProgressIndicator extends ProgressIndicator {

    public WhiteProgressIndicator() {
        this(-1);
    }

    public WhiteProgressIndicator(double progress) {
        super(progress);

        //set indicator to white
        var eff = new ColorAdjust();
        eff.setBrightness(1);
        eff.setSaturation(-1);
        setEffect(eff);
    }
}
