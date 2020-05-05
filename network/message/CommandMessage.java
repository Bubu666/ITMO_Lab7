package network.message;

import network.command.Command;

import java.io.Serializable;

public class CommandMessage extends Message implements Serializable {
    public final Command command;

    public CommandMessage(String content, Command command) {
        super(content);
        this.command = command;
    }

    @Override
    public String toString() {
        return "Command" + super.toString() + " {command = " + command + "}";
    }
}
