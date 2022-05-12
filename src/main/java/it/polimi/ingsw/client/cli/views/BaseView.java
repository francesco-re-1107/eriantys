package it.polimi.ingsw.client.cli.views;

import it.polimi.ingsw.client.cli.Cursor;

public abstract class BaseView {
    protected Cursor cursor = Cursor.getInstance();
    protected abstract void init() ;
}
