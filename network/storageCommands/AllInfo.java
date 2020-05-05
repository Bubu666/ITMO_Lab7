package network.storageCommands;

import network.User;
import network.storage.Storage;

public class AllInfo extends StorageCommand {

    private final User user;
    public static final String helpInfo = "  all_info : вывусти информацию о всех объектах коллекции\n";

    public AllInfo(User user) {
        super("all_info");
        this.user = user;
    }

    @Override
    public String execute(Storage storage) {
        return storage.all_info(user);
    }
}
