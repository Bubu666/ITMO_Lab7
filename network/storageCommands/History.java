package network.storageCommands;

import network.User;
import network.storage.Storage;

/**
 * Команда "history"
 */
public class History extends StorageCommand {
    private User user;
    /**
     * Информация о использовании команды
     */
    public final static String helpInfo = "  history : вывести последние 5 команд\n";

    public History(User user) {
        super("history");
        this.user = user;
    }

    /**
     * Реализация команды
     */
    @Override
    public String execute(Storage storage) {
        return storage.history(user);
    }
}
