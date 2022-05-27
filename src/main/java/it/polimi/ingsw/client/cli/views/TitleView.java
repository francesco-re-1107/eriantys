package it.polimi.ingsw.client.cli.views;

import it.polimi.ingsw.Utils;
import it.polimi.ingsw.client.cli.Cursor;
import it.polimi.ingsw.client.cli.Palette;

import java.nio.charset.StandardCharsets;

import static org.fusesource.jansi.Ansi.ansi;

/**
 * This view is used to show a generic title with an optional subtitle.
 */
public class TitleView extends BaseView {

    /**
     * The title to show.
     */
    private final Title title;

    /**
     * The subtitle to show.
     */
    private final String subtitle;

    /**
     * Create a simple title with no subtitle.
     * @param title the title to show
     */
    public TitleView(Title title) {
        this(title, "");
    }

    /**
     * Create a title with a subtitle. The subtitle is printed centered below the title.
     * @param title the title to show
     * @param subtitle the subtitle to show
     */
    public TitleView(Title title, String subtitle) {
        this.title = title;
        this.subtitle = subtitle;
    }

    @Override
    public void draw() {
        //select color based on title
        int color = switch (title) {
            case ERIANTYS -> Palette.ERIANTYS;
            case WIN -> Palette.WIN;
            case LOSE -> Palette.LOSE;
            case TIE -> Palette.TIE;
            case PAUSED -> Palette.PAUSED;
            case TERMINATED -> Palette.TERMINATED;
        };

        // read title from file
        var titleString = title.name();

        try (var is = getClass().getResourceAsStream("/titles/" + title.name().toLowerCase() + ".txt");) {
            titleString = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            Utils.LOGGER.warning("Could not read title file: " + title.name().toLowerCase() + ".txt");
        }

        cursor.print(ansi().fg(color).a(titleString), 0, 0);
        cursor.printCentered(subtitle, (int)titleString.lines().count() + 1);
        cursor.moveToXY(1, Cursor.HEIGHT);
    }

    /**
     * All the possible titles
     */
    public enum Title{
        ERIANTYS,
        WIN,
        LOSE,
        TIE,
        PAUSED,
        TERMINATED
    }

}
