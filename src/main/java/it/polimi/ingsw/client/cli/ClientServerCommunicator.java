package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.Utils;
import it.polimi.ingsw.common.requests.Request;
import it.polimi.ingsw.common.responses.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * This class handles the low level communication from client to server
 */
public class ClientServerCommunicator {
    /**
     * Server socket
     */
    private final Socket socket;

    /**
     * Stores the listener for every request received
     */
    private final ClientServerCommunicator.CommunicatorListener communicatorListener;

    /**
     * Instantiates a communicator
     * @param socket the server socket
     * @param listener response listener
     */
    public ClientServerCommunicator(Socket socket, CommunicatorListener listener) {
        this.socket = socket;
        this.communicatorListener = listener;
    }

    /**
     * This method binds to the socket input stream and listens for response from the server
     */
    public void startListening() {
        try {
            var in = new ObjectInputStream(socket.getInputStream());

            while (socket.isConnected()){
                var o = in.readObject();
                var r = (Response) o;
                communicatorListener.onResponse(r);
            }

            //close connections
            in.close();
            socket.close();
            communicatorListener.onDisconnect();

        } catch (Exception e){
            Utils.LOGGER.severe(e.getMessage());
        }
    }

    /**
     * This method sends a request to the server
     * @param r the request to send
     */
    public void send(Request r){
        try {
            var out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(r);
        }catch (IOException e){
            Utils.LOGGER.severe(e.getMessage());
        }
    }

    /**
     * Listener interface
     */
    public interface CommunicatorListener {
        void onResponse(Response r);
        void onDisconnect();
    }

}
