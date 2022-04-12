package it.polimi.ingsw;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.common.reducedmodel.ReducedGame;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println(args);

        try {
            if (args[0].equals("server")) {
                startServer();
            } else if (args[0].equals("client")) {
                startClient();
            }
        }catch (Exception e) {
            System.err.println("Arguments error");
        }
    }

    public static void startServer() {
        new Server(readPortFromProperties())
                .startListening();
    }

    public static void startClient() {
        //...
    }

    private static int readPortFromProperties() {
        Properties prop = new Properties();

        try (FileInputStream fis = new FileInputStream(Constants.CONFIG_FILE_PATH)) {
            prop.load(fis);

            return Integer.parseInt(prop.getProperty("server.port"));
        } catch (Exception e) {
            return Constants.DEFAULT_SERVER_PORT;
        }

    }
}
