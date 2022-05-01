package it.polimi.ingsw;

import it.polimi.ingsw.server.Server;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        //rough arguments parsing to determine if server or client must be started
        try {
            if (args[0].equals("server")) {
                startServer();
            } else if (args[0].equals("client")) {
                startClient(args[1].equals("--gui"));
            }
        }catch (Exception e) {
            Utils.LOGGER.severe("Arguments error");
            System.exit(0);
        }
    }

    /**
     * Start the server
     */
    public static void startServer() {
        new Server(Utils.GetAppConfig().port())
                .startListening();
    }

    /**
     * Start the client with cli or gui respectively
     * @param gui whether the client must be started with gui or not.
     */
    public static void startClient(boolean gui) {
        if(gui) {
            Utils.LOGGER.info("Starting client with GUI");
        }else{
            Utils.LOGGER.info("Starting CLI client");
        }
    }
}
