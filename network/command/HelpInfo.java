package network.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Хранит информацию о команде для вывода в консоль
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface HelpInfo {

    /**
     * Возвращает хранимую информацию о команде
     * @return Информация о команде
     */
    String info();
}
