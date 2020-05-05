package network.storageCommands;

import network.User;
import network.storage.Storage;

/**
 * Команда "count_by_soundtrack_name"
 */
public class CountBySoundtrackName extends StorageCommand {

    /**
     * Название саундтрека
     */
    private String soundtrackName;
    private User user;
    /**
     * Информация о использовании команды
     */
    public final static String helpInfo = "  count_by_soundtrack_name soundtrack_name : вывести количество элементов, значение поля soundtrackName которых равно заданному\n  count_by_soundtrack_name random : (*)\n";

    /**
     * Принимает название саундтрека
     * @param soundtrackName Название саундтрека
     */
    public CountBySoundtrackName(String soundtrackName, User user) {
        super("count_by_soundtrack_name");
        this.soundtrackName = soundtrackName;
        this.user = user;
    }

    /**
     * Реализация команды
     */
    @Override
    public String execute(Storage storage) {
        return storage.count_by_soundtrack_name(soundtrackName, user);
    }
}
