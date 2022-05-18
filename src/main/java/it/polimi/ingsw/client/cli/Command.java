package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.cli.commands.ExitCommand;

public abstract class Command {
    //...

    public static Command parse(String command) {
        return new ExitCommand();
    }
}
