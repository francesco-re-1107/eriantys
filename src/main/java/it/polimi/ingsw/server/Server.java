package it.polimi.ingsw.server;

import it.polimi.ingsw.server.controller.Controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class binds to the socket at the given port and listens for connections
 */
public class Server {

    /**
     * Port on which the server is listening
     */
    private final int port;

    /**
     * Thread pool used for clients
     */
    private ExecutorService executor;

    /**
     * Socket on which the server is listening
     */
    private ServerSocket serverSocket;

    /**
     * Main controller
     */
    private final Controller controller;

    /**
     * Create a server object with the given port
     * @param port
     */
    public Server(int port) {
        this.port = port;
        this.controller = new Controller();
    }

    /**
     * This method creates the socket and starts accepting clients.
     * For every client a VirtualView is created and assigned to a new thread.
     */
    public void startListening() {
        executor = Executors.newCachedThreadPool();
        try{
            serverSocket = new ServerSocket(port);
        }catch (IOException e){
            System.err.println(e.getMessage()); //port not available
        }
        System.out.println("Server ready");

        while (true){
            try{
                Socket socket = serverSocket.accept();
                executor.submit(new VirtualView(controller, socket));
            }catch(IOException e){
                System.err.println(e.getMessage()); //socket closed
            }
        }
    }
}