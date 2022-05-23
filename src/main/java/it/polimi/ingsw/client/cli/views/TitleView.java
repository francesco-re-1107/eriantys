package it.polimi.ingsw.client.cli.views;

import it.polimi.ingsw.client.cli.Cursor;
import it.polimi.ingsw.client.cli.Palette;

import static org.fusesource.jansi.Ansi.ansi;

public class TitleView extends BaseView {

    private final Title title;

    private final String description;

    public TitleView(Title title) {
        this(title, "");
    }

    public TitleView(Title title, String description) {
        this.title = title;
        this.description = description;
    }

    @Override
    public void draw() {
        int color = switch (title) {
            case ERIANTYS -> Palette.ERIANTYS;
            case WIN -> Palette.WIN;
            case LOSE -> Palette.LOSE;
            case TIE -> Palette.TIE;
            case PAUSED -> Palette.PAUSED;
            case TERMINATED -> Palette.TERMINATED;
        };

        // read title from file
        var is = getClass().getResourceAsStream("/titles/" + title.name().toLowerCase() + ".txt");
        var titleString = title.name();
        try {
            titleString = new String(is.readAllBytes());
        } catch (Exception e) {}

        cursor.print(ansi().fg(color).a(titleString), 0, 0);
        cursor.printCentered(description, (int)titleString.lines().count() + 1);
        cursor.moveToXY(1, Cursor.HEIGHT);
    }

    public enum Title{
        ERIANTYS,
        WIN,
        LOSE,
        TIE,
        PAUSED,
        TERMINATED
    }

}
