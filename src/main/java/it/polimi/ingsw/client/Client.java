package it.polimi.ingsw.client;

import it.polimi.ingsw.Utils;
import it.polimi.ingsw.client.ClientServerCommunicator.CommunicatorListener;
import it.polimi.ingsw.client.cli.ClientState;
import it.polimi.ingsw.common.requests.RegisterNicknameRequest;
import it.polimi.ingsw.common.responses.Update;

import java.io.IOException;
import java.net.Socket;

public class Client implements CommunicatorListener {

    private ClientState currentState;
    private final ClientServerCommunicator communicator;

    /**
     * Instantiates a new Client.
     */
    public Client(String serverAddress, int port) throws IOException {
        communicator = new ClientServerCommunicator(new Socket(serverAddress, port), this);

        var t = new Thread(() -> communicator.startListening());
        t.start();

        currentState = ClientState.WELCOME;
    }

    @Override
    public void onUpdate(Update r) {
        Utils.LOGGER.info("Received update: " + r.getClass().getSimpleName());
    }

    @Override
    public void onDisconnect() {

    }

    public void registerNickname(String nickname, ClientServerCommunicator.SuccessListener successListener,
                                 ClientServerCommunicator.ErrorListener errorListener) {
        communicator.send(new RegisterNicknameRequest(nickname), successListener, errorListener);
    }
}
