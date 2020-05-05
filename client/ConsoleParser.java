package client;

import client.command.ClientCommandWorker;
import client.handle.ConsoleHandler;
import network.command.Cmd;
import network.command.Command;
import network.human.Car;
import network.human.Coordinates;
import network.human.HumanBeing;
import network.human.Mood;
import network.storage.StorageManagement;
import network.storageCommands.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Предназначен для считывания и обработки пользователььских действий над объектом класса {@code server.Storage}
 */
public class ConsoleParser {

    /**
     * {@code Scanner} для ввода пользовательской информации
     */
    private Scanner input;

    /**
     * Поток вывода информации
     */
    private PrintStream output = System.out;

    /**
     * Счетчик неправильно введенных команд
     */
    private int wrongCounter = 0;

    /**
     * Содержит текст, который выводится в консоль для
     * оповещения пользователя о том, что он ввел неверные аргументы команды.
     */
    private final String wrongArguments = "  Не правильные аргументы. Попробуйте заново\n  Для получения справки о доступных командах используйте \"help\" и \"server_help\".";

    /**
     * Содержит текст, который выводится в консоль для
     * оповещения пользователя о том, что значение поля не может быть {@code null}.
     */
    private final String wrongValue = "  Значение не может быть null\n> ";

    /**
     * Содержит текст, который выводится в консоль для
     * оповещения пользователя о том, что он ввел несуществующую команду.
     */
    private final String wrongCommand = "  Не верная команда. Для получения справки используйте \"help\" и \"server_help\".";

    /**
     * Содержит текст, который выводится в консоль для
     * оповещения пользователя о том, что он ввел некорректные аргументы команды
     */
    private final String wrongFormat = "  Неверный формат, попробуйте еще раз\n> ";

    /**
     * Переменная для оповещения программы о том,
     * что пользователь отказался вводить некоторые команды.
     */
    private boolean wasBroken;

    /**
     * Переменная хранит значение true, если обработка команд происходит
     * из стандартного потока System.in, иначе значение false
     */
    private boolean isConsole = false;

    private final static int MAX_PEOPLE = 200;

    /**
     * Хранит допустимые серверные команды.
     * Ключ - название команды, значение - конструктор, по которому можно создать экземпляр соответствующего класса команды
     */
    private static final HashMap<String, Constructor> serverCommands = new HashMap<>();

    /**
     * Хранит допустимые клиентские команды.
     * Ключ - название команды, значение - конструктор, по которому можно создать экземпляр соответствующего класса команды
     */
    private static final HashMap<String, Constructor> clientCommands = new HashMap<>();

