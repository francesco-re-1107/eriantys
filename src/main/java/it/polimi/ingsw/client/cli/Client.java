package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.Utils;
import it.polimi.ingsw.client.cli.ClientServerCommunicator.CommunicatorListener;
import it.polimi.ingsw.common.requests.RegisterNicknameRequest;
import it.polimi.ingsw.common.responses.Response;

import java.io.IOException;
import java.net.Socket;

public class Client implements CommunicatorListener {

    private ClientState currentState;
    private ClientServerCommunicator communicator;

    /**
     * Instantiates a new Client.
     *
     */
    public Client(String serverAddress, int port) throws IOException {
        communicator = new ClientServerCommunicator(new Socket(serverAddress, port), this);

        var t = new Thread(() -> communicator.startListening());
        t.start();

        currentState = ClientState.WELCOME;
    }

    @Override
    public void onResponse(Response r) {
        Utils.LOGGER.info("Received response: " + r.getClass().getSimpleName());
    }

    @Override
    public void onDisconnect() {

    }

    public void registerNickname(String nickname) {
        communicator.send(new RegisterNicknameRequest(nickname));
    }
}
