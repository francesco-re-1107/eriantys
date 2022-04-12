package it.polimi.ingsw.server;

import it.polimi.ingsw.common.Parser;
import it.polimi.ingsw.common.responses.Response;
import it.polimi.ingsw.common.requests.Request;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ServerClientCommunicator {

    private final Socket socket;

    private final CommunicatorListener communicatorListener;

    private Parser parser;

    public ServerClientCommunicator(Socket socket, CommunicatorListener listener) {
        this.socket = socket;
        this.communicatorListener = listener;
        this.parser = new SerializationParser(); //or JsonParser
    }

    public void startListening() {
        try {
            Scanner in = new Scanner(socket.getInputStream());

            while (socket.isConnected()){
                String line = in.nextLine();

                Request r = parser.parseRequest(line);
                communicatorListener.onRequest(r);
            }

            //close connections
            in.close();
            socket.close();
            communicatorListener.onDisconnect();

        } catch (IOException e){
            System.err.println(e.getMessage());
        }
    }

    public void send(Response r){
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            out.println(parser.encodeResponse(r));
        }catch (IOException e){
            System.err.println(e.getMessage());
        }
    }

    public interface CommunicatorListener {
        void onRequest(Request r);
        void onDisconnect();
    }
}
