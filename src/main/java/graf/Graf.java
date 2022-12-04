package graf;

import exceptii.DuplicatedElementException;
import exceptii.NotExistentException;
import utils.Pereche;

import java.util.Collection;

public interface Graf<Nod, Muchie extends Pereche<Nod, Nod>> {
    /**
     * Adauga un nod in graf
     * @param nod - nodul de adaugat
     * @throws DuplicatedElementException - daca nodul exista deja in graf
     */
    void addNod(Nod nod) throws DuplicatedElementException;
    /**
     * Adauga o muchie in graf
     * @param muchie - muchia de adaugat
     * @throws DuplicatedElementException - daca muchia exista deja in graf
     */
    void addMuchie(Muchie muchie) throws DuplicatedElementException;
    /**
     * Sterge un nod din graf
     * @param nod - nodul de sters
     * @throws NotExistentException - daca nodul nu exista in graf
     */
    void removeNod(Nod nod) throws NotExistentException;
    /**
     * Sterge o muchie din graf
     * @param muchie - muchia de sters
     * @throws NotExistentException - daca muchia nu exista in graf
     */
    void removeMuchie(Muchie muchie) throws NotExistentException;
    /**
     * updateaza un nod din graf
     * @param nod - nodul vechi
     * @param newNod - nodul nou
     */
    void updateNod(Nod nod, Nod newNod) throws NotExistentException;
    /**
     * Verifica daca o muchie exista in graf
     * @param muchie - muchia de verificat
     * @param newMuchie - muchia noua
     */
    void updateMuchie(Muchie muchie, Muchie newMuchie) throws NotExistentException;

    /**
     * determina toate nodurile din graf
     * @return o colectie de noduri reprezentand nodurile grafului
     */
    Collection<Nod> getNoduri();
}