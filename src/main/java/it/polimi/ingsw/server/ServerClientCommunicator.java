package it.polimi.ingsw.server;

import it.polimi.ingsw.Utils;
import it.polimi.ingsw.common.requests.Request;
import it.polimi.ingsw.common.responses.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * This class handles the low level communication from server to client
 */
public class ServerClientCommunicator {

    /**
     * Client socket
     */
    private final Socket socket;

    /**
     * Stores the listener for every request received
     */
    private final CommunicatorListener communicatorListener;

    /**
     * Instantiates a communicator
     * @param socket the client socket
     * @param listener request listener
     */
    public ServerClientCommunicator(Socket socket, CommunicatorListener listener) {
        this.socket = socket;
        this.communicatorListener = listener;
    }

    /**
     * This method binds to the socket input stream and listens for requests from the client
     */
    public void startListening() {
        try {
            var in = new ObjectInputStream(socket.getInputStream());

            while (socket.isConnected()){
                var o = in.readObject();
                var r = (Request) o;
                communicatorListener.onRequest(r);

                Utils.LOGGER.info("Request received: " + r.getClass().getSimpleName());
            }

            //close connections
            in.close();
            socket.close();
            communicatorListener.onDisconnect();
        } catch (Exception e){
            Utils.LOGGER.info("Client disconnected");
            communicatorListener.onDisconnect();
        }
    }

    /**
     * This method sends a response to the client
     * @param r the response to send
     */
    public void send(Response r){
        try {
            var out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(r);
        }catch (IOException e){
            //client disconnected
            Utils.LOGGER.info("Client disconnected " + e.getMessage());
            communicatorListener.onDisconnect();
        }
    }

    /**
     * Listener interface
     */
    public interface CommunicatorListener {
        void onRequest(Request r);
        void onDisconnect();
    }
}
