package it.polimi.ingsw.client.gui.customviews;

import it.polimi.ingsw.common.reducedmodel.ReducedPlayer;
import it.polimi.ingsw.server.model.AssistantCard;
import it.polimi.ingsw.server.model.Student;
import javafx.beans.NamedArg;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

/**
 * This class shows a player's board.
 * if myBoard is flagged true, it only shows the nickname and the card played by this client.
 */
public class PlayerBoardView extends VBox {

    public final boolean myBoard;
    private ReducedPlayer player;
    private Label nicknameLabel;
    private AssistantCardView assistantCardView;
    private HBox coinBox;
    private Label coinLabel;
    private TowerView towerView;
    private Label towerLabel;
    private final Map<Student, StudentView> studentViews = new EnumMap<>(Student.class);
    private final Map<Student, Label> studentLabels = new EnumMap<>(Student.class);

    public PlayerBoardView() {
        this(false);
    }

    public PlayerBoardView(@NamedArg("myBoard") boolean myBoard) {
        super();
        this.myBoard = myBoard;

        setupElements();
        setAlignment(Pos.TOP_CENTER);
        setSpacing(15);
        getStylesheets().add(getClass().getResource("/css/player_board_view.css").toExternalForm());
    }

    private void setupElements() {
        nicknameLabel = new Label();
        nicknameLabel.setId("nickname_label");

        assistantCardView = new AssistantCardView();

        if(myBoard) {
            nicknameLabel.setText("Tu");
            getChildren().addAll(nicknameLabel, assistantCardView);
        } else {
            var hbox = new HBox();
            hbox.setAlignment(Pos.TOP_CENTER);
            hbox.setSpacing(15);

            var vbox = new VBox();
            vbox.setAlignment(Pos.TOP_LEFT);
            vbox.setSpacing(5);

            //students
            for (Student s : Student.values()) {
                var hb = new HBox();
                hb.setSpacing(3);
                hb.setAlignment(Pos.CENTER_LEFT);

                var sv = new StudentView(s);
                sv.setFitWidth(27);
                var svLabel = new Label("0(0)");
                svLabel.setId("board_labels");

                studentViews.put(s, sv);
                studentLabels.put(s, svLabel);

                hb.getChildren().addAll(sv, svLabel);
                vbox.getChildren().add(hb);
            }

            //tower
            towerView = new TowerView();
            towerView.setFitWidth(27);
            towerLabel = new Label();
            towerLabel.setId("board_labels");
            var towerBox = new HBox();
            towerBox.setAlignment(Pos.CENTER_LEFT);
            towerBox.setSpacing(3);
            towerBox.getChildren().addAll(towerView, towerLabel);

            //coin
            var coin = new ImageView(new Image(getClass().getResource("/assets/coin.png").toExternalForm()));
            coin.setFitWidth(27);
            coin.setPreserveRatio(true);
            coinLabel = new Label();
            coinLabel.setId("board_labels");
            coinBox = new HBox();
            coinBox.setAlignment(Pos.CENTER_LEFT);
            coinBox.setSpacing(3);
            coinBox.getChildren().addAll(coin, coinLabel);

            vbox.getChildren().addAll(coinBox, towerBox);
            hbox.getChildren().addAll(assistantCardView, vbox);
            getChildren().addAll(nicknameLabel, hbox);
        }
    }

    /**
     * Sets the player to be displayed.
     * @param player
     */
    public void setPlayer(ReducedPlayer player) {
        if(myBoard) return;

        this.player = player;

        nicknameLabel.setText(player.nickname());

        for (Student s : Student.values()) {
            var text = player.school().getCountForStudent(s) + "(" + player.entrance().getCountForStudent(s) + ")";
            studentLabels.get(s).setText(text);
        }

        towerView.setTowerColor(player.towerColor());
        towerLabel.setText(String.valueOf(player.towersCount()));

        coinLabel.setText(String.valueOf(player.coins()));
    }

    /**
     * Sets current professors
     * @param professors
     */
    public void setProfessors(Map<Student, String> professors) {
        if(myBoard) return;

        for(Student s : Student.values()) {
            var hasProfessor = Objects.equals(player.nickname(), professors.get(s));
            studentViews.get(s).setProfessor(hasProfessor);
        }
    }

    /**
     * Set card played by the player
     * @param card
     */
    public void setPlayedCard(AssistantCard card) {
        assistantCardView.setCard(card);
    }

    /**
     * Set whether the game is expert mode or not
     * @param expertMode
     */
    public void setVisibilityForExpertMode(boolean expertMode) {
        if(myBoard) return;

        coinBox.setVisible(expertMode);
        coinBox.setManaged(expertMode);
    }
}
