package client.command;

import network.User;

import java.lang.reflect.InvocationTargetException;

public class Exit extends ClientCommand {

    public static final String helpInfo = "  exit/quit : завершение работы";

    public Exit(User user) {
        super("exit");
    }

    @Override
    public Integer execute(Integer object) {
        ClientCommandWorker.exit();
        return object;
    }
}
