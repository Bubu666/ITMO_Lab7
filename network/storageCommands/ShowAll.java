package network.storageCommands;

import network.User;
import network.storage.Storage;

import java.lang.reflect.InvocationTargetException;

public class ShowAll extends StorageCommand {
    private final User user;

    public static final String helpInfo = "  show_all : Вывести все элементы коллекции\n";

    public ShowAll(User user) {
        super("show_all");
        this.user = user;
    }

    @Override
    public String execute(Storage storage) throws InvocationTargetException, IllegalAccessException {
        return storage.show_all(user);
    }
}
