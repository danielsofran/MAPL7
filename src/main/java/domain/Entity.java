package domain;

import java.io.Serializable;
import java.util.Objects;

public class Entity<ID> implements Serializable {
    private static final long serialVersionUID = 37846873547835478L;

    private ID id = null;

    /**
     * constructor default
     * set id to null
     */
    public Entity() {}

    /**
     * constructor with parameter
     * @param id id of current entity
     */
    public Entity(ID id) {
        this.id = id;
    }

    /**
     * gets the entity's id
     * @return the current id
     */
    public ID getId() {
        return id;
    }

    /**
     * sets the current id to the given id
     * @param id the given id
     */
    public void setId(ID id) {
        this.id = id;
    }

    /**
     * check if current entity has the same value as the given object, that can represent only an entity
     * @param o the given object
     * @return true if the instances are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity that = (Entity) o;
        return id.equals(that.id);
    }

    /**
     * entity cannot be cloned
     * @throws CloneNotSupportedException - nici o entitate nu poate fi clonata
     */
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Two users can't have the same id!");
    }

    /**
     * computes the hash code asociated with the current entity
     * @return the hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
