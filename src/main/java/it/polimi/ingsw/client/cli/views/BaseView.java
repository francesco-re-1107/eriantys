package it.polimi.ingsw.client.cli.views;

import it.polimi.ingsw.client.cli.Cursor;

public abstract class BaseView {
    protected final Cursor cursor = Cursor.getInstance();
    public abstract void draw() ;
}
