package network.storageCommands;

import network.User;
import network.storage.Storage;

import java.lang.reflect.InvocationTargetException;

public class DeleteAllScripts extends StorageCommand {

    public static final String helpInfo = "  delete_all_scripts : удалить все скрипты в каталоге клиента\n";
    private User user;
    public DeleteAllScripts(User user) {
        super("delete_all_scripts");
        this.user = user;
    }

    @Override
    public String execute(Storage storage) throws InvocationTargetException, IllegalAccessException {
        return storage.delete_all_scripts(user);
    }
}
