package exceptii;

public class MyException extends RuntimeException {
    /**
     * Constructor
     * @param message - mesajul de eroare
     */
    public MyException(String message) {
        super(message);
    }
}
