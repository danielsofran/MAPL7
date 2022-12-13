package service;

import domain.Prietenie;
import domain.PrietenieState;
import domain.User;
import exceptii.DuplicatedElementException;
import exceptii.NotExistentException;
import exceptii.ServiceException;
import graf.AlgoritmiGraf;
import graf.GrafListaAdiacenta;
import graf.StrategiiCelMaiLungDrum;
import repo.Repository;
import utils.Pair;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class ServicePrietenii {
    protected Repository<Long, User> repoUser;
    protected Repository<Long, Prietenie> repoPrietenii;
    protected Long idGenerator;

    /**
     * Constructorul clasei ServicePrietenii
     * initializeaza repository-urile, parserele si graful
     *
     * @param repoPrietenii - repository-ul de prietenii
     * @param repoUser      - repository-ul de useri
     */
    public ServicePrietenii(Repository<Long, Prietenie> repoPrietenii, Repository<Long, User> repoUser) {
        this.repoPrietenii = repoPrietenii;
        this.repoUser = repoUser;
        idGenerator = 1L;
        repoPrietenii.findAll().stream().mapToLong(Prietenie::getId).max().ifPresent(id -> idGenerator = id + 1);
    }

    /**
     * adauga o prietenie in repository
     *
     * @param id1            - id-ul primului user
     * @param id2            - id-ul celui de-al doilea user
     * @param friendsFrom    - momentul de cand prietenia are loc
     * @param prietenieState - starea prieteniei
     */
    public void add(Long id1, Long id2, LocalDateTime friendsFrom, PrietenieState prietenieState) {
        if (repoUser.findOne(id1) == null || repoUser.findOne(id2) == null)
            throw new NotExistentException("Unul dintre useri nu exista!");
        Prietenie prietenie = new Prietenie(idGenerator, id1, id2, friendsFrom, prietenieState);
        if (repoPrietenii.findOne(pr -> pr.equals(prietenie)) != null)
            throw new DuplicatedElementException("Prietenia exista deja!");
        repoPrietenii.save(prietenie);
        idGenerator++;
    }

    /**
     * sterge o prietenie din repository
     * @param id - id-ul prieteniei de sters
     */
    public void remove(Long id) {
        repoPrietenii.delete(id);
    }

    /**
     * sterge prietenia dintre doi useri
     * @param id1 - id-ul primului user
     * @param id2 - id-ul celui de-al doilea user
     */
    public void remove(Long id1, Long id2) {
        if(Objects.equals(id1, id2)) throw new ServiceException("Nu poti sterge o prietenie cu tine insuti!");
        Prietenie stearsa = repoPrietenii.delete(prietenie -> prietenie.contains(id1, id2));
        if (stearsa == null)
            throw new NotExistentException("Prietenia nu exista!");
    }

    /**
     * modifica o prietenie din repository
     *
     * @param id             - id-ul prieteniei de modificat
     * @param id1            - noul id al primului user
     * @param id2            - noul id al celui de-al doilea user
     * @param friendsFrom    - momentul din care are loc prietenia
     * @param prietenieState - starea prieteniei
     * @apiNote id-ul prieteniei nu se poate modifica
     */
    public void update(Long id, Long id1, Long id2, LocalDateTime friendsFrom, PrietenieState prietenieState) {
        Prietenie oldPrietenie = repoPrietenii.findOne(id);
        if (oldPrietenie == null)
            throw new NotExistentException("Prietenia nu exista!");
        if(friendsFrom == null)
            friendsFrom = oldPrietenie.getFriendsFrom();
        Prietenie newPrietenie = new Prietenie(id, id1, id2, friendsFrom, prietenieState);
        repoPrietenii.update(id, newPrietenie);
    }

    public Prietenie acceptRequest(Prietenie prietenie){
        update(prietenie.getId(), prietenie.getFirst(), prietenie.getSecond(), prietenie.getFriendsFrom(), PrietenieState.Accepted);
        prietenie.setState(PrietenieState.Accepted);
        return prietenie;
    }

    public Prietenie rejectRequest(Prietenie prietenie){
        remove(prietenie.getId());
        return null;
    }

    /**
     * determina o prietenie dupa id
     * @param id - id-ul prieteniei cautate
     * @return prietenia cu id-ul dat
     * @throws NotExistentException - daca prietenia nu exista
     */
    public Prietenie findOne(Long id) throws NotExistentException {
        Prietenie prietenie = repoPrietenii.findOne(id);
        if(prietenie == null)
            throw new NotExistentException("Prietenia nu exista!");
        return prietenie;
    }

    /**
     * determina o prietenie din repository
     *
     * @param user1 - id-ul primului user
     * @param user2 - id-ul celui de-al doilea user
     * @return - prietenia determinata
     * @throws NotExistentException - daca prietenia nu exista
     */
    public Prietenie findByUserIds(User user1, User user2) throws NotExistentException {
        if(user1 == null || user2 == null)
            throw new NotExistentException("Unul din useri nu exista!");
        Prietenie prietenie = repoPrietenii.findOne(e -> e.contains(user1.getId(), user2.getId()));
        if(prietenie == null)
            throw new NotExistentException("Prietenia nu exista!");
        return prietenie;
    }

    public List<User> findPrieteni(User user){
        return repoPrietenii.findAll().stream()
                .filter(prietenie -> prietenie.contains(user.getId()))
                .filter(prietenie -> prietenie.getState() == PrietenieState.Accepted)
                .map(prietenie -> {
                    if(Objects.equals(prietenie.getFirst(), user.getId())) return prietenie.getSecond();
                    return prietenie.getFirst();
                })
                .filter(id_pr -> !id_pr.equals(user.getId()))
                .map(id_pr -> repoUser.findOne(id_pr)).collect(Collectors.toList());
    }

    public User getOther(Prietenie prietenie, User user){
        return repoUser.findOne(prietenie.getOther(user.getId()));
    }

    public Prietenie sendPrietenieRequest(User userFrom, User userTo){
        Long id1 = userFrom.getId();
        Long id2 = userTo.getId();
        LocalDateTime friendsFrom = LocalDateTime.now();
        PrietenieState prietenieState = PrietenieState.Pending;

        if (repoUser.findOne(id1) == null || repoUser.findOne(id2) == null)
            throw new NotExistentException("Unul dintre useri nu exista!");
        Prietenie prietenie = new Prietenie(idGenerator, id1, id2, friendsFrom, prietenieState);
        if (repoPrietenii.findOne(pr -> pr.equals(prietenie)) != null)
            throw new DuplicatedElementException("Prietenia exista deja!");
        repoPrietenii.save(prietenie);
        idGenerator++;

        return prietenie;
    }

    public List<Prietenie> findCereri(User user){
        return repoPrietenii.findAll().stream()
                .filter(prietenie -> prietenie.getReceiverId() == user.getId() || prietenie.contains(user.getId()) &&prietenie.getState() != PrietenieState.Pending)
                //s.filter(prietenie -> prietenie.getState() == PrietenieState.Pending)
                .collect(Collectors.toList());
    }


    /**
     * determina toate prietenile din repository
     * @return - lista de prietenii
     */
    public Collection<Prietenie> findAll() {
        return repoPrietenii.findAll();
    }

    /**
     * determina graful de prietenii
     * @return - graful de prietenii, representat prin lista de adiacenta
     */
    private GrafListaAdiacenta<Long, Prietenie> getGraf() {
        GrafListaAdiacenta<Long, Prietenie> graf = new GrafListaAdiacenta<>();
        repoUser.findAll().stream().map(User::getId).forEach(graf::addNod);
        List<Prietenie> prietenii = new ArrayList<>(repoPrietenii.findAll());
        prietenii.forEach(graf::addMuchie);
        return graf;
    }
    /**
     * determina numarul de comunitati
     * @return - numarul de comunitati
     */
    public Integer getNumarComunitati(){
        List<GrafListaAdiacenta<Long, Prietenie>> comunitati = getGraf().componenteConexe();
        return comunitati.size();
    }

    /**
     * determina cea mai sociabila comunitate
     * @param strategie - algoritmul pe grafuri folosit pentru determinarea componentei cu cel mai lung drum
     * @return - pereche formata din multimea de useri din comunitate si scorul comunitatii (cel mai lung drum din graf)
     */
    public Pair<Set<User>, Integer> getCeaMaiSociabilaComunitate(StrategiiCelMaiLungDrum strategie){
        Pair<GrafListaAdiacenta<Long, Prietenie>, Integer> comunitate;
        GrafListaAdiacenta<Long, Prietenie> graf = getGraf();
        if(strategie == StrategiiCelMaiLungDrum.Backtracking) comunitate = AlgoritmiGraf.componentWithLongestPath(graf);
        else comunitate = AlgoritmiGraf.componentWithLongestPath2(graf);
        Set<User> users = comunitate.getFirst().getNoduri().stream().map(repoUser::findOne).collect(Collectors.toSet());
        return new Pair<>(users, comunitate.getSecond());
    }
}
