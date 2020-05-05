package network.human;

import java.io.Serializable;
import java.util.Objects;

/**
 * Местоположение объектов
 */
public class Coordinates implements Serializable {

    /**
     * Абсцисса объекта
     */
    private Integer x;

    /**
     * Ордината объекта
     */
    private Double y;

    /**
     * Принимает значения полей {@code x} и {@code y}X
     *
     * @param x Абсцисса
     * @param y Ордината
     */
    public Coordinates(Integer x, Double y) {
        if (x == null || y == null) throw new NullPointerException();
        this.x = x;
        this.y = y;
    }

    /**
     * Возвращает целочисленное значение абсциссы объекта
     *
     * @return Абсцисса
     */
    public Integer getX() {
        return x;
    }

    /**
     * Возвращает вещественное значение ординаты объекта
     *
     * @return Ордината
     */
    public Double getY() {
        return y;
    }

    /**
     * Устанавливает новое значение абсциссы объекта
     *
     * @param x Новое значение Абсциссы
     */
    public void setX(Integer x) {
        if (x == null) throw new NullPointerException();
        this.x = x;
    }

    /**
     * Устанавливает новое значение ординаты объекта
     *
     * @param y Новое значение ординаты
     */
    public void setY(Double y) {
        if (y == null) throw new NullPointerException();
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Coordinates))
            return false;
        Coordinates that = (Coordinates) o;
        return x.equals(that.x) && y.equals(that.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "{" + "\n\t\t\"x\": " + x + ",\n\t\t\"y\": " + y + "\n\t}";
    }
}
