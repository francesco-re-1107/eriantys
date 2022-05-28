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
    private static final int X_POSITION = 36;

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

        for(int i = 0; i < clouds.size(); i++) {
            // remap each cloud to the right position
            var point = CloudsLayouts.getPointForCloud(clouds.size(), i);

            cursor.moveToXY(X_POSITION + point.x(), Y_POSITION + point.y());

            new CloudView(clouds.get(i), i).draw();
        }

        cursor.restoreCursorPosition();
    }
}
