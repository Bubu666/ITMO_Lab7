package network.human;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;


/**
 * Объектная модель человеческого существа.
 * Класс содержит поля состояния человека, а также хранилище имен и саундтреков
 * для реализации случайной генерации объектов данного класса.
 */
public class HumanBeing implements Comparable<HumanBeing>, Serializable {

    @Stored
    private String owner;

    /**
     * Уникальный id каждого объекта типа {@code HumanBeing}
     * <p>
     * Поле генерируется автоматически при инициализации объекта
     */
    @Stored
    private int id;


    /**
     * Время создания конкретного объекта.
     * <p>
     * Поле генерируется автоматически при инициализации объекта
     */
    @Stored
    private java.util.Date creationDate;

    /**
     * Имя
     */
    @Stored
    private String name;

    /**
     * Местоположение
     */
    @Stored
    private Coordinates coordinates;

    /**
     * Характеристика характера
     */
    @Stored
    private Boolean realHero;

    /**
     * Наличие зубочистки
     */
    @Stored
    private boolean hasToothpick;

    /**
     * Скорость соударения
     */
    @Stored
    private double impactSpeed;

    /**
     * Название саундтрека
     */
    @Stored
    private String soundtrackName;

    /**
     * Время ожидания в минутах
     */
    @Stored
    private Integer minutesOfWaiting;

    /**
     * Настроение
     */
    @Stored
    private Mood mood;

    /**
     * Автомобиль
     */
    @Stored
    private Car car;

    /**
     * Количество созданных объектов.
     * переменная нужна для генерации {@code id} объектов
     */
    private static int numOfHumans;

    /**
     * Объект хранилища имен и саундтреков для инициализации случайно сгенерированных объектов
     */
    private static HumanStorage humanStorage;

    {
        creationDate = new Date();
        id = ++numOfHumans;
    }

    static {
        humanStorage = new HumanStorage();
    }

    /**
     * Общий конструктор
     *
     * @param name             Имя
     * @param coordinates      Местоположение
     * @param realHero         Является ли героем
     * @param hasToothpick     Наличие зубочистки
     * @param impactSpeed      Скорость соударения
     * @param soundtrackName   Название саундтрека
     * @param minutesOfWaiting Количество минут ожидания
     * @param mood             Настроение
     * @param car              Автомобиль
     */
    public HumanBeing(String name,
                      Coordinates coordinates,
                      Boolean realHero,
                      boolean hasToothpick,
                      double impactSpeed,
                      String soundtrackName,
                      Integer minutesOfWaiting,
                      Mood mood,
                      Car car
    ) {
        if (name == null || coordinates == null || realHero == null || soundtrackName == null || minutesOfWaiting == null || mood == null || car == null)
            throw new NullPointerException();

        if (name.equals(""))
            throw new InappropriateHumanNameException("Недопустимое имя человека");

        this.name = name;
        this.coordinates = coordinates;
        this.realHero = realHero;
        this.hasToothpick = hasToothpick;
        this.impactSpeed = impactSpeed;
        this.soundtrackName = soundtrackName;
        this.minutesOfWaiting = minutesOfWaiting;
        this.mood = mood;
        this.car = car;
    }

    /**
     * Конструктор для создания объектов из файла.
     * Дополнительно принимается время создания объекта.
     *
     * @param name             Имя
     * @param coordinates      Местоположение
     * @param realHero         Является ли героем
     * @param hasToothpick     Наличие зубочистки
     * @param impactSpeed      Скорость соударения
     * @param soundtrackName   Название саундтрека
     * @param minutesOfWaiting Количество минут ожидания
     * @param mood             Настроение
     * @param car              Автомобиль
     * @param creationDate     Время создания объекта
     */
    public HumanBeing(int id,
                      String name,
                      Coordinates coordinates,
                      Boolean realHero,
                      boolean hasToothpick,
                      double impactSpeed,
                      String soundtrackName,
                      Integer minutesOfWaiting,
                      Mood mood,
                      Car car,
                      Instant creationDate
    ) {
        this(name, coordinates, realHero, hasToothpick, impactSpeed, soundtrackName, minutesOfWaiting, mood, car);
        this.id = id;
        if (creationDate != null)
            this.creationDate = Date.from(creationDate);
    }

