package network.human;

import java.io.Serializable;
import java.util.Objects;

/**
 * Объектная модель машины
 */
public class Car implements Serializable {

    /**
     * Крутость машины
     */
    private boolean cool;

    public Car(boolean cool) {
        this.cool = cool;
    }

    /**
     * Возвращает характеристику крутости автомобиля
     * @return {@code true} если машина крутая, иначе - {@code false}
     */
    public boolean isCool() {
        return cool;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Car))
            return false;
        Car car = (Car) o;
        return cool == car.cool;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cool);
    }

    @Override
    public String toString() {
        return "{" + "\n\t\t\"cool\": " + cool + "\n\t}";
    }
}
