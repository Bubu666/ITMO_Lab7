package network.storageCommands;

import network.User;
import network.storage.Storage;

/**
 * Команда "info"
 */
public class Info extends StorageCommand {
    private User user;
    /**
     * Информация о использовании команды
     */
    public final static String helpInfo = "  info : вывести информацию о коллекции\n";

    public Info(User user) {
        super("info");
        this.user = user;
    }

    /**
     * Реализация команды
     */
    @Override
    public String execute(Storage storage) {
        return storage.info(user);
    }
}