    public void init() {
        creationDate = new Date();
        id = Math.abs(hashCode());
    }

    public HumanBeing init(String owner, int humanId) {
        creationDate = new Date();
        id = humanId;
        this.owner = owner;
        return this;
    }

    private HumanBeing() {}

    public static HumanBeing rebuild(String owner, int id, String name,
                                     int x, double y, boolean realHero,
                                     boolean hasToothpick, int impactSpeed,
                                     String soundtrackName, int minutesOfWaiting,
                                     String mood, boolean coolCar, Timestamp creationDate) {
        HumanBeing human = new HumanBeing();
        human.owner = owner;
        human.id = id;
        human.name = name;
        human.coordinates = new Coordinates(x, y);
        human.realHero = realHero;
        human.hasToothpick = hasToothpick;
        human.impactSpeed = impactSpeed;
        human.soundtrackName = soundtrackName;
        human.minutesOfWaiting = minutesOfWaiting;
        human.mood = Mood.of(mood);
        human.car = new Car(coolCar);
        human.creationDate = creationDate;
        return human;
    }

    /**
     * Сравнивает объекты между собой
     *
     * @param o Другой объект
     * @return Результат сравнения
     */
    public int compareTo(HumanBeing o) {
        return this.minutesOfWaiting - o.minutesOfWaiting;
    }

    public String getOwner() {
        return owner;
    }

    /**
     * Возвращает имя объекта
     *
     * @return Имя
     */
    public String getName() {
        return name;
    }

    /**
     * Возвращает метсоположение объекта
     *
     * @return Местоположение
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * Устанавливает новые координаты объекта
     *
     * @param coordinates Новые координаты
     */
    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * Возвращает дату создания объекта
     *
     * @return Дата создания
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * Возвращает название саундтрека
     *
     * @return Название саундтрека
     */
    public String getSoundtrackName() {
        return soundtrackName;
    }

    /**
     * Возвращает id объекта
     *
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Статический метод для генерации случайного саундтрека
     *
     * @return Саундтрек
     */
    public static String getRandomSoundtrack() {
        return humanStorage.getSoundtrack();
    }

    /**
     * Статический метод для генерации случайного объекта человеческого существа
     *
     * @return Человеческое существо
     */
    public static HumanBeing getHuman() {
        Random r = new Random();

        return new HumanBeing(humanStorage.getName(),
                new Coordinates(r.nextInt(200), r.nextInt(200) * Math.random()),
                r.nextBoolean(),
                r.nextBoolean(),
                r.nextInt(150) * Math.random(),
                humanStorage.getSoundtrack(),
                r.nextInt(100),
                Mood.getRandom(),
                new Car(Math.random() > 0.5)
        );
    }

    /**
     * Возвращает наличие характеристики характера человеческого существа
     *
     * @return {@code true} если объект - "реальный герой", иначе - {@code false}
     */
    public Boolean isRealHero() {
        return realHero;
    }

    /**
     * Возвращает наличие у объекта зубочистки
     *
     * @return {@code true} если есть зубочистка, иначе - {@code false}
     */
    public boolean hasToothpick() {
        return hasToothpick;
    }

    /**
     * Возвращает скорость соударения
     *
     * @return Скорость соударения
     */
    public double getImpactSpeed() {
        return impactSpeed;
    }

    /**
     * Возвращает время ожидания объекта
     *
     * @return Количество минут ожидания
     */
    public Integer getMinutesOfWaiting() {
        return minutesOfWaiting;
    }

    /**
     * Возвращает настроение объекта
     *
     * @return Настроение
     */
    public Mood getMood() {
        return mood;
    }

    /**
     * Возвращает автомобиль объекта
     *
     * @return Автомобиль
     */
    public Car getCar() {
        return car;
    }

    /**
     * Вложенный статический класс для генерации случайных значений полей {@code name} и {@code soundtrackName}
     */
    private static class HumanStorage {

        /**
         * Список доступных имен
         */
        private static ArrayList<String> nameList = new ArrayList<>();

        /**
         * Список доступных саундтреков
         */
        private static ArrayList<String> soundtrackList = new ArrayList<>();

