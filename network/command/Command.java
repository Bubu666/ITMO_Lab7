package network.command;

import network.storage.Storage;

import java.lang.reflect.InvocationTargetException;

/**
 * Интерфейс для реализации команд
 */
public interface Command<R, S> {

    /**
     * Выполняет команду
     *
     * @throws InvocationTargetException _
     * @throws IllegalAccessException _
     */
    R execute(S object) throws InvocationTargetException, IllegalAccessException;
}
