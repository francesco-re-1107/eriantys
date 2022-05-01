package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.Utils;
import it.polimi.ingsw.common.Parser;
import it.polimi.ingsw.common.SerializationParser;
import it.polimi.ingsw.common.responses.Response;
import it.polimi.ingsw.common.requests.Request;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * This class handles the low level communication from client to server
 */
public class ClientServerCommunicator {
    /**
     * Server socket
     */
    private final Socket socket;


    /**
     * Used for serialization of the data, could be json or java serialization
     */
    private final Parser parser;

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
        this.parser = new SerializationParser(); //or JsonParser
    }

    /**
     * This method binds to the socket input stream and listens for response from the server
     */
    public void startListening() {
        try {
            Scanner in = new Scanner(socket.getInputStream());

            while (socket.isConnected()){
                String line = in.nextLine();

                Response r = parser.decodeResponse(line);
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
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            out.println(parser.encodeRequest(r));
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
