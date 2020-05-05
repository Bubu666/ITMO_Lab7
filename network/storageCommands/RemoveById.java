package network.storageCommands;

import network.User;
import network.storage.Storage;

/**
 * Команда "remove_by_id"
 */
public class RemoveById extends StorageCommand {
    private User user;
    /**
     * id объекта
     */
    private int id;

    /**
     * Информация о использовании команды
     */
    public final static String helpInfo = "  remove_by_id id : удалить элемент из коллекции по его id\n";

    /**
     * Принимает id объекта
     * @param user
     * @param id Id объекта
     */
    public RemoveById(User user, int id) {
        super("remove_by_id");
        this.user = user;
        this.id = id;
    }

    /**
     * Реализация команды
     */
    @Override
    public String execute(Storage storage) {
        return storage.remove_by_id(id, user);
    }
}
