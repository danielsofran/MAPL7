package repo;

import domain.Entity;
import domain.validation.Validator;
import exceptii.DuplicatedElementException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class InMemoryRepository <ID, E extends Entity<ID>> implements Repository<ID, E> {
    protected Map<ID, E> entities;
    protected Validator<E> validator;

    /**
     * Constructorul clasei InMemoryRepository
     * @param validator - validatorul
     */
    public InMemoryRepository(Validator<E> validator){
        this.validator = validator;
        this.entities = new HashMap<>();
    }

    /**
     * gaseste entitatea cu id-ul dat
     * @return entitatea cu id-ul dat
     * @param id - id-ul entitatii
     * @throws IllegalArgumentException - daca id-ul este null
     */
    @Override
    public E findOne(ID id) {
        if(id == null)
            throw new IllegalArgumentException("id must be not null");
        return entities.get(id);
    }

    /**
     * determina entitatea care respecta conditia data
     * @param predicate - predicatul dupa care se filtreaza
     * @return - entitatea care indeplineste predicatul sau null daca nu exista
     */
    @Override
    public E findOne(Predicate<E> predicate) {
        if(predicate == null)
            throw new IllegalArgumentException("predicate must be not null");
        return entities.values().stream().filter(predicate).findFirst().orElse(null);
    }

    /**
     * @return toate entitatile
     */
    @Override
    public Collection<E> findAll() {
        return entities.values();
    }

    /**
     * salveaza entitatea
     * @param entity - entitatea de salvat
     * @return entitatea salvata sau null daca nu exista
     * @throws IllegalArgumentException - daca entitatea este null
     * @throws DuplicatedElementException - daca entitatea exista deja
     */
    @Override
    public E save(E entity) {
        if(entity == null)
            throw new IllegalArgumentException("entity must be not null");
        E old = entities.get(entity.getId());
        if(old != null)
            throw new DuplicatedElementException("entity already exists");
        validator.validate(entity);
        entities.put(entity.getId(), entity);
        return entity;
    }

    /**
     * sterge entitatea cu id-ul dat
     * @param id - id-ul entitatii de sters
     * @return entitatea stearsa sau null daca nu exista
     * @throws IllegalArgumentException - daca id-ul este null
     */
    @Override
    public E delete(ID id) {
        if(id == null)
            throw new IllegalArgumentException("id must be not null");
        return entities.remove(id);
    }

    /**
     * sterge entitatea care indeplineste conditia data
     * @param predicate - predicatul dupa care se filtreaza
     * @return entitatea stearsa sau null daca nu exista
     * @throws IllegalArgumentException - daca predicatul este null
     */
    @Override
    public E delete(Predicate<E> predicate) {
        if(predicate == null)
            throw new IllegalArgumentException("predicate must be not null");
        E entity = entities.values().stream().filter(predicate).findFirst().orElse(null);
        if(entity != null)
            entities.remove(entity.getId());
        return entity;
    }

    /**
     * actualizeaza entitatea cu id-ul dat
     * @param id - id-ul entitatii de actualizat
     * @param entity - entitatea cu noile date
     * @return entitatea inainte de actualizare sau null daca nu exista
     * @throws IllegalArgumentException - daca id-ul sau entitatea sunt null
     */
    @Override
    public E update(ID id, E entity) {
        if(id == null)
            throw new IllegalArgumentException("id must be not null");
        if(entity == null)
            throw new IllegalArgumentException("entity must be not null");
        validator.validate(entity);
        E old = entities.get(id);
        if(old != null)
            entities.replace(id, entity);
        return old;
    }

    /**
     * sterge toate entitatile
     */
    @Override
    public void clear() {
        entities.clear();
    }
}
