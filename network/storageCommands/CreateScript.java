package network.storageCommands;

import network.User;
import network.command.Command;
import network.storage.Storage;

import java.util.List;

public class CreateScript extends StorageCommand {
    public final static String helpInfo = "  create_script script_file_name : создать скрипт с указанным для него названием файла.\n";
    private List<Command> script;
    private String scriptFileName;
    private User user;

    public CreateScript(String scriptFileName, List<Command> script, User user) {
        super("create_script");
        this.scriptFileName = scriptFileName;
        this.script = script;
        this.user = user;
    }

    @Override
    public String execute(Storage storage) {
        return storage.create_script(script, scriptFileName, user);
    }
}
