package network.storageCommands;

import network.User;
import network.human.HumanBeing;
import network.storage.Storage;

/**
 * Команда "remove_greater"
 */
public class RemoveGreater extends StorageCommand {
    private User user;
    /**
     * Введенный пользователем объект
     */
    private HumanBeing humanBeing;

    /**
     * Информация о использовании команды
     */
    public final static String helpInfo = "  remove_greater : удалить из коллекции все элементы, превышающие заданный\n  remove_greater name_of_human\n  remove_greater random : (*)\n";

    /**
     * Принимает объект {@code HumanBeing}
     * @param user
     * @param humanBeing Объект
     */
    public RemoveGreater(User user, HumanBeing humanBeing) {
        super("remove_greater");
        this.user = user;
        this.humanBeing = humanBeing;
    }

    /**
     * Реализация команды
     */
    @Override
    public String execute(Storage storage) {
        return storage.remove_greater(humanBeing, user);
    }
}
