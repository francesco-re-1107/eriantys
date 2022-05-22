package it.polimi.ingsw.client.cli.views;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.cli.DrawingCharacters;
import it.polimi.ingsw.client.cli.Palette;
import it.polimi.ingsw.common.reducedmodel.ReducedPlayer;
import it.polimi.ingsw.common.reducedmodel.ReducedRound;
import it.polimi.ingsw.server.model.Student;

import java.util.Map;

import static org.fusesource.jansi.Ansi.ansi;

public class PlayerBoardView extends BaseView {

    private final ReducedPlayer player;

    private final Map<Student, String> professors;

    private final boolean isCurrentPlayer;

    private final boolean myPlayer;

    private final boolean expertMode;
    private final ReducedRound currentRound;

    public PlayerBoardView(ReducedPlayer player,
                           ReducedRound currentRound,
                           Map<Student, String> professors,
                           boolean expertMode) {
        this.player = player;
        this.professors = professors;
        this.currentRound = currentRound;
        this.myPlayer = player.nickname().equals(Client.getInstance().getNickname());
        this.expertMode = expertMode;
        this.isCurrentPlayer = player.nickname().equals(currentRound.currentPlayer());
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

    private void drawTowers() {
        cursor.saveCursorPosition();

        cursor.moveRelative(14, 3);

        cursor.print(ansi()
                .bg(Palette.DASHBOARD_BACKGROUND)
                .fg(Palette.TOWERS_COLOR_MAP.get(player.towerColor()))
                .a(DrawingCharacters.TOWER)
                .fg(Palette.WHITE)
                .bold()
                .a(" " + player.towersCount())
                .reset()
        );

        cursor.restoreCursorPosition();
    }

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
                .a(myPlayer ? "Tu" : player.nickname())
                .reset());

        cursor.restoreCursorPosition();
    }

}
