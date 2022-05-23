package it.polimi.ingsw.client.cli.views;

import it.polimi.ingsw.Constants;
import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.cli.Palette;
import it.polimi.ingsw.common.reducedmodel.ReducedCharacterCard;
import it.polimi.ingsw.common.reducedmodel.charactercards.ReducedCentaurCharacterCard;
import it.polimi.ingsw.common.reducedmodel.charactercards.ReducedFarmerCharacterCard;
import it.polimi.ingsw.common.reducedmodel.charactercards.ReducedKnightCharacterCard;
import it.polimi.ingsw.common.reducedmodel.charactercards.ReducedPostmanCharacterCard;
import it.polimi.ingsw.common.requests.PlayCharacterCardRequest;
import it.polimi.ingsw.server.model.Character;
import org.fusesource.jansi.Ansi;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.fusesource.jansi.Ansi.ansi;

public class CharacterCardsView extends BaseView{

    private final ListView<Map.Entry<Character, Integer>> listView;

    public CharacterCardsView(List<Map.Entry<Character, Integer>> playableCards) {
        this.listView = new ListView<>(playableCards, getRenderer(), "Carte personaggio acquistabili", "Seleziona carta", "Nessuna carta giocabile");
    }

    @Override
    public void draw() {
        listView.setListener((e, index) -> processCharacterCardSelected(e.getKey()));
        listView.draw();
    }

    private void processCharacterCardSelected(Character character) {
        //TODO: implement all the possible character cards
        ReducedCharacterCard card = new ReducedCentaurCharacterCard();

        switch (character) {
            case CENTAUR -> {
                card = new ReducedCentaurCharacterCard();
            }
            case FARMER -> {
                card = new ReducedFarmerCharacterCard();
            }
            case GRANDMA -> {

            }
            case HERALD -> {

            }
            case KNIGHT -> {
                card = new ReducedKnightCharacterCard();
            }
            case MINSTREL -> {

            }
            case MUSHROOM_MAN -> {

            }
            case POSTMAN -> {
                card = new ReducedPostmanCharacterCard();
            }
        }

        var request = new PlayCharacterCardRequest(card);
        Client.getInstance().forwardGameRequest(request);
    }

    private Function<Map.Entry<Character, Integer>, Ansi> getRenderer() {
        return entry -> ansi()
                .bold()
                .fgBrightMagenta()
                .a(Constants.CHARACTER_NAMES.get(entry.getKey()))
                .reset()
                .fg(Palette.WHITE)
                .a(" - ")
                .a(Constants.CHARACTER_DESCRIPTIONS.get(entry.getKey()))
                .a(" $ " + entry.getKey().getCost(entry.getValue()))
                .reset();
    }
}
