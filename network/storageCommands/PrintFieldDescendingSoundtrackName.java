package network.storageCommands;

import network.User;
import network.storage.Storage;

/**
 * Команда "print_field_descending_soundtrack_name"
 */
public class PrintFieldDescendingSoundtrackName extends StorageCommand {
    private User user;
    /**
     * Информация о использовании команды
     */
    public final static String helpInfo = "  print_field_descending_soundtrack_name : вывести значения поля soundtrackName в порядке убывания\n";

    public PrintFieldDescendingSoundtrackName(User user) {
        super("print_field_descending_soundtrack_name");
        this.user = user;
    }

    /**
     * Реализация команды
     */
    @Override
    public String execute(Storage storage) {
        return storage.print_field_descending_soundtrack_name(user);
    }
}
