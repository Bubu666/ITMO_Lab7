package network.storageCommands;

import network.User;
import network.storage.Storage;

/**
 * Команда "execute_script"
 */
public class ExecuteScript extends StorageCommand {

    /**
     * Имя файла
     */
    private String fileName;
    private User user;
    /**
     * Информация о использовании команды
     */
    public final static String helpInfo = "  execute_script file_name : считать и исполнить скрипт из указанного файла. \n";

    /**
     * Принимает имя файла
     * @param fileName Имя файла
     * @param user
     */
    public ExecuteScript(String fileName, User user) {
        super("execute_script");
        this.fileName = fileName;
        this.user = user;
    }

    public String getFileName() {
        return fileName;
    }

    /**
     * Реализация команды
     */
    @Override
    public String execute(Storage storage) {
        return storage.execute_script(fileName, user);
    }
}
