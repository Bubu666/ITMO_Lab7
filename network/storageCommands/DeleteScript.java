package network.storageCommands;

import network.User;
import network.storage.Storage;

import java.lang.reflect.InvocationTargetException;

public class DeleteScript extends StorageCommand {
    private String name;

    public static final String helpInfo = "  delete_script script_name : удалить указанный скрипт из каталога клиента\n";
    private User user;
    public DeleteScript(String name, User user) {
        super("delete_script");
        this.name = name;
        this.user = user;
    }

    @Override
    public String execute(Storage storage) throws InvocationTargetException, IllegalAccessException {
        return storage.delete_script(name, user);
    }
}
