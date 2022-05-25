package it.polimi.ingsw.client.cli.views;

import it.polimi.ingsw.client.cli.Cursor;

/**
 * This is the base class for all the views.
 * It holds a reference to the cursor and exposes the draw() method.
 */
public abstract class BaseView {

    /**
     * Cursor used to draw the view
     */
    protected final Cursor cursor = Cursor.getInstance();

    /**
     * Draws the view on the screen
     */
    public abstract void draw() ;

}
