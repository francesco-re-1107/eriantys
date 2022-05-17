package it.polimi.ingsw.server;

import it.polimi.ingsw.Constants;
import it.polimi.ingsw.Utils;
import it.polimi.ingsw.common.requests.PingRequest;
import it.polimi.ingsw.common.requests.Request;
import it.polimi.ingsw.common.responses.Response;
import it.polimi.ingsw.common.responses.replies.AckReply;

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
     * This flag represents if the client is still connected
     */
    private boolean isConnected = true;

    /**
     * Output stream of the socket
     */
    private ObjectOutputStream outputStream;

    /**
     * Instantiates a communicator
     *
     * @param socket   the client socket
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
            this.socket.setSoTimeout(Constants.DISCONNECTION_TIMEOUT);
            var in = new ObjectInputStream(socket.getInputStream());

            while (socket.isConnected()) {
                var r = (Request) in.readObject();

                //ping requests are bounced back to the client immediately
                if (r instanceof PingRequest) {
                    send(new AckReply(r.getRequestId()));
                } else { //otherwise, the request is forwarded to the listener
                    Utils.LOGGER.info("Request received: " + r.getClass().getSimpleName());
                    communicatorListener.onRequest(r);
                }
            }

            disconnect();
        } catch (IOException e) {
            //IOException is thrown when the client is disconnected
            disconnect();
        } catch (ClassNotFoundException e) {
            //the received object is not a request
            Utils.LOGGER.warning("Client transmitted something illegal");
            e.printStackTrace();
        }
    }

    /**
     * This method is used internally to notify listener that the client disconnected and to close the socket
     */
    private void disconnect() {
        if (!isConnected) return;

        Utils.LOGGER.info("Client disconnected");
        isConnected = false;
        communicatorListener.onDisconnect();
        try {
            socket.close();
        } catch (IOException e) {
            Utils.LOGGER.finest("Cannot close socket");
        }
    }

    /**
     * This method sends a response to the client
     *
     * @param r the response to send
     */
    public void send(Response r) {
        if (!isConnected) {
            Utils.LOGGER.info("Cannot send response, client is not connected");
            return;
        }

        try {
            //open the output stream if it is not already opened
            if (outputStream == null)
                outputStream = new ObjectOutputStream(socket.getOutputStream());
            //write request to the socket
            outputStream.writeObject(r);
        } catch (IOException e) {
            //IOException is thrown when the client is disconnected
            disconnect();
        }
    }

    /**
     * Whether the client is still connected
     * @return true if the client is still connected, false otherwise
     */
    public boolean isConnected() {
        return isConnected;
    }

    /**
     * TESTING PURPOSES ONLY
     */
    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    /**
     * Listener interface
     */
    public interface CommunicatorListener {
        /**
         * This method is called when a request is received
         */
        void onRequest(Request r);

        /**
         * This method is called when the client disconnects
         */
        void onDisconnect();
    }
}
