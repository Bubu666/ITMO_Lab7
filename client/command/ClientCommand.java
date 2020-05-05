package client.command;

import network.command.AbstractCommand;
import network.command.Command;

public abstract class ClientCommand extends AbstractCommand implements Command<Integer, Integer> {
    public ClientCommand(String name) {
        super(name);
    }
}
