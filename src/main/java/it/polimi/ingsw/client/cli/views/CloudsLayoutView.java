package it.polimi.ingsw.client.cli.views;

import it.polimi.ingsw.client.cli.CloudsLayouts;
import it.polimi.ingsw.server.model.StudentsContainer;

import java.util.List;

public class CloudsLayoutView extends BaseView{

    private final List<StudentsContainer> clouds;

    private static final int X_OFFSET = 37;

    private static final int Y_OFFSET = 8;

    public CloudsLayoutView(List<StudentsContainer> clouds) {
        this.clouds = clouds;
    }

    @Override
    public void draw() {
        cursor.saveCursorPosition();

        for(var c : clouds) {
            var i = clouds.indexOf(c);
            var x = CloudsLayouts.getPointForCloud(clouds.size(), i).x();
            var y = CloudsLayouts.getPointForCloud(clouds.size(), i).y();
            cursor.moveToXY(X_OFFSET + x, Y_OFFSET + y);
            new CloudView(c, i).draw();
        }
        cursor.restoreCursorPosition();
    }
}
