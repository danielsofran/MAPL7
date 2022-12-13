package domain;

import utils.Pereche;
import utils.Utils;

import java.time.LocalDateTime;
import java.util.Objects;

public class Prietenie extends Entity<Long> implements Pereche<Long, Long> {
    private Long id_user1;
    private Long id_user2;
    private LocalDateTime friendsFrom;
    private PrietenieState state;

    /**
     * Constructorul implicit
     */
    public Prietenie(){}

    /**
     * Constructor cu parametri
     * @param id - id-ul prieteniei
     * @param id1 - id-ul primului user
     * @param id2 - id-ul celui de-al doilea user
     */
    public Prietenie(Long id, Long id1, Long id2, LocalDateTime friendsFrom, PrietenieState state) {
        super(id);
        id_user1 = id1;
        id_user2 = id2;
        this.friendsFrom = friendsFrom;
        this.state = state;
    }

    /**
     * getter pentru primul user
     * @return id-ul primului user
     */
    @Override
    public Long getFirst() {
        return id_user1;
    }

    /**
     * getter pentru al doilea user
     * @return id-ul celui de-al doilea user
     */
    @Override
    public Long getSecond() {
        return id_user2;
    }

    public Long getSenderId() { return id_user1; }
    public Long getReceiverId() {return id_user2; }

    public void setSenderId(Long id){
        id_user1 = id;
    }

    public void setReceiverId(Long id){
        id_user2 = id;
    }
    /**
     * getter pentru momentul de cand cei doi useri sunt prieteni
     * @return - mometnul de cand cei doi useri sunt prieteni
     */
    public LocalDateTime getFriendsFrom() {
        return friendsFrom;
    }

    /**
     * getter pentru starea prieteniei
     * Accepted - cei doi utilizatori sunt prieteni
     * Pending - primul utilizator i-a trimis cerere celui de-al doilea
     * @return - starea prieteniei
     */
    public PrietenieState getState(){
        return state;
    }

    /**
     * setter pentru primul user
     * @param user1 - id-ul primului user
     */
    @Override
    public void setFirst(Long user1) {
        this.id_user1 = user1;
    }

    /**
     * setter pentru al doilea user
     * @param user2 - id-ul celui de-al doilea user
     */
    @Override
    public void setSecond(Long user2) {
        this.id_user2 = user2;
    }

    /**
     * setter pentru momentul de cand are loc prietenia
     * @param friendsFrom - momentul respectiv
     */
    public void setFriendsFrom(LocalDateTime friendsFrom) {
        this.friendsFrom = friendsFrom;
    }

    /**
     * setter pentru starea prieteniei
     * @param state - starea prieteniei
     */
    public void setState(PrietenieState state) {
        this.state = state;
    }

    /**
     * verifica daca unul dintre userii prieteniei are id-ul dat
     * @param id - id-ul dat
     * @return true - daca unul dintre userii prieteniei are id-ul dat
     */
    public boolean contains(Long id){
        return id_user1.equals(id) || id_user2.equals(id);
    }

    /**
     * verifica daca prietenia are cei doi useri cu id-urile id1 si id2
     * @apiNote nu conteaza ordinea id-urilor
     * @param id1 - id-ul primului user
     * @param id2 - id-ul celui de-al doilea user
     * @return true - daca prietenia are cei doi useri cu id-urile id1 si id2
     */
    public boolean contains(Long id1, Long id2){
        return (id_user1.equals(id1) && id_user2.equals(id2)) || (id_user1.equals(id2) && id_user2.equals(id1));
    }

    public Long getOther(Long id){
        return (id_user1.equals(id)) ? id_user2 : id_user1;
    }

    /**
     * verifica daca doua prietenii sunt identice
     * @param o - prietenia cu care se compara
     * @return true daca sunt egale, false altfel
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Prietenie prietenie = (Prietenie) o;
        return getId().equals(prietenie.getId()) ||
                id_user1.equals(prietenie.id_user1) && id_user2.equals(prietenie.id_user2) ||
                id_user1.equals(prietenie.id_user2) && id_user2.equals(prietenie.id_user1);
    }

    /**
     * calculeaza hashcode-ul prieteniei
     * @return hashcode-ul prieteniei
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId(), id_user1, id_user2);
    }

    /**
     * transforma prietenia intr-un string
     * @return string-ul prieteniei
     */
    @Override
    public String toString() {
        return "id: " + id_user1 + " - id: " + id_user2 + " din \'" + friendsFrom.format(Utils.DATE_TIME_FORMATTER) + '\'';
    }
}
