package it.polimi.ingsw.client.cli.views;

import it.polimi.ingsw.client.cli.CloudsLayouts;
import it.polimi.ingsw.server.model.StudentsContainer;

import java.util.List;

/**
 * This view is used to display all the clouds of the round
 */
public class CloudsLayoutView extends BaseView{

    /**
     * The list of clouds of the round
     */
    private final List<StudentsContainer> clouds;

    /**
     * Absolute x position of the clouds view
     */
    private static final int X_POSITION = 37;

    /**
     * Absolute y position of the clouds view
     */
    private static final int Y_POSITION = 8;

    /**
     * Create a CloudsLayoutView with the given clouds
     * @param clouds the list of clouds to display
     */
    public CloudsLayoutView(List<StudentsContainer> clouds) {
        this.clouds = clouds;
    }

    @Override
    public void draw() {
        cursor.saveCursorPosition();

        for(var c : clouds) {
            var i = clouds.indexOf(c);

            // remap each cloud to the right position
            var x = CloudsLayouts.getPointForCloud(clouds.size(), i).x();
            var y = CloudsLayouts.getPointForCloud(clouds.size(), i).y();
            cursor.moveToXY(X_POSITION + x, Y_POSITION + y);

            new CloudView(c, i).draw();
        }

        cursor.restoreCursorPosition();
    }
}
