package network.storage;

import network.User;
import network.command.Cmd;
import network.command.Command;
import network.human.HumanBeing;

import java.io.IOException;
import java.util.List;

public interface StorageManagement {
    /**
     * Вывод в консоль информации о доступных командах
     */
    @Cmd
    String server_help(User user);


    @Cmd
    String all_info(User user);


    /**
     * Вывод в консоль информации о коллекции объектов класса {@code HumanBeing}
     */
    @Cmd
    String info(User user);


    /**
     * Вывод в консоль объектов коллекции объектов класса {@code HumanBeing}
     */
    @Cmd
    String show(User user);


    @Cmd
    String show_another(User user, String another);


    @Cmd
    String show_all(User user);


    /**
     * Добавление объекта в коллекцию объектов класса {@code HumanBeing}
     *
     * @param humanBeing Добавляемый объект
     */
    @Cmd
    String add(HumanBeing[] humanBeing, User user);


    /**
     * Обновление объекта коллекции объектов класса {@code HumanBeing}
     * по заданному id
     *
     * @param id         Id объекта
     * @param humanBeing Объект
     */
    @Cmd
    String update(int id, HumanBeing humanBeing, User user);


    /**
     * Удаление объекта коллекции объектов класса {@code HumanBeing}
     * по заданному id
     *
     * @param id Id удаляемого объекта
     */
    @Cmd
    String remove_by_id(int id, User user);


    /**
     * очистка коллеции объектов класса {@code HumanBeing}
     */
    @Cmd
    String clear(User user);


    /**
     * сохранение коллекции {объектов класса {@code HumanBeing} в файл
     */
    String save() throws IOException;




    /**
     * Принудительное завершение программы
     */
    String exit(User user);


    /**
     * Удаление из коллекции объектов класса {@code HumanBeing}
     * последнего добавленного элемента
     */
    @Cmd
    String remove_head(User user);


    /**
     * Удаление объектов коллекции объектов класса
     * {@code HumanBeing}, которые привышают заданный элемент
     *
     * @param delimiter Заданный элемент
     */
    @Cmd
    String remove_greater(HumanBeing delimiter, User user);


    /**
     * Вывод в консоль последних пяти команд
     */
    @Cmd
    String history(User user);


    /**
     * Вывод в консоль объекта коллекции объектов класса {@code HumanBeing},
     * который имеет максимальное значение переменной {@code mood}
     */
    @Cmd
    String max_by_mood(User user);


    /**
     * Вывод в консоль количества объектов коллекции объектов класса
     * {@code HumanBeing} с заданным значением поля {@code soundtrackName}
     *
     * @param soundtrackName Заданное значение поля {@code soundtrackName}
     */
    @Cmd
    String count_by_soundtrack_name(String soundtrackName, User user);


    /**
     * Вывод в консоль списка {@code soundtrackName} объектов коллекции
     * {@code humanBeings} в порядке убывания
     */
    @Cmd
    String print_field_descending_soundtrack_name(User user);

    /**
     * Выполнение скрипта из файла по заданному имени
     *
     * @param fileName Имя файла, в котором находится скрипт
     */
    @Cmd
    String execute_script(String fileName, User user);

    @Cmd
    String create_script(List<Command> script, String scriptName, User user);

    @Cmd
    String delete_script(String name, User user);

    @Cmd
    String delete_all_scripts(User user);

    @Cmd
    String scripts(User user);
}