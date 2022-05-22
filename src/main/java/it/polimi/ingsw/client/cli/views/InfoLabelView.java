package it.polimi.ingsw.client.cli.views;

import it.polimi.ingsw.client.InfoString;

public class InfoLabelView extends BaseView{

    private final String infoString;
    public InfoLabelView(InfoString infoString, Object... args) {
        this.infoString = String.format(infoString.toString(), args);
    }

    @Override
    public void draw() {
        cursor.print(infoString, 1, 22);
    }
}
