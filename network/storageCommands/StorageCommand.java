package network.storageCommands;

import network.command.AbstractCommand;
import network.command.Command;
import network.storage.Storage;

import java.io.Serializable;
import java.util.Objects;

public abstract class StorageCommand extends AbstractCommand implements Command<String, Storage>, Serializable {

    protected StorageCommand(String name) {
        super(name);
    }
}
