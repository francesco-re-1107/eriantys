package it.polimi.ingsw.client;

public enum InfoString {

    EMPTY(""),
    MY_TURN_PLAY_ASSISTANT_CARD("È il tuo turno: gioca la tua carta assistente"),
    MY_TURN_PLACE_STUDENTS("È il tuo turno: posiziona %s studenti sulle isole oppure nella tua sala"),
    MY_TURN_PLAY_CHARACTER_CARD("È il tuo turno: gioca una carta personaggio oppure muovi madre natura al più di %s passi"),
    MY_TURN_MOVE_MOTHER_NATURE("È il tuo turno: muovi madre natura al più di %s passi"),
    MY_TURN_SELECT_CLOUD("È il tuo turno: seleziona una nuvola"),
    OTHER_PLAYER_WAIT_FOR_HIS_TURN("È il turno di %s: attendi");


    private final String text;

    InfoString(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

}