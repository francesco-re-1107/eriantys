package it.polimi.ingsw.client.cli.views;

import it.polimi.ingsw.Utils;
import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.cli.DrawingCharacters;
import it.polimi.ingsw.client.cli.Palette;
import it.polimi.ingsw.common.reducedmodel.ReducedPlayer;
import it.polimi.ingsw.common.reducedmodel.ReducedRound;
import it.polimi.ingsw.server.model.Student;
import it.polimi.ingsw.server.model.Tower;

import java.util.Map;

import static org.fusesource.jansi.Ansi.ansi;

/**
 * This view shows a generic player board
 */
public class PlayerBoardView extends BaseView {

    /**
     * The player to show
     */
    private final ReducedPlayer player;

    /**
     * Current professors
     */
    private final Map<Student, String> professors;

    /**
     * If the given player is the current player
     */
    private final boolean isCurrentPlayer;

    /**
     * If the given player is my player
     */
    private final boolean isMyPlayer;

    /**
     * If game is expert mode then show coins, otherwise hide them
     */
    private final boolean expertMode;

    /**
     * Current round, used to show the played assistant cards
     */
    private final ReducedRound currentRound;

    /**
     * Create a board view for the given player
     * @param player the player to show
     * @param currentRound the current round
     * @param professors the current professors
     * @param expertMode if the game is expert mode or not
     */
    public PlayerBoardView(ReducedPlayer player,
                           ReducedRound currentRound,
                           Map<Student, String> professors,
                           boolean expertMode) {
        this.player = player;
        this.professors = professors;
        this.currentRound = currentRound;
        this.isMyPlayer = Client.getInstance().getNickname().equals(player.nickname());
        this.expertMode = expertMode;
        this.isCurrentPlayer = currentRound.currentPlayer().equals(player.nickname());
    }

    @Override
    public void draw() {
        drawNickname();
        drawStudents();
        drawCard();
        drawTowers();
        if(expertMode)
            drawCoins();
    }

    /**
     * Draw coins view
     */
    private void drawCoins() {
        cursor.saveCursorPosition();

        cursor.moveRelative(14, 5);

        cursor.print(ansi()
                .bg(Palette.DASHBOARD_BACKGROUND)
                .a(DrawingCharacters.COIN)
                .fg(Palette.WHITE)
                .bold()
                .a(" " + player.coins())
                .reset()
        );

        cursor.restoreCursorPosition();
    }

    /**
     * Draw towers view
     */
    private void drawTowers() {
        cursor.saveCursorPosition();

        cursor.moveRelative(14, 3);

        cursor.print(ansi()
                .bg(Utils.isWindows() && player.towerColor() == Tower.BLACK ? Palette.BLACK_TOWER_CONTRAST_BACKGROUND_WIN : Palette.DASHBOARD_BACKGROUND)
                .fg(Palette.TOWERS_COLOR_MAP.get(player.towerColor()))
                .a(DrawingCharacters.TOWER)
                .bg(Palette.DASHBOARD_BACKGROUND)
                .fg(Palette.WHITE)
                .bold()
                .a(" " + player.towersCount())
                .reset()
        );

        cursor.restoreCursorPosition();
    }

    /**
     * Draw played card
     */
    private void drawCard() {
        cursor.saveCursorPosition();

        cursor.moveRelative(13, 1);

        //card
        var cardPlayed = currentRound
                .playedAssistantCards()
                .get(player.nickname());
        var cardString = cardPlayed == null ? "[N/A]" :
                "[" + cardPlayed.turnPriority() + "|" + cardPlayed.motherNatureMaxMoves() + "]";

        cursor.print(ansi()
                .fg(Palette.WHITE)
                .bg(Palette.DASHBOARD_BACKGROUND)
                .bold()
                .a(cardString)
                .reset()
        );

        cursor.restoreCursorPosition();
    }

    /**
     * Draw students owned by the player
     */
    private void drawStudents() {
        cursor.saveCursorPosition();

        cursor.moveRelative(4, 1);

        for (var student : Student.values()) {
            var hasProfessor = player.nickname().equals(professors.get(student));

            //student
            cursor.print(ansi()
                    .bg(Palette.DASHBOARD_BACKGROUND)
                    .fg(Palette.STUDENTS_COLOR_MAP.get(student))
                    .a(hasProfessor ? DrawingCharacters.PROFESSOR : DrawingCharacters.STUDENT)
                    .reset());

            cursor.moveRelative(1, 0);

            //students count
            var studentsString = player.school().getCountForStudent(student) +
                    "(" + player.entrance().getCountForStudent(student) + ")";
            cursor.print(ansi()
                    .bg(Palette.DASHBOARD_BACKGROUND)
                    .fg(Palette.WHITE)
                    .bold()
                    .a(studentsString)
                    .reset()
            );
            cursor.moveRelative(-(2 + studentsString.length()), 1);
        }

        cursor.restoreCursorPosition();
    }

    /**
     * Draw the nickname of the player.
     * If the player is the current player, an indicator is shown.
     * If the given player is my player, it's shown "Tu" instead of the nickname.
     */
    private void drawNickname() {
        cursor.saveCursorPosition();

        cursor.moveRelative(1, 0);
        cursor.print(ansi()
                .fg(Palette.WHITE)
                .bg(Palette.DASHBOARD_BACKGROUND)
                .a(isCurrentPlayer ? DrawingCharacters.CURRENT_PLAYER : DrawingCharacters.EMPTY)
                .reset()
        );

        cursor.moveRelative(1, 0);
        cursor.print(ansi()
                .fg(Palette.WHITE)
                .bg(Palette.DASHBOARD_BACKGROUND)
                .a(isMyPlayer ? "Tu" : player.nickname())
                .reset());

        cursor.restoreCursorPosition();
    }

}
