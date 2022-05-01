package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.cli.ClientServerCommunicator.CommunicatorListener;
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
    public Client(String server_url, int port) throws IOException {
        communicator = new ClientServerCommunicator(
                new Socket(server_url, port),
                this);
        currentState = ClientState.WELCOME;

    }

    @Override
    public void onResponse(Response r) {

    }

    @Override
    public void onDisconnect() {

    }
}
