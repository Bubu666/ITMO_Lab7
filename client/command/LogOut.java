package client.command;

import network.User;

public class LogOut extends ClientCommand {

    public final static String helpInfo = "  log_out : выйти из учетной записи\n";

    public LogOut(User user) {
        super("log_out");
    }

    @Override
    public Integer execute(Integer input) {
        ClientCommandWorker.log_out();
        return input;
    }
}