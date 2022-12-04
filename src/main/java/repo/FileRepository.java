package repo;

import domain.Entity;
import domain.validation.Validator;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Predicate;

public class FileRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID, E>{
    protected String path;

    /**
     * Constructorul clasei FileRepository
     * @param validator - validatorul
     * @param path - calea catre fisier
     */
    public FileRepository(Validator<E> validator, String path) {
        super(validator);
        this.path = path;
        loadFromFile();
    }

    /**
     * citeste entitatile din fisier
     */
    protected void loadFromFile() {
        try(ObjectInputStream in = new ObjectInputStream(Files.newInputStream(Paths.get(path)))) {
            E entity = (E) in.readObject();
            while(entity != null) {
                super.save(entity);
                entity = (E) in.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            //e.printStackTrace();
        }
    }

    /**
     * salveaza entitatile in fisier
     */
    protected void writeToFile() {
        try(ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(Paths.get(path)))) {
            for(E entity : super.findAll()) {
                out.writeObject(entity);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * salveaza entitatea in fisier
      * @param entity - entitatea de salvat
     * @return entitatea salvata sau null daca nu a fost salvata
     */
    @Override
    public E save(E entity) {
        E savedEntity = super.save(entity);
        writeToFile();
        return savedEntity;
    }

    /**
     * sterge entitatea cu id-ul dat
     * @param id - id-ul entitatii
     * @return entitatea stearsa sau null daca nu exista
     */
    @Override
    public E delete(ID id) {
        E deletedEntity = super.delete(id);
        writeToFile();
        return deletedEntity;
    }

    /**
     * sterge entitatea care respecta conditia data
     * @param predicate - predicatul dupa care se filtreaza
     * @return entitatea stearsa sau null daca nu exista
     */
    @Override
    public E delete(Predicate<E> predicate) {
        E deletedEntity = super.delete(predicate);
        writeToFile();
        return deletedEntity;
    }

    /**
     * actualizeaza entitatea cu id-ul dat
     * @param id - id-ul entitatii
     * @param entity - entitatea cu care se actualizeaza
     */
    @Override
    public E update(ID id, E entity) {
        E updatedEntity = super.update(id, entity);
        writeToFile();
        return updatedEntity;
    }

    /**
     * sterge toate entitatile din fisier
     */
    @Override
    public void clear() {
        super.clear();
        writeToFile();
    }
}
