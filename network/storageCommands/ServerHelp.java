package network.storageCommands;

import network.User;
import network.storage.Storage;

/**
 * Команда "help"
 */
public class ServerHelp extends StorageCommand {
    private User user;
    /**
     * Информация о использовании команды
     */
    public final static String helpInfo = "  help : вывести справку по доступным командам\n";

    public ServerHelp(User user) {
        super("help");
        this.user = user;
    }

    /**
     * Реализация команды
     */
    @Override
    public String execute(Storage storage) {
        return storage.server_help(user);
    }
}
