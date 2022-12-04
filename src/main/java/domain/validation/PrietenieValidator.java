package domain.validation;

import domain.Prietenie;
import exceptii.ValidationException;

public class PrietenieValidator implements Validator<Prietenie> {

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

    /**
     * Valideaza o prietenie
     * @param entity - prietenia de validat
     * @throws ValidationException - daca prietenia nu este valida
     */
    @Override
    public void validate(Prietenie entity) throws ValidationException {
        if (entity == null)
            throw new ValidationException("Prietenia nu poate fi vida!");
        if (entity.getFirst() == null || entity.getSecond() == null)
            throw new ValidationException("Prietenia trebuie sa aiba cel putin un user!");
        if (entity.getFirst().equals(entity.getSecond()))
            throw new ValidationException("Un user nu poate fi prieten cu el insusi!");
        validateId(entity.getFirst());
        validateId(entity.getSecond());
    }
}
