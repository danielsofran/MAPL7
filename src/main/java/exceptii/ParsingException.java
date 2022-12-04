package exceptii;

public class ParsingException extends ValidationException {
    /**
     * Constructor
     * @param message - mesajul de eroare
     */
    public ParsingException(String message){
        super(message);
    }
}
