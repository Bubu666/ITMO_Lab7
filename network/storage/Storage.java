package network.storage;

import network.command.Command;

/**
 * Содержит методы для работы с коллекциией объектов класса {@code HumanBeing}
 */
public interface Storage extends StorageManagement {

    String apply(Command<String, Storage> command, boolean history);
}