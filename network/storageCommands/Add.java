package network.storageCommands;

import network.User;
import network.human.HumanBeing;
import network.storage.Storage;

/**
 * Команда "add"
 */
public class Add extends StorageCommand {

    /**
     * Введенный пользователем объект
     */
    private HumanBeing[] people;

    private User user;

    /**
     * Информация о использовании команды
     */
    public final static String helpInfo = "  add : добавить новый элемент в коллекцию\n  add name_of_human\n  add random : (*)\n  add random num_of_elements : (*)\n";

    /**
     * Принимает ссылку на новый объект {@code HumanBeing}
     * @param people Новый объект
     */
    public Add(HumanBeing[] people, User user) {
        super("add");
        this.people = people;
        this.user = user;
    }

    /**
     * Реализация команды
     */
    @Override
    public String execute(Storage storage) {
        return storage.add(people, user);
    }
}