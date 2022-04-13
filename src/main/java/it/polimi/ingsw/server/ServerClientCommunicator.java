package it.polimi.ingsw.server;

import it.polimi.ingsw.common.Parser;
import it.polimi.ingsw.common.SerializationParser;
import it.polimi.ingsw.common.responses.Response;
import it.polimi.ingsw.common.requests.Request;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

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
     * Used for serialization of the data, could be json or java serialization
     */
    private final Parser parser;

    /**
     * Instantiates a communicator
     * @param socket the client socket
     * @param listener request listener
     */
    public ServerClientCommunicator(Socket socket, CommunicatorListener listener) {
        this.socket = socket;
        this.communicatorListener = listener;
        this.parser = new SerializationParser(); //or JsonParser
    }

    /**
     * This method binds to the socket input stream and listens for requests from the client
     */
    public void startListening() {
        try {
            Scanner in = new Scanner(socket.getInputStream());

            while (socket.isConnected()){
                String line = in.nextLine();

                Request r = parser.decodeRequest(line);
                communicatorListener.onRequest(r);
            }

            //close connections
            in.close();
            socket.close();
            communicatorListener.onDisconnect();

        } catch (Exception e){
            System.err.println(e.getMessage());
        }
    }

    /**
     * This method sends a response to the client
     * @param r the response to send
     */
    public void send(Response r){
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            out.println(parser.encodeResponse(r));
        }catch (IOException e){
            System.err.println(e.getMessage());
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