    static {
        try {
            for (Method m : StorageManagement.class.getDeclaredMethods()) {
                if (m.isAnnotationPresent(Cmd.class)) {
                    serverCommands.put(m.getName(),
                            Class.forName("network.storageCommands." + castToCommandClass(m.getName())).getConstructors()[0]);
                }
            }
            for (Method m: ClientCommandWorker.class.getDeclaredMethods()) {
                if (m.isAnnotationPresent(Cmd.class)) {
                    clientCommands.put(m.getName(), Class.forName("client.command." + castToCommandClass(m.getName())).getConstructors()[0]);
                }
            }
            clientCommands.put("quit", clientCommands.get("exit"));
            serverCommands.put("script", serverCommands.get("execute_script"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Слушает пользовательские команды (или же команды, описанные в скрипте).
     * Метод разбивает введенную пользователем строку, содержащую
     * команду и ее аргументы, на массив слов и отправляет ссылку на этот
     * массив методу {@code handle} для обработки команды.
     *
     * @param stream - поток, из которого произвадится чтение команд
     */
    public Command getCommand(InputStream stream) {
        input = new Scanner(new InputStreamReader(stream));

        if (stream.equals(System.in)) {
            isConsole = true;
        }

        String message = input.nextLine();

        if (Authentication.started) {
            Authentication.inputString = message;
            return null;
        }

        if (ConsoleHandler.waitingForAnswer) {
            ConsoleHandler.inputString = message;
            return null;
        }

        String[] words = Arrays.stream(message.trim().split(" ")).filter(w -> !w.equals(""))
                .toArray(String[]::new);

        return words.length > 0 ? handle(words) : null;
    }

    private List<Command> makeScript() {
        output.println("  Вводите команды с их аргументами построчно.\n  Концом скрипта является слово \"end\". \n  break для отмены ввода");

        LinkedList<Command> script = new LinkedList<>();

        System.out.print("\r> ");

        String message;
        while (!(message = input.nextLine()).equals("end")) {

            if (message.equalsIgnoreCase("break"))
                return null;

            final String[] words = Arrays.stream(message.trim().split(" ")).filter(w -> !w.equals(""))
                    .toArray(String[]::new);

            final Command command = words.length > 0 ? handle(words) : null;

            if (command != null) {
                script.add(command);
            }

            System.out.print("\r> ");
        }

        return script;
    }

    /**
     * Метод для обработки пользовательского ввода, формарования объектов
     * команд {@code Command} и отправки этих команд методу {@code apply} класса {@code server.Storage}.
     * <p>
     * Если пользовательская команда состоит всего из одного слова и не имеет аргументов,
     * то формирование команды происходит с помощью средств {@code java.lang.reflect}
     * и метода {@code castToCommandClass}
     *
     * @param command - массив фрагментов команды
     */
    public Command handle(String[] command) {
        if (command == null || command.length == 0 || command[0].equals(""))
            return null;

        Message m = commandBuilder(command);

        if (m == null) {
            if (++wrongCounter != 11)
                output.println(wrongCommand);
            else
                output.println("  У тебя все в порядке?");
            return null;
        }

        switch (m.command) {
            case "show_another": {
                if (m.args.length == 1) {
                    return new ShowAnother(Client.account, m.args[0]);
                } else {
                    output.println(wrongArguments);
                    return null;
                }
            }

            case "delete_script": {
                if (m.args.length == 1) {
                    return new DeleteScript(m.args[0], Client.account);
                } else {
                    output.println(wrongArguments);
                    return null;
                }
            }

            case "create_script": {
                if (m.args.length == 1) {
                    List<Command> script = makeScript();

                    return script == null ? null : new CreateScript(m.args[0], script, Client.account);

                } else {
                    output.println(wrongArguments);
                    return null;
                }
            }

            case "add": {
                HumanBeing newHumanBeing;
                if (m.args.length > 0) {
                    StringBuilder name;

                    if (m.args[0].equalsIgnoreCase("RANDOM")) {
                        if (m.args.length == 2) {

                            try {
                                int num = m.args[1].equalsIgnoreCase("max")
                                        ? MAX_PEOPLE
                                        : Math.min(Integer.parseInt(m.args[1]), MAX_PEOPLE);

                                HumanBeing[] people = new HumanBeing[num];
                                for (int i = 0; i < num; ++i) {
                                    people[i] = HumanBeing.getHuman();
                                }

                                return new Add(people, Client.account);

                            } catch (NumberFormatException e) {
                                output.println(wrongArguments);
                                return null;
                            }

                        } else if (m.args.length != 1) {
                            output.println(wrongArguments);
                            return null;
                        }
                        newHumanBeing = HumanBeing.getHuman();

                    } else {
                        name = new StringBuilder(m.args[0]);

                        for (int i = 1; i < m.args.length; ++i) {
                            name.append(" ").append(m.args[i]);
                        }

                        newHumanBeing = offerToEnterHuman(name.toString());

                        if (newHumanBeing == null)
                            break;
                    }

                    return new Add(new HumanBeing[]{newHumanBeing}, Client.account);

                } else {
                    newHumanBeing = offerToEnterHuman(null);
                    if (newHumanBeing != null)
                        return new Add(new HumanBeing[]{newHumanBeing}, Client.account);
                }

                return null;
            }

            case "update": {
                HumanBeing newHumanBeing;
                if (m.args.length > 1) {
                    try {
                        int id = Integer.parseInt(m.args[0]);
                        if (m.args[1].equalsIgnoreCase("RANDOM")) {
                            if (m.args.length == 2) {
                                newHumanBeing = HumanBeing.getHuman();
                                return new Update(id, newHumanBeing, Client.account);
                            } else {
                                output.println(wrongArguments);
                                return null;
                            }
                        } else {
                            StringBuilder name = new StringBuilder(m.args[1]);
                            for (int i = 2; i < m.args.length; ++i) {
                                name.append(" ").append(m.args[i]);
                            }
                            newHumanBeing = offerToEnterHuman(name.toString());
                            if (newHumanBeing == null)
                                return null;
                            return new Update(id, newHumanBeing, Client.account);
                        }

                    } catch (NumberFormatException e) {
                        output.println(wrongArguments);
                    }

                } else if (m.args.length == 1) {

                    try {
                        int id = Integer.parseInt(m.args[0]);
                        newHumanBeing = offerToEnterHuman(null);
                        if (newHumanBeing != null)
                            return new Update(id, newHumanBeing, Client.account);
                    } catch (NumberFormatException e) {
                        output.println(wrongArguments);
                    }

                } else {
                    output.println(wrongArguments);
                }
                return null;
            }

            case "remove_by_id": {

                if (m.args.length == 1) {
                    try {
                        int id = Integer.parseInt(m.args[0]);

                        return new RemoveById(Client.account, id);
                    } catch (NumberFormatException e) {
                        output.println(wrongArguments);
                    }

                } else {
                    output.println(wrongArguments);
                }
                return null;
            }

            case "script":
            case "execute_script": {
                if (m.args.length == 1) {
                    return new ExecuteScript(m.args[0], Client.account);
                } else {
                    output.println(wrongArguments);
                }
                return null;
            }

            case "remove_greater": {
                if (m.args.length > 0) {
                    HumanBeing humanBeing;

                    if (m.args[0].equalsIgnoreCase("RANDOM")) {

                        if (m.args.length == 1)
                            humanBeing = HumanBeing.getHuman();
                        else {
                            output.println(wrongArguments);
                            return null;
                        }

                    } else {
                        StringBuilder name = new StringBuilder(m.args[1]);

                        for (int i = 1; i < m.args.length; ++i) {
                            name.append(" ").append(m.args[i]);
                        }
                        humanBeing = offerToEnterHuman(name.toString());
                    }

                    if (humanBeing == null)
                        return null;

                    return new RemoveGreater(Client.account, humanBeing);

                } else {
                    output.println(wrongArguments);
                }
                return null;
            }

            case "count_by_soundtrack_name": {
                if (m.args.length > 0) {
                    String soundtrack;

                    if (m.args[0].equalsIgnoreCase("RANDOM")) {

                        if (m.args.length == 1)
                            soundtrack = HumanBeing.getRandomSoundtrack();
                        else {
                            output.println(wrongArguments);
                            return null;
                        }

                    } else {
                        StringBuilder song = new StringBuilder(m.args[0]);

                        for (int i = 1; i < m.args.length; ++i) {
                            song.append(m.args[i]);
                        }

                        soundtrack = song.toString();
                    }

                    return new CountBySoundtrackName(soundtrack, Client.account);

                } else {
                    output.println(wrongArguments);
                }
                return null;
            }

            default: {
                try {
                    Command cmd = (Command) (m.constructor).newInstance(Client.account);

                    if (m.args.length != 0) {
                        output.println(wrongArguments);
                    } else {
                        return cmd;
                    }


                } catch (Exception e) {
                    Client.log.warning(e::toString);
                    output.println(wrongCommand);
                }
            }
        }
        return null;
    }


    /**
     * Перерабатывает название команды, введенной пользователем,
     * в название класса для создания соответствующей команды
     *
     * @param str Команда, введенная пользователем
     * @return - Название класса команды
     */
    private static String castToCommandClass(String str) {
        if (str.equals(""))
            return str;

        char[] chars = str.toLowerCase().toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);

        for (int i = 1; i < chars.length; ++i) {
            if (chars[i] == '_') {
                chars[i + 1] = Character.toUpperCase(chars[i + 1]);
            }
        }

        return new String(chars).replaceAll("_", "");
    }

    /**
     * Возвращает сложенное по частям сообщение, содержащее команду и ее аргументы.
     *
     * @param parts Части сообщения
     * @return Сложенное сообщение
     */
    private Message commandBuilder(String[] parts) {
        if (parts == null || parts.length == 0)
            return null;

        StringBuilder cmd = new StringBuilder();

        for (int i = 0; i < parts.length; ++i) {
            if (i > 0)
                cmd.append("_");
            cmd.append(parts[i].replaceAll(" ", "").toLowerCase());

            Constructor commandConstructor = serverCommands.get(cmd.toString());
            if (commandConstructor != null) {
                return new Message(commandConstructor, cmd.toString(),
                        Arrays.copyOfRange(parts, i + 1, parts.length));
            }

            commandConstructor = clientCommands.get(cmd.toString());
            if (commandConstructor != null) {
                return new Message(commandConstructor, cmd.toString(),
                        Arrays.copyOfRange(parts, i + 1, parts.length));
            }
        }
        return null;
    }

    /**
     * Объекты хранят название команды, аргументы и конструктор, по которому можно создать объект команды.
     */
    private static class Message {
        /**
         * Конструктор, по которому можно создать объект команды
         */
        public final Constructor constructor;

        /**
         * Название команды
         */
        public final String command;

        /**
         * Массив аргументов команды в срочном представлении
         */
        public final String[] args;

        /**
         * Создание объекта сообщения
         *
         * @param constructor Конструктор, по которому можно создать объект команды
         * @param command     Название команды
         * @param args        Массив аргументов команды в срочном представлении
         */
        private Message(Constructor constructor, String command, String[] args) {
            this.constructor = constructor;
            this.command = command;
            this.args = args;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Message)) return false;
            Message message = (Message) o;
            return Objects.equals(constructor, message.constructor) &&
                    Objects.equals(command, message.command) &&
                    Arrays.equals(args, message.args);
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(constructor, command);
            result = 31 * result + Arrays.hashCode(args);
            return result;
        }
    }

    /**
     * Метод для построчного ввода пользователем характеристких объекта
     * класса {@code HumanBeing}
     * <p>
     * Возвращает null, если пользователь отказался от ввода характеристик объекта
     *
     * @param name Введенное пользователем значение поля {@code name}
     *             класса {@code HumanBeing}
     * @return Полученный по результатам построчного ввода объект
     * класса {@code HumanBeing} или null в случае принудительного выхода пользователем
     * из режима ввода характеристик объекта
     */
    private HumanBeing offerToEnterHuman(String name) {
        if (isConsole)
            output.println("  Введите характеристики объекта "
                    + (name == null ? "" : "\"" + name + "\" ")
                    + "(команда \"break\" для отмены ввода)");

        wasBroken = false;

        if (name == null) {
            name = enterString("Name");
        }
        if (wasBroken)
            return null;

        Coordinates coords = enterCoordinates();
        if (wasBroken)
            return null;

        Boolean realHero = enterBoolean("Real hero");
        if (wasBroken)
            return null;

        Boolean hasToothPick = enterBoolean("Has toothpick");
        if (wasBroken)
            return null;

        Double impactSpeed = enterDouble("Impact speed");
        if (wasBroken)
            return null;

        String soundtrackName = enterString("Soundtrack name");
        if (wasBroken)
            return null;

        Integer minutesOfWaiting = enterInteger("Minutes of waiting");
        if (wasBroken)
            return null;

        Mood mood = enterMood();
        if (wasBroken)
            return null;

        Boolean hasCoolCar = enterBoolean("Has cool car");
        if (wasBroken)
            return null;

        Car car = new Car(hasCoolCar);

        return new HumanBeing(name,
                coords,
                realHero,
                hasToothPick,
                impactSpeed,
                soundtrackName,
                minutesOfWaiting,
                mood,
                car
        );
    }


    /**
     * Метод для ввода пользователем данных типа String
     * <p>
     * Если пользователь ввел команду "break",
     * то происходит принудительный выход из режима ввода характеристик объекта
     * путем изменения поля {@code wasBroken}
     *
     * @param field Название поля, для которого вводится значение
     * @return Введенное пользователем значение
     */
    private String enterString(String field) {
        if (isConsole)
            output.print("  " + field + " (String)\n> ");

        while (true) {
            String input = this.input.nextLine();

            if (input.equals("")) {
                output.print(wrongValue);
                continue;
            }

            if (input.equals("break"))
                wasBroken = true;

            return input;
        }
    }


    /**
     * Метод для ввода пользователем данных типа Integer
     * <p>
     * Если пользователь ввел команду "break",
     * то происходит принудительный выход из режима ввода характеристик объекта
     * путем изменения поля {@code wasBroken}
     *
     * @param field Название поля, для которого вводится значение
     * @return Введенное пользователем значение
     */
    private Integer enterInteger(String field) {
        if (isConsole)
            output.print("  " + field + " (int)\n> ");
        while (true) {
            String input = this.input.nextLine().replaceAll("\\s", "");

            if (input.equals("")) {
                output.print(wrongValue);
                continue;
            }

            if (input.equals("break")) {
                wasBroken = true;
                return null;
            }

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                output.print(wrongFormat);
            }
        }
    }


