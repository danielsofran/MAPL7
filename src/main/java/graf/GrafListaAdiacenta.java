package graf;

import exceptii.DuplicatedElementException;
import exceptii.NotExistentException;
import utils.Pereche;

import java.util.*;

public class GrafListaAdiacenta<Nod, Muchie extends Pereche<Nod, Nod>> implements Graf<Nod, Muchie>{
    private final Map<Nod, Set<Muchie>> G;
    private final Map<Nod, Boolean> deleted;

    /**
     * Constructorul clasei
     */
    public GrafListaAdiacenta(){
        G = new HashMap<>();
        deleted = new HashMap<>();
    }

    /**
     * verifica daca un nod exista in graf
     * @param nod - nodul de verificat
     * @return true - daca nodul exista in graf, false - altfel
     */
    public Boolean hasNod(Nod nod){
        return G.containsKey(nod) && (!deleted.containsKey(nod) || !deleted.get(nod));
    }

    /**
     * adauga un nod in graf
     * @param nod - nodul de adaugat
     * @throws DuplicatedElementException - daca nodul exista deja in graf
     */
    @Override
    public void addNod(Nod nod) throws DuplicatedElementException {
        if(!G.containsKey(nod)){
            G.put(nod, new HashSet<>());
            deleted.put(nod, false);
        }
        else if(deleted.containsKey(nod) && deleted.get(nod))
            deleted.put(nod, false);
        //else throw new DuplicatedElementException("Nod duplicat!");
    }

    /**
     * adauga o muchie in graf
     * @param muchie - muchia de adaugat
     * @throws DuplicatedElementException - daca muchia exista deja in graf
     */
    @Override
    public void addMuchie(Muchie muchie) throws DuplicatedElementException {
        Nod nod1 = muchie.getFirst();
        Nod nod2 = muchie.getSecond();
        if(!hasNod(nod1)) addNod(nod1);
        if(!hasNod(nod2)) addNod(nod2);
        G.get(nod1).add(muchie);
        G.get(nod2).add(muchie);
    }

    /**
     * sterge un nod din graf
     * @param nod - nodul de sters
     * @throws NotExistentException - daca nodul nu exista in graf
     */
    @Override
    public void removeNod(Nod nod) throws NotExistentException {
        if(hasNod(nod))
        {
            G.remove(nod);
            deleted.put(nod, true);
        }
        else throw new NotExistentException("Nod inexistent!");
    }

    /**
     * sterge o muchie din graf
     * @param muchie - muchia de sters
     * @throws NotExistentException - daca muchia nu exista in graf
     */
    @Override
    public void removeMuchie(Muchie muchie) throws NotExistentException {
        Nod nod1 = muchie.getFirst();
        Nod nod2 = muchie.getSecond();
        if(!hasNod(nod1) || !hasNod(nod2))
            throw new NotExistentException("Muchia "+muchie+" este inexistenta!");
        G.get(nod1).removeIf(m -> m.equals(muchie));
        G.get(nod2).removeIf(m -> m.equals(muchie));
    }

    /**
     * updateaza un nod din graf
     * @param nod - nodul care trebuie updatat
     * @param newNod - nodul cu care se face update
     * @throws NotExistentException - daca nodul nu exista in graf
     */
    @Override
    public void updateNod(Nod nod, Nod newNod) throws NotExistentException {
        Set<Muchie> muchii = G.get(nod);
        if(muchii == null)
            throw new NotExistentException("Nodul "+nod+" este inexistent!");
        G.remove(nod);
        muchii.forEach(muchie -> {
            if(muchie.getFirst().equals(nod))
                muchie.setFirst(newNod);
            else muchie.setSecond(newNod);
        });
        G.put(newNod, muchii);
    }

