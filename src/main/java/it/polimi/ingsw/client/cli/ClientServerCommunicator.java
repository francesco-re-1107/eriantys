package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.Constants;
import it.polimi.ingsw.Utils;
import it.polimi.ingsw.common.BetterTimer;
import it.polimi.ingsw.common.requests.Request;
import it.polimi.ingsw.common.responses.Response;
import it.polimi.ingsw.common.responses.UpdateResponse;

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

    private boolean isConnected = true;
    private ResponseListener lastRequestSuccessListener;
    private ErrorListener lastRequestErrorListener;
    private long lastRequestTime;
    private ObjectOutputStream outputStream;

    private BetterTimer disconnectionTimer;

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
        setupDisconnectionTimer();
        try {
            var in = new ObjectInputStream(socket.getInputStream());

            while (socket.isConnected()){
                var r = (Response) in.readObject();
                this.disconnectionTimer.restart();

                //it's an update response
                if(r instanceof UpdateResponse u) {
                    communicatorListener.onUpdate(u);
                }else{ //it's a request response
                    if(System.currentTimeMillis() - lastRequestTime < Constants.RESPONSE_TIMEOUT) {
                        this.lastRequestSuccessListener.onResponse(r);
                    }else{
                        this.lastRequestErrorListener.onError(new Exception("Response timeout"));
                    }
                }
            }

            Utils.LOGGER.info("BOH");
            disconnect();
        } catch (Exception e){
            Utils.LOGGER.info("BOH2");
            e.printStackTrace();
            disconnect();
        }
    }

    private void setupDisconnectionTimer() {
        this.disconnectionTimer = new BetterTimer(() -> {
            Utils.LOGGER.info("Timer expired, disconnecting");
            disconnect();
        }, Constants.PING_INTERVAL * 3);
    }

    private void disconnect() {
        Utils.LOGGER.info("Server disconnected");
        isConnected = false;
        disconnectionTimer.stop();
        communicatorListener.onDisconnect();
        try {
            socket.close();
        } catch (IOException e) { }
    }

    /**
     * This method sends a request to the server
     * @param r the request to send
     */
    public void send(Request r, ResponseListener successListener, ErrorListener errorListener) {
        if(!isConnected) {
            errorListener.onError(new Exception("Server not connected"));
            Utils.LOGGER.info("Cannot send request, client is not connected");
            return;
        }

        try {
            if(outputStream == null)
                outputStream = new ObjectOutputStream(socket.getOutputStream());

            outputStream.writeObject(r);

            this.lastRequestSuccessListener = successListener;
            this.lastRequestErrorListener = errorListener;
            this.lastRequestTime = System.currentTimeMillis();

        }catch (IOException e){
            errorListener.onError(e);
            disconnect();
        }
    }

    public interface ResponseListener {
        void onResponse(Response r);
    }

    public interface ErrorListener {
        void onError(Exception error);
    }

    /**
     * Listener interface
     */
    public interface CommunicatorListener {
        void onUpdate(UpdateResponse r);
        void onDisconnect();
    }

}
