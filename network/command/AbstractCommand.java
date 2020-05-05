package network.command;

import network.storageCommands.StorageCommand;

import java.io.Serializable;
import java.util.Objects;

public class AbstractCommand implements Serializable {
    /**
     * Имя команды в пользовательском представлении
     */
    protected final String name;

    /**
     * Принимает название команды
     * @param name Название команды
     */

    public AbstractCommand(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StorageCommand)) return false;
        StorageCommand that = (StorageCommand) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
