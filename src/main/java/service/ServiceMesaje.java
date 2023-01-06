package service;

import domain.Mesaj;
import domain.Prietenie;
import domain.User;
import repo.Repository;
import utils.events.ChangeEventType;
import utils.events.ChangedEvent;
import utils.observer.MyAbstractObservable;

import java.util.Collection;
import java.util.stream.Collectors;

public class ServiceMesaje extends MyAbstractObservable<ChangedEvent<Mesaj>> {
    protected Repository<Long, Mesaj> repoMesaje;
    protected Repository<Long, User> repoUser;

    public ServiceMesaje(Repository<Long, Mesaj> repoMesaje, Repository<Long, User> repoUser)
    {
        this.repoMesaje = repoMesaje;
        this.repoUser = repoUser;
    }

    public Mesaj sendMesaj(User userFrom, User userTo, String content)
    {
        Mesaj mesaj = new Mesaj(userFrom.getId(), userTo.getId(), content);
        mesaj = repoMesaje.save(mesaj);
        notifyObservers(new ChangedEvent<>(ChangeEventType.ADDED, mesaj));
        return mesaj;
    }

    public Collection<Mesaj> findAll(User user1, User user2){
        return repoMesaje.findAll().stream().filter(msg -> msg.contains(user1, user2)).collect(Collectors.toList());
    }
}
