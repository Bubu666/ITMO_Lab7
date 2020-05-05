package network.human;

/**
 * Исключение выбрасывается в случае
 * неправильного ввода пользователем имени объекта
 */
public class InappropriateHumanNameException extends RuntimeException {
    public InappropriateHumanNameException(String message) {
        super(message);
    }
}