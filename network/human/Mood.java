package network.human;

import java.io.Serializable;

/**
 * Возможные значения настроения объекта
 */
public enum Mood implements Serializable {
    SADNESS {
        @Override
        public String toString() {
            return "sadness";
        }
    },
    GLOOM {
        @Override
        public String toString() {
            return "gloom";
        }
    },
    CALM {
        @Override
        public String toString() {
            return "calm";
        }
    },
    RAGE {
        @Override
        public String toString() {
            return "rage";
        }
    };

    /**
     * Статический метод возвращает настроение по его названию
     * @param name Название настроения
     * @return Объект настроения
     */
    public static Mood of(String name) {
        switch (name.toLowerCase()) {
            case "sadness":
                return Mood.SADNESS;
            case "gloom":
                return Mood.GLOOM;
            case "calm":
                return Mood.CALM;
            default:
                return Mood.RAGE;
        }
    }

    /**
     * Возвращает случайное настроение
     * @return Случайное настроение
     */
    public static Mood getRandom() {
        double random = Math.random();
        if (random < 0.25D)
            return Mood.SADNESS;
        if (random < 0.5D)
            return Mood.GLOOM;
        if (random < 0.75D)
            return Mood.CALM;
        else
            return Mood.RAGE;
    }
}