package it.polimi.ingsw;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.common.reducedmodel.ReducedGame;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        Game g = new Game(2, true);

        try {
            g.addPlayer("p1");
            g.addPlayer("p2");
        }catch (Exception e){}

        g.startGame();

        /*Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();


        String json = gson.toJson(ReducedGame.fromGame(g));
        System.out.println(json);

        ReducedGame rg = gson.fromJson(json, ReducedGame.class);
        System.out.println(rg.players().get(0).entrance());
        */

        ObjectMapper mapper = new ObjectMapper();

        try {
            String jsonString = mapper.writeValueAsString(ReducedGame.fromGame(g));
            System.out.println(jsonString);

            ReducedGame rg = mapper.readValue(jsonString, ReducedGame.class);
            System.out.println(rg.players().get(0).entrance());
        }catch (Exception e){
            e.printStackTrace();
        }


    }
}
