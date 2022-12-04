package repo;

import domain.Entity;
import domain.validation.Validator;

public class AbstractRepoDB<ID, E extends Entity<ID>> {
    protected final Validator<E> validator;
    protected final String url;
    protected final String username;
    protected final String password;

    /**
     * Constructorul clasei UserRepoDB
     * @param validator - validatorul
     * @param url - url-ul bazei de date
     * @param username - username-ul bazei de date
     * @param password - parola userului bazei de date
     */
    public AbstractRepoDB(Validator<E> validator, String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }
}
