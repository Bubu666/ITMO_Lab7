package network.storageCommands;

import network.User;
import network.storage.Storage;

/**
 * Команда "max_by_mood"
 */
public class MaxByMood extends StorageCommand {
    private User user;
    /**
     * Информация о использовании команды
     */
    public final static String helpInfo = "  max_by_mood : вывести любой объект из коллекции, значение поля mood которого является максимальным\n";

    public MaxByMood(User user) {
        super("max_by_mood");
        this.user = user;
    }

    /**
     * Реализация команды
     */
    @Override
    public String execute(Storage storage) {
        return storage.max_by_mood(user);
    }
}
