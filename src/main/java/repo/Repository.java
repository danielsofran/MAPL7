package repo;

import domain.Entity;
import exceptii.DuplicatedElementException;

import java.util.Collection;
import java.util.function.Predicate;

public interface Repository <ID, E extends Entity<ID>> {
    /**
     * gaseste entitatea cu id-ul dat
     * @param id - id-ul entitatii
     * @return entitatea gasita sau null daca nu exista
     * @throws IllegalArgumentException - daca id-ul este null
     */
    E findOne(ID id) throws IllegalArgumentException;

    /**
     * determina entitatea care respecta conditia data
     * @param predicate - predicatul dupa care se filtreaza
     * @return - entitatea care indeplineste predicatul
     * @throws IllegalArgumentException - daca predicatul este null
     */
    E findOne(Predicate<E> predicate) throws IllegalArgumentException;

    /**
     * @return toate entitatile
     */
    Collection<E> findAll();

    /**
     * salveaza entitatea
     * @param entity - entitatea de salvat
     * @return entitatea salvata sau null daca nu exista
     * @throws IllegalArgumentException - daca entitatea este null
     * @throws DuplicatedElementException - daca entitatea exista deja
     */
    E save(E entity) throws IllegalArgumentException, DuplicatedElementException;

    /**
     * sterge entitatea cu id-ul dat
     * @param id - id-ul entitatii de sters
     * @return entitatea stearsa sau null daca nu exista
     * @throws IllegalArgumentException - daca id-ul este null
     */
    E delete(ID id) throws IllegalArgumentException;

    /**
     * sterge entitatea care indeplineste conditia data
     * @param predicate - predicatul dupa care se filtreaza
     * @return entitatea stearsa sau null daca nu exista
     * @throws IllegalArgumentException - daca predicatul este null
     */
    E delete(Predicate<E> predicate) throws IllegalArgumentException;

    /**
     * actualizeaza entitatea cu id-ul dat
     * @param id - id-ul entitatii de actualizat
     * @param entity - entitatea cu noile date
     * @return entitatea actualizata sau null daca nu exista
     * @throws IllegalArgumentException - daca id-ul sau entitatea sunt null
     */
    E update(ID id, E entity) throws IllegalArgumentException;

    /**
     * sterge toate entitatile
     */
    void clear();
}