        static {
            try {
                InputStream names = HumanBeing.class.getResourceAsStream("/names.txt");
                InputStream soundtracks = HumanBeing.class.getResourceAsStream("/soundtracks.txt");

                BufferedReader reader = new BufferedReader(new InputStreamReader(names));
                nameList = (ArrayList<String>) reader.lines().collect(Collectors.toList());

                reader = new BufferedReader(new InputStreamReader(soundtracks));
                soundtrackList = (ArrayList<String>) reader.lines().collect(Collectors.toList());

                names.close();
                soundtracks.close();

            } catch (Exception e) {
                System.out.println("  " + e.getMessage());
                System.out.println("  Рандомная инициализация объектов может не работать");
            }
        }

        /**
         * Возвращает случайно сгенерированное название саундтрека
         *
         * @return Название саундтрека
         */
        String getSoundtrack() {
            return soundtrackList.size() > 0
                    ? soundtrackList.get(new Random().nextInt(soundtrackList.size()))
                    : "random soundtrack";
        }

        /**
         * Возвращает случайно сгенерированное имя объекта
         *
         * @return Имя объекта
         */
        String getName() {
            return nameList.size() > 0
                    ? nameList.get(new Random().nextInt(nameList.size()))
                    : "Random Name";
        }
    }


    /**
     * Возвращает строковое представление объектов класса {@code HumanBeing} в формате CSV для сохранения в файл
     *
     * @return Поля объекта в формате CSV
     */
    public String toCSVFormatString() {
        return String.format("%d;%s;%d %f;%b;%b;%f;%s;%d;%s;%b;%s",
                id,
                name,
                coordinates.getX(),
                coordinates.getY(),
                realHero,
                hasToothpick,
                impactSpeed,
                soundtrackName,
                minutesOfWaiting,
                mood,
                car.isCool(),
                creationDate.toInstant()
        ).replaceAll(",", ".");
    }

    public String dbValues() {
        return String.format("values ('%s'; %d; '%s'; %d; %f; %b; %b; %f; '%s'; %d; '%s'; %b; '%s')",
                owner,
                id,
                name,
                coordinates.getX(),
                coordinates.getY(),
                realHero,
                hasToothpick,
                impactSpeed,
                soundtrackName,
                minutesOfWaiting,
                mood,
                car.isCool(),
                new Timestamp(creationDate.getTime())
        ).replaceAll(",", ".").replaceAll(";", ",");
    }

    public String dbProperties() {
        return String.format("(%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)",
                "owner",
                "id",
                "name",
                "x",
                "y",
                "real_hero",
                "has_toothpick",
                "impact_speed",
                "soundtrack_name",
                "minutes_of_waiting",
                "mood",
                "cool_car",
                "creation_date"
        );
    }

    /**
     * Возвращает строковое представление названий полей класса {@code HumanBeing} в формате CSV для сохранения в файл
     *
     * @return Названия полей объекта в формате CSV
     */
    public static String fieldHeadersCSVString() {
        return String.format("%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;",
                "id",
                "name",
                "coordinates",
                "realHero",
                "hasToothpick",
                "impactSpeed",
                "soundtrackName",
                "minutesOfWaiting",
                "mood",
                "car",
                "creationDate"
        );
    }

    @Override
    public String toString() {
        return "{\n" + "\towner: " + owner + "\n\t\"id\": " + id + ",\n\t\"name\": \"" + name + '\"' + ",\n\t\"coordinates\": " + coordinates + ",\n\t\"creationDate\": " + creationDate + ",\n\t\"realHero: " + realHero + ",\n\t\"hasToothpick: " + hasToothpick + ",\n\t\"impactSpeed\": " + impactSpeed + ",\n\t\"soundtrackName\": \"" + soundtrackName + "\",\n\t\"minutesOfWaiting\": " + minutesOfWaiting + ",\n\t\"mood: " + mood + ",\n\t\"car\": " + car + "\n}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof HumanBeing))
            return false;
        HumanBeing that = (HumanBeing) o;
        return name.equals(that.name) && coordinates.equals(that.coordinates) && soundtrackName
                .equals(that.soundtrackName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(owner,
                id,
                name,
                coordinates,
                creationDate,
                realHero,
                hasToothpick,
                impactSpeed,
                soundtrackName,
                minutesOfWaiting,
                mood,
                car
        );
    }
}