package domain.validation;

import domain.Mesaj;
import exceptii.ValidationException;

public class MesajValidator implements Validator<Mesaj> {
    /**
     * Valideaza id-ul unui un user
     * @param id - id-ul de validat
     * @throws ValidationException - daca user-ul nu este valid
     */
    private void validateId(Long id) throws ValidationException {
        if (id == null) {
            throw new ValidationException("Id-ul nu poate fi null!");
        }
        if(id.compareTo(0L) < 0) {
            throw new ValidationException("Id-ul nu poate fi negativ!");
        }
    }

    @Override
    public void validate(Mesaj entity) throws ValidationException {
        if (entity == null)
            throw new ValidationException("Mesajul nu poate fi vida!");
        if (entity.getUserFrom() == null || entity.getUserTo() == null)
            throw new ValidationException("Mesajul trebuie sa aiba cel putin un user!");
        if (entity.getUserFrom().equals(entity.getUserTo()))
            throw new ValidationException("Nu pot trimite mesaj mie!");
        validateId(entity.getUserFrom());
        validateId(entity.getUserTo());
    }
}