    /**
     * Метод для ввода пользователем данных типа Double
     * <p>
     * Если пользователь ввел команду "break",
     * то происходит принудительный выход из режима ввода характеристик объекта
     * путем изменения поля {@code wasBroken}
     *
     * @param field Название поля, для которого вводится значение
     * @return Введенное пользователем значение
     */
    private Double enterDouble(String field) {
        if (isConsole)
            output.print("  " + field + " (double)\n> ");
        while (true) {
            String input = this.input.nextLine().replaceAll("\\s", "");

            if (input.equals("")) {
                output.print(wrongValue);
                continue;
            }

            switch (input) {
                case "break":
                    wasBroken = true;
                    return 0.0D;

                default:
                    try {
                        return Double.parseDouble(input);
                    } catch (NumberFormatException e) {
                        output.print(wrongFormat);
                    }
            }
        }
    }


    /**
     * Метод для ввода пользователем данных для поля {@code coordinates}
     * объекта класса {@code HumanBeing}
     * <p>
     * Если пользователь ввел команду "break",
     * то происходит принудительный выход из режима ввода характеристик объекта
     * путем изменения поля {@code wasBroken}
     *
     * @return Инициализированный объект класса Coordinates
     */
    private Coordinates enterCoordinates() {
        if (isConsole)
            output.print("  Coordinates в формате \"(x, y)\":\n> ");
        while (true) {
            String input = this.input.nextLine().replaceAll("\\s", "");

            if (input.equals("")) {
                output.print(wrongValue);
                continue;
            }

            String[] coords = Pattern.compile("[(,)]").split(input);

            if (input.equals("break")) {
                wasBroken = true;
                return null;
            }

            try {
                return new Coordinates(Integer.parseInt(coords[1]), Double.parseDouble(coords[2]));
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                output.print(wrongFormat);
            }
        }
    }


