package it.polimi.ingsw.client;

/**
 * This class contains all the information strings used by the client.
 */
public enum InfoString {

    EMPTY(""),
    MY_TURN_PLAY_ASSISTANT_CARD("È il tuo turno: gioca la tua carta assistente"),
    MY_TURN_PLACE_STUDENTS("È il tuo turno: posiziona %s studenti sulle isole oppure nella tua sala"),
    MY_TURN_PLAY_CHARACTER_CARD("È il tuo turno: gioca una carta personaggio oppure muovi madre natura al più di %s passi"),
    MY_TURN_MOVE_MOTHER_NATURE("È il tuo turno: muovi madre natura al più di %s passi"),
    MY_TURN_SELECT_CLOUD("È il tuo turno: seleziona una nuvola"),
    OTHER_PLAYER_WAIT_FOR_HIS_TURN("È il turno di %s: attendi"),
    MY_TURN_SELECT_ISLAND_FOR_GRANDMA("NONNA: seleziona l'isola su cui porer il divieto di entrata."),

    MY_TURN_SELECT_ISLAND_FOR_HERALD("ARALDO: seleziona l'isola su cui calcolare l'influenza."),
    MY_TURN_SELECT_STUDENT_TO_SWAP_FROM_SCHOOL_TO_ENTRANCE("MENESTRELLO: rimuovi %s studenti dalla sala."),
    MY_TURN_SELECT_STUDENT_TO_SWAP_FROM_ENTRANCE_TO_SCHOOL("MENESTRELLO: aggiungi %s studenti alla sala.");

    private final String text;

    InfoString(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

}
