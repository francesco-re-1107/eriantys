package it.polimi.ingsw.client.cli.views;

import it.polimi.ingsw.client.InfoString;

/**
 * This shows an info label (InfoString).
 */
public class InfoLabelView extends BaseView{

    /**
     * The info string to show.
     */
    private final String infoString;

    /**
     * Create an InfoLabelView with the given info string and args
     * @param infoString the info string to show
     * @param args the args to apply to info string
     */
    public InfoLabelView(InfoString infoString, Object... args) {
        this.infoString = String.format(infoString.toString(), args);
    }

    @Override
    public void draw() {
        cursor.print(infoString, 1, 22);
    }
}
