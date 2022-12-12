package domain.validation;

import domain.User;
import exceptii.ValidationException;

public class UserValidator implements Validator<User> {
    private final Long zero = 0L;

    /**
     * Valideaza id-ul unui un user
     * @param user - user-ul de validat
     * @throws ValidationException - daca user-ul nu este valid
     */
    private void validateId(User user) throws ValidationException {
        if (user.getId() == null) {
            throw new ValidationException("Id-ul nu poate fi null!");
        }
        if(user.getId().compareTo(zero) < 0) {
            throw new ValidationException("Id-ul nu poate fi negativ!");
        }
    }

    /**
     * Valideaza numele unui user
     * @param user - user-ul de validat
     * @throws ValidationException - daca user-ul nu este valid
     */
    private void validateName(User user) throws ValidationException {
        if (user.getName() == null) {
            throw new ValidationException("Numele nu poate fi null!");
        }
        if (user.getName().equals("")) {
            throw new ValidationException("Numele nu poate fi vid!");
        }
        if(!user.getName().matches("^[a-zA-Z\\s]*$")) {
            throw new ValidationException("Numele nu poate contine decat litere si spatii!");
        }
    }

    /**
     * Valideaza emailul unui user
     * @param user - user-ul de validat
     * @throws ValidationException - daca user-ul nu este valid
     */
    private void validateEmail(User user) throws ValidationException {
        if (user.getEmail() == null) {
            throw new ValidationException("Email-ul nu poate fi null!");
        }
        if (user.getEmail().equals("")) {
            throw new ValidationException("Email-ul nu poate fi vid!");
        }
    }

    /**
     * Valideaza parola unui user
     * @param user - user-ul de validat
     * @throws ValidationException - daca user-ul nu este valid
     */
    private void validatePassword(User user) throws ValidationException {
        if (user.getPassword() == null) {
            throw new ValidationException("Parola nu poate fi null!");
        }
        if (user.getPassword().equals("")) {
            throw new ValidationException("Parola nu poate fi vida!");
        }
    }

    /**
     * Valideaza un user
     * @param entity - user-ul de validat
     * @throws ValidationException - daca user-ul nu este valid
     */
    @Override
    public void validate(User entity) throws ValidationException {
        if(entity == null){
            throw new ValidationException("Entitatea nu poate fi null!");
        }
        validateId(entity);
        validateName(entity);
        validateEmail(entity);
        validatePassword(entity);
    }
}