    /**
     * updateaza o muchie din graf
     * @param muchie - muchia de updatat
     * @param newMuchie - muchia cu care se face update
     * @throws NotExistentException - daca muchia nu exista in graf
     */
    @Override
    public void updateMuchie(Muchie muchie, Muchie newMuchie) throws NotExistentException {
        Nod nod1 = muchie.getFirst();
        Nod nod2 = muchie.getSecond();
        if(!hasNod(nod1) || !hasNod(nod2))
            throw new NotExistentException("Muchia "+muchie+" este inexistenta!");
        if(!hasNod(newMuchie.getFirst())) addNod(newMuchie.getFirst());
        if(!hasNod(newMuchie.getSecond())) addNod(newMuchie.getSecond());
        G.get(nod1).removeIf(m -> m.equals(muchie));
        G.get(nod2).removeIf(m -> m.equals(muchie));
        G.get(newMuchie.getFirst()).add(newMuchie);
        G.get(newMuchie.getSecond()).add(newMuchie);
    }

    /**
     * determina vecinii unui nod din graf
     * @param nod - nodul pentru care se determina vecinii
     * @return - setul de vecini
     */
    public Set<Nod> getVecini(Nod nod){
        Set<Nod> vecini = new HashSet<>();
        G.get(nod).forEach(muchie -> {
            Nod nod1;
            if(muchie.getFirst().equals(nod))
                nod1 = muchie.getSecond();
            else nod1 = muchie.getFirst();
            if(hasNod(nod1))
                vecini.add(nod1);
        });
        return vecini;
    }

    /**
     * determina muchiile incidente unui nod din graf
     * @param nod - nodul pentru care se determina muchiile incidente
     * @return - setul de muchii incidente
     */
    public Set<Muchie> getMuchiiAdiacente(Nod nod){
        Set<Muchie> rez = new HashSet<>();
        G.get(nod).forEach(muchie -> {
            Nod nod1;
            if(muchie.getFirst().equals(nod))
                nod1 = muchie.getSecond();
            else nod1 = muchie.getFirst();
            if(hasNod(nod1))
                rez.add(muchie);
        });
        return rez;
    }

    /**
     * determina toate nodurile din graf
     * @return - setul de noduri
     */
    @Override
    public Set<Nod> getNoduri(){
        return G.keySet();
    }

    /**
     * determina componenta conexa pornind de la un nod
     * @param source - nodul sursa
     * @return - componenta conexa determinata de nodul sursa
     */
    public GrafListaAdiacenta<Nod, Muchie> componentaConexa(Nod source){
        GrafListaAdiacenta<Nod, Muchie> graf = new GrafListaAdiacenta<>();
        Queue<Nod> queue = new LinkedList<>();
        Map<Nod, Boolean> visited = new HashMap<>();
        for(Nod nod : G.keySet()) {
            visited.put(nod, false);
        }
        queue.add(source);
        graf.addNod(source);
        while (!queue.isEmpty()){
            Nod t = queue.poll();
            if(t != null && !visited.get(t)){
                visited.put(t, true);
                for(Muchie m : getMuchiiAdiacente(t)){
                    Nod nod = m.getFirst().equals(t) ? m.getSecond() : m.getFirst();
                    if(!visited.get(nod)){
                        queue.add(nod);
                        graf.addMuchie(m);
                    }
                }
            }
        }
        return graf;
    }

    /**
     * determina componentele conexe ale grafului
     * @return - lista de componente conexe
     */
    public List<GrafListaAdiacenta<Nod, Muchie>> componenteConexe(){
        List<GrafListaAdiacenta<Nod, Muchie>> grafuri = new LinkedList<>();
        Map<Nod, Boolean> visited = new HashMap<>();
        for(Nod nod : G.keySet()) {
            visited.put(nod, false);
        }
        for(Nod nod : G.keySet()){
            if(!visited.get(nod)){
                GrafListaAdiacenta<Nod, Muchie> graf = componentaConexa(nod);
                grafuri.add(graf);
                graf.getNoduri().forEach(n -> visited.put(n, true));
            }
        }
        return grafuri;
    }
}
