package network.storageCommands;

import network.User;
import network.storage.Storage;

/**
 * Команда "clear"
 */
public class Clear extends StorageCommand {
    private User user;
    /**
     * Информация о использовании команды
     */
    public final static String helpInfo = "  clear : очистить коллекцию\n";

    public Clear(User user) {
        super("clear");
        this.user = user;
    }

    /**
     * Реализация команды
     */
    @Override
    public String execute(Storage storage) {
        return storage.clear(user);
    }
}