    /**
     * Метод для ввода пользователем данных для поля {@code mood}
     * объекта класса {@code HumanBeing}
     * <p>
     * Если пользователь ввел команду "break",
     * то происходит принудительный выход из режима ввода характеристик объекта
     * путем изменения поля {@code wasBroken}
     *
     * @return Инициализированный объект класса Mood
     */
    private Mood enterMood() {
        if (isConsole)
            output.print("  Mood \"SADNESS/GLOOM/CALM/RAGE\":\n> ");
        while (true) {
            String input = this.input.nextLine().replaceAll(" ", "").toUpperCase();

            if (input.equals("")) {
                output.print(wrongValue);
                continue;
            }

            if (input.equals("BREAK")) {
                wasBroken = true;
                return null;
            }

            try {
                return Mood.valueOf(input);
            } catch (IllegalArgumentException e) {
                output.print(wrongValue);
            }
        }
    }


    /**
     * Метод для ввода пользователем данных типа Boolean
     * <p>
     * Если пользователь ввел команду "break",
     * то происходит принудительный выход из режима ввода характеристик объекта
     * путем изменения поля {@code wasBroken}
     *
     * @param field Название поля, для которого вводится значение
     * @return Введенное значение
     */
    private Boolean enterBoolean(String field) {
        if (isConsole)
            output.print("  " + field + " \"true/false\"\n> ");
        while (true) {
            String input = this.input.nextLine().replaceAll("\\s", "").toLowerCase();

            if (input.equals("")) {
                output.print(wrongValue);
                continue;
            }

            switch (input) {
                case "true":
                    return true;

                case "false":
                    return false;

                case "break":
                    wasBroken = true;
                    return false;
            }
            output.print(wrongFormat);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ConsoleParser))
            return false;
        ConsoleParser that = (ConsoleParser) o;
        return input.equals(that.input);
    }

    @Override
    public int hashCode() {
        return Objects.hash(input);
    }
}