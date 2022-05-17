package it.polimi.ingsw.client;

import it.polimi.ingsw.Constants;
import it.polimi.ingsw.Utils;
import it.polimi.ingsw.common.requests.PingRequest;
import it.polimi.ingsw.common.requests.Request;
import it.polimi.ingsw.common.responses.Reply;
import it.polimi.ingsw.common.responses.Response;
import it.polimi.ingsw.common.responses.Update;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
     * This flag represents if the client is still connected
     */
    private boolean isConnected = true;

    /**
     * This map stores the listeners for every pending request
     */
    private final Map<UUID, ListenersPair> pendingRequests;

    /**
     * Output stream of the socket
     */
    private ObjectOutputStream outputStream;

    /**
     * Instantiates a communicator
     * @param socket the server socket
     * @param listener response listener
     */
    public ClientServerCommunicator(Socket socket, CommunicatorListener listener) {
        this.socket = socket;
        this.communicatorListener = listener;
        this.pendingRequests = new HashMap<>();
    }

    /**
     * This method binds to the socket input stream and listens for response from the server on a new thread
     */
    public void startListening() {
        startPinging();
        new Thread(() -> {
            try {
                this.socket.setSoTimeout(Constants.DISCONNECTION_TIMEOUT);
                var in = new ObjectInputStream(socket.getInputStream());

                while (socket.isConnected()) {
                    var r = (Response) in.readObject();

                    //it's an update response
                    if (r instanceof Update u) {
                        communicatorListener.onUpdate(u);
                    } else if (r instanceof Reply re) { //it's a request reply
                        pendingRequests.get(re.getRequestId()).successListener.onSuccess(re);
                        pendingRequests.remove(re.getRequestId());
                    }
                }

                disconnect();
            } catch (IOException e) {
                disconnect();
            } catch (ClassNotFoundException e) {
                Utils.LOGGER.warning("Server transmitted something illegal");
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Send ping requests to the client every {@link Constants#PING_INTERVAL} milliseconds
     */
    private void startPinging() {
        new Thread(() -> {
            while (isConnected) {
                try {
                    send(new PingRequest());
                    Thread.sleep(Constants.PING_INTERVAL);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }

    /**
     * This method is used internally to notify listener that the server disconnected and to close the socket
     */
    private void disconnect() {
        if(!isConnected) return;

        Utils.LOGGER.info("Server disconnected");
        isConnected = false;
        communicatorListener.onDisconnect();

        //notify error for all pending requests and remove them from the map
        pendingRequests.values().forEach(v -> v.errorListener.onError(new IOException("Server disconnected")));
        pendingRequests.clear();

        try {
            socket.close();
        } catch (IOException e) {
            Utils.LOGGER.finest("Cannot close socket");
        }
    }


    /**
     * Send a request and do nothing when it completes.
     *
     * @param request The request to send.
     */
    public void send(Request request) {
        send(request, r -> {}, e -> {});
    }


    /**
     * Send a request and call the successListener when the request succeeds.
     *
     * @param request The request to send.
     * @param successListener A callback that will be called if the request is successful.
     */
    public void send(Request request, SuccessListener successListener) {
        send(request, successListener, e -> {});
    }


    /**
     * Send a request to the server, and call the successListener if the request succeeds, or the errorListener if it
     * fails.
     *
     * @param request The request to send.
     * @param successListener A callback that will be called if the request is successful.
     * @param errorListener A callback that will be called if the request fails.
     */
    public void send(Request request, SuccessListener successListener, ErrorListener errorListener) {
        if(!isConnected) {
            errorListener.onError(new Exception("Server not connected"));
            Utils.LOGGER.info("Cannot send request, client is not connected");
            return;
        }

        try {
            if(outputStream == null)
                outputStream = new ObjectOutputStream(socket.getOutputStream());

            outputStream.writeObject(request);
            pendingRequests.put(request.getRequestId(), new ListenersPair(successListener, errorListener));
        }catch (IOException e){
            errorListener.onError(e);
            disconnect();
        }
    }

    /**
     * This listener is called when the request is successful.
     */
    public interface SuccessListener {
        void onSuccess(Reply r);
    }

    /**
     * This listener is called when the request fails.
     */
    public interface ErrorListener {
        void onError(Exception exception);
    }

    /**
     * Listener interface
     */
    public interface CommunicatorListener {
        void onUpdate(Update u);
        void onDisconnect();
    }

    /**
     * This class is used internally to store the listeners for a request.
     * @param successListener
     * @param errorListener
     */
    private record ListenersPair(SuccessListener successListener, ErrorListener errorListener) {}
}
