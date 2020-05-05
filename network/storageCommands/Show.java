package network.storageCommands;

import network.User;
import network.storage.Storage;

/**
 * Команда "show"
 */
public class Show extends StorageCommand {
    private User user;
    /**
     * Информация о использовании команды
     */
    public final static String helpInfo = "  show : вывести все элементы коллекции в строковом представлении\n";

    public Show(User user) {
        super("show");
        this.user = user;
    }

    /**
     * Реализация команды
     */
    @Override
    public String execute(Storage storage) {
        return storage.show(user);
    }
}
