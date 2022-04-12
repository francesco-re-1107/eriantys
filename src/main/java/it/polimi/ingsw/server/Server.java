package it.polimi.ingsw.server;

import it.polimi.ingsw.server.controller.Controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private final int port;

    private ExecutorService executor;

    private ServerSocket serverSocket;

    private final Controller controller;

    public Server(int port) {
        this.port = port;
        this.controller = new Controller();
    }

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

    public int getPort() {
        return port;
    }
}
