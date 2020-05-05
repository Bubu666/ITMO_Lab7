package network.storageCommands;

import network.User;
import network.storage.Storage;

/**
 * Команда "exit"
 */
public class Exit extends StorageCommand {
    private User user;
    /**
     * Информация о использовании команды
     */
    public final static String helpInfo = "  exit : завершить программу\n";

    /**
     * Принимает ссылку на хранилище
     * @param user
     */
    public Exit(User user) {
        super("exit");
        this.user = user;
    }

    /**
     * Реализация команды
     */
    @Override
    public String execute(Storage storage) {
        return storage.exit(user);
    }
}
