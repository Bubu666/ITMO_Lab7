package network.storageCommands;

import network.User;
import network.storage.Storage;

public class Scripts extends StorageCommand {
    public static final String helpInfo = "  scripts : выводит список скриптов клиента.\n";
    private User user;
    public Scripts(User user) {
        super("scripts");
        this.user = user;
    }

    @Override
    public String execute(Storage storage) {
        return storage.scripts(user);
    }
}
