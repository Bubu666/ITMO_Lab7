package network.storageCommands;

import network.User;
import network.storage.Storage;


public class ShowAnother extends StorageCommand {
    private final String another;
    private final User user;

    public static final String helpInfo = "  show_another user : Вывести элементы коллекции заданного пользователя\n";

    public ShowAnother(User user, String another) {
        super("show_another");
        this.another = another;
        this.user = user;
    }

    @Override
    public String execute(Storage storage) {
        return storage.show_another(user, another);
    }
}
