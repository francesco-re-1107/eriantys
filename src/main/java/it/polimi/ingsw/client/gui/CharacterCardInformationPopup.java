package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.Constants;
import it.polimi.ingsw.server.model.Character;
import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CharacterCardInformationPopup extends ContextMenu {

    public CharacterCardInformationPopup(Character character, int usedTimes) {
        super();

        var titleLabel = new Label(Constants.CHARACTER_NAMES.get(character).toUpperCase());
        titleLabel.setStyle("-fx-font-size: 24px;");

        var descriptionLabel = new Label(Constants.CHARACTER_DESCRIPTIONS.get(character));
        descriptionLabel.setWrapText(true);
        descriptionLabel.setMaxWidth(450);
        descriptionLabel.setStyle("-fx-font-size: 18px;");

        var coinImage = new ImageView(getClass().getResource("/assets/coin.png").toExternalForm());
        coinImage.setFitHeight(25);
        coinImage.setFitWidth(25);

        var costLabel = new Label(String.valueOf(character.getCost(usedTimes)));
        costLabel.setStyle("-fx-font-size: 22px;");

        var costBox = new HBox(coinImage, costLabel);
        costBox.setSpacing(5);
        costBox.setAlignment(Pos.CENTER_RIGHT);

        var vbox = new VBox();
        vbox.setId("character-card-information-popup");
        vbox.setSpacing(5);
        vbox.getChildren().addAll(titleLabel, descriptionLabel, costBox);
        getItems().add(new CustomMenuItem(vbox));
    }
}
