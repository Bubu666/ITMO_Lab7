package network.storageCommands;

import network.User;
import network.human.HumanBeing;
import network.storage.Storage;

/**
 * Команда "update"
 */
public class Update extends StorageCommand {

    /**
     * Введенный пользователем объект
     */
    private HumanBeing humanBeing;

    /**
     * id объекта
     */
    private int id;
    private User user;
    /**
     * Информация о использовании команды
     */
    public final static String helpInfo = "  update id : обновить значение элемента коллекции, id которого равен заданному\n  update id random : (*)\n";

    /**
     * Принимает id объекта и объект {@code HumanBeing}
     * @param id Id
     * @param newHumanBeing Новый объект
     * @param user
     */
    public Update(int id, HumanBeing newHumanBeing, User user) {
        super("update");
        this.id = id;
        this.humanBeing = newHumanBeing;
        this.user = user;
    }

    /**
     * Реализация команды
     */
    @Override
    public String execute(Storage storage) {
        return storage.update(id, humanBeing, user);
    }
}
