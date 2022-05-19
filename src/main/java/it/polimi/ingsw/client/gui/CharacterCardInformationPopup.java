package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.server.model.Character;
import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.EnumMap;
import java.util.Map;

public class CharacterCardInformationPopup extends ContextMenu {

    private static final Map<Character, String> characterNames = new EnumMap<>(Character.class);

    private static final Map<Character, String> characterDescriptions = new EnumMap<>(Character.class);

    static {
        characterNames.put(Character.CENTAUR, "Centauro");
        characterDescriptions.put(Character.CENTAUR, "Le torri non vengono considerate nel calcolo dell'influenza.");

        characterNames.put(Character.FARMER, "Contadino");
        characterDescriptions.put(Character.FARMER, "Prendi il controllo dei professori anche se hai lo stesso numero di studenti di altri giocatori.");

        characterNames.put(Character.KNIGHT, "Cavaliere");
        characterDescriptions.put(Character.KNIGHT, "Ricevi 2 punti addizionali nel calcolo dell'influenza.");

        characterNames.put(Character.GRANDMA, "Nonna");
        characterDescriptions.put(Character.GRANDMA, "Puoi porre un divieto su un'isola a tua scelta.");

        characterNames.put(Character.POSTMAN, "Postino");
        characterDescriptions.put(Character.POSTMAN, "Madre nature potrà spostarsi di 2 punti addizionali.");

        characterNames.put(Character.HERALD, "Araldo");
        characterDescriptions.put(Character.HERALD, "Puoi calcolare l'influenza su un'isola a tua scelta.");

        characterNames.put(Character.MINSTREL, "Menestrello");
        characterDescriptions.put(Character.MINSTREL, "Puoi scambiare 2 studenti della sala con 2 studenti all'entrata");

        characterNames.put(Character.MUSHROOM_MAN, "Fungaro");
        characterDescriptions.put(Character.MUSHROOM_MAN, "Puoi selezionare un colore di uno studente, quest'ultimo non influirà nel calcolo dell'influenza");
    }

    public CharacterCardInformationPopup(Character character, int usedTimes) {
        super();

        var titleLabel = new Label(characterNames.get(character).toUpperCase());
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        var descriptionLabel = new Label(characterDescriptions.get(character));
        descriptionLabel.setWrapText(true);
        descriptionLabel.setMaxWidth(500);

        var coinImage = new ImageView(getClass().getResource("/assets/coin.png").toExternalForm());
        coinImage.setFitHeight(25);
        coinImage.setFitWidth(25);

        var costBox = new HBox(coinImage, new Label(String.valueOf(character.getCost(usedTimes))));
        costBox.setSpacing(5);
        costBox.setAlignment(Pos.CENTER_RIGHT);

        var vbox = new VBox();
        vbox.setId("character-card-information-popup");
        vbox.setSpacing(5);
        vbox.getChildren().addAll(titleLabel, descriptionLabel, costBox);
        getItems().add(new CustomMenuItem(vbox));
    }
}
