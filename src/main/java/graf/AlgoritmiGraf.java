package graf;

import utils.Pair;
import utils.Pereche;

import java.util.*;

public class AlgoritmiGraf {
    /**
     * parcurge graful in latime
     * @param graf - graful de parcurs
     * @param source - nodul sursa
     * @param visited - map in care se retin nodurile vizitate
     * @param parent - map in care se retin parintii nodurilor
     * @param distance - map in care se retin distantele nodurilor
     * @param <Nod> - tipul nodurilor
     * @param <Muchie> - tipul muchiilor
     */
    protected static <Nod, Muchie extends Pereche<Nod, Nod>> void BFS(GrafListaAdiacenta<Nod, Muchie> graf, Nod source, Map<Nod, Boolean> visited, Map<Nod, Nod> parent, Map<Nod, Integer> distance){
        Queue<Nod> Q = new LinkedList<>();
        Q.add(source);
        visited.put(source, true);
        parent.put(source, null);
        distance.put(source, 0);
        while(!Q.isEmpty()){
            Nod nod = Q.poll();
            for(Nod vecin : graf.getVecini(nod)){
                if(!visited.get(vecin)){
                    visited.put(vecin, true);
                    parent.put(vecin, nod);
                    distance.put(vecin, distance.get(nod) + 1);
                    Q.add(vecin);
                }
            }
        }
    }

    /**
     * parcurge graful in adancime
     * @param graf - graful de parcurs
     * @param source - nodul sursa
     * @param visited - map in care se retin nodurile vizitate
     * @param parent - map in care se retin parintii nodurilor
     * @param distance - map in care se retin distantele nodurilor
     * @param <Nod> - tipul nodurilor
     * @param <Muchie> - tipul muchiilor
     */
    protected static <Nod, Muchie extends Pereche<Nod, Nod>> void DFS(GrafListaAdiacenta<Nod, Muchie> graf, Nod source, Map<Nod, Boolean> visited, Map<Nod, Nod> parent, Map<Nod, Integer> distance){
        Stack<Nod> S = new Stack<>();
        S.push(source);
        visited.put(source, true);
        parent.put(source, null);
        distance.put(source, 0);
        while(!S.isEmpty()){
            Nod nod = S.pop();
            for(Nod vecin : graf.getVecini(nod)){
                if(!visited.get(vecin)){
                    visited.put(vecin, true);
                    parent.put(vecin, nod);
                    distance.put(vecin, distance.get(nod) + 1);
                    S.push(vecin);
                }
            }
        }
    }

    private static Integer dmax = 0;

    /**
     * determina cel mai lung drum din graf
     * @param graf - graful in care se cauta cel mai lung drum
     * @param source - nodul sursa
     * @param visited - map in care se retin nodurile vizitate
     * @param distance - map in care se retin distantele nodurile
     * @param <Nod> - tipul nodurilor
     * @param <Muchie> - tipul muchiilor
     */
    public static <Nod, Muchie extends Pereche<Nod, Nod>> void BackDrum(GrafListaAdiacenta<Nod, Muchie> graf, Nod source, Map<Nod, Boolean> visited, Map<Nod, Integer> distance){
        for(Nod nod : graf.getVecini(source)){
            if(!visited.get(nod)){
                visited.put(nod, true);
                int d = distance.get(source) + 1;
                distance.put(nod, d);
                if(d > dmax) dmax = d;
                BackDrum(graf, nod, visited, distance);
                visited.put(nod, false);
                distance.put(nod, d-1);
            }
        }
    }

    /**
     * determina componenta care contine cel mai lung drum din graf
     * algoritm: Backtracking
     * @param graf - graful in care se cauta componenta
     * @return - perechea formata din componenta care contine cel mai lung drum si lungimea acestuia
     * @param <Nod> - tipul nodurilor
     * @param <Muchie> - tipul muchiilor
     */
    public static <Nod, Muchie extends Pereche<Nod, Nod>> Pair<GrafListaAdiacenta<Nod, Muchie>, Integer> componentWithLongestPath(GrafListaAdiacenta<Nod, Muchie> graf){
        int dmaxall = -1;
        GrafListaAdiacenta<Nod, Muchie> compmax = new GrafListaAdiacenta<>();
        for(GrafListaAdiacenta<Nod, Muchie> comp : graf.componenteConexe()){
            Set<Nod> noduri = comp.getNoduri();
            // get first element of noduri
            Iterator<Nod> it = noduri.iterator();
            Nod source = it.next();
            Map<Nod, Boolean> visited = new HashMap<>();
            Map<Nod, Integer> distance = new HashMap<>();
            for(Nod nod : noduri){
                visited.put(nod, false);
                distance.put(nod, 0);
            }
            visited.put(source, true);
            dmax = 0;
            BackDrum(graf, source, visited, distance);
            if(dmax > dmaxall) {
                dmaxall = dmax;
                compmax = comp;
            }
        }
        return new Pair<>(compmax, dmaxall);
    }

    /**
     * determina componenta care contine cel mai lung drum din graf
     * algoritm: n DFS-uri
     * @param graf - graful in care se cauta componenta
     * @return - perechea formata din componenta care contine cel mai lung drum si lungimea acestuia
     * @param <Nod> - tipul nodurilor
     * @param <Muchie> - tipul muchiilor
     */
    public static <Nod, Muchie extends Pereche<Nod, Nod>> Pair<GrafListaAdiacenta<Nod, Muchie>, Integer> componentWithLongestPath2(GrafListaAdiacenta<Nod, Muchie> graf){
        int dmax = -1;
        Nod source = null;
        Map<Nod, Nod> parent = new HashMap<>();
        Map<Nod, Boolean> visited = new HashMap<>();
        Map<Nod, Integer> distance = new HashMap<>();
        for(Nod nod : graf.getNoduri()){
            visited.put(nod, false);
            parent.put(nod, null);
            distance.put(nod, 0);
        }
        for(Nod nod : graf.getNoduri()){
            DFS(graf, nod, visited, parent, distance);
            int d = distance.values().stream().max(Integer::compareTo).get();
            if(d > dmax){
                dmax = d;
                source = nod;
            }
            for(Nod nod2 : graf.getNoduri()){
                visited.put(nod2, false);
                parent.put(nod2, null);
                distance.put(nod2, 0);
            }
        }
        return new Pair<>(graf.componentaConexa(source), dmax);
    }
}
