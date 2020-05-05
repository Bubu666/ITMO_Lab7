package network.storage;

import network.command.Command;
import network.command.HelpInfo;
import network.human.HumanBeing;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.LinkedList;

public abstract class AbstractStorage implements Storage {

    /**
     * Список объектов класса {@code HumanBeing}
     */
    protected LinkedList<HumanBeing> humanBeings;


    /**
     * Список вводимых команд {@code Command}
     */
    protected LinkedList<Command> commandList;

    /**
     * Содержит всю информацию о командах
     */
    protected static String helpInfo;


    /**
     * Дата инициализации коллекции
     */
    protected Date dateOfInitialization;

    {
        humanBeings = new LinkedList<>();
        commandList = new LinkedList<>();
        dateOfInitialization = new Date();
    }

    protected AbstractStorage(Class storage) {
        StringBuilder info = new StringBuilder();
        for (Method m : storage.getDeclaredMethods()) {
            if (m.isAnnotationPresent(HelpInfo.class)) {
                info.append(m.getAnnotation(HelpInfo.class).info());
            }
        }
        helpInfo = info.toString();
    }
}
