package test;

import controller.DataController;
import controller.DataPersistenceMethod;
import domain.PrietenieState;
import domain.UserDetails;
import exceptii.DuplicatedElementException;
import exceptii.NotExistentException;
import exceptii.RepositoryException;
import graf.StrategiiCelMaiLungDrum;

import java.time.LocalDateTime;

public class Test {
    /**
     * test
     * @param args - args
     */
    public static void main(String[] args) {
        DataController controller = new DataController(DataPersistenceMethod.InMemory);
        controller.clear();

        UserDetails user1 = new UserDetails(); user1.add("Numehthg", "Email1", "Pasw1");
        UserDetails user2 = new UserDetails(); user2.add("Nume ejfbeh jkdfb", "Email2", "Pasw2");
        UserDetails user3 = new UserDetails(); user3.add("Nume ejfbeh jkdfb", "Email2", "Pasw2");

        controller.getServiceUser().add(user1);
        controller.getServiceUser().add(user2);
        controller.getServiceUser().add(user3);

        controller.getServicePrietenii().add(1L, 2L, LocalDateTime.now(), PrietenieState.Accepted);
        controller.getServicePrietenii().add(1L, 3L, LocalDateTime.now(), PrietenieState.Accepted);
        controller.getServicePrietenii().add(2L, 3L, LocalDateTime.now(), PrietenieState.Accepted);
        try{controller.getServicePrietenii().add(2L, 3L, LocalDateTime.now(), PrietenieState.Accepted); assert false;}
        catch (DuplicatedElementException ignored){}
        try{controller.getServicePrietenii().add(1L, 4L, LocalDateTime.now(), PrietenieState.Accepted); assert false;}
        catch (RepositoryException ignored){}

        controller.getServicePrietenii().remove(3L);
        try{controller.getServicePrietenii().remove(1L, 4L); assert false;}
        catch (NotExistentException ignored){}

        controller.getServiceUser().remove(1L);
        assert controller.getServicePrietenii().findAll().isEmpty();

        assert controller.getServicePrietenii().getCeaMaiSociabilaComunitate(StrategiiCelMaiLungDrum.Backtracking).getSecond() == 0;
        assert controller.getServicePrietenii().getCeaMaiSociabilaComunitate(StrategiiCelMaiLungDrum.N_DFSuri).getSecond() == 0;

        controller.getServiceUser().remove(2L);
        controller.getServiceUser().remove(3L);

        assert controller.getServiceUser().findAll().isEmpty();
        assert controller.getServicePrietenii().findAll().isEmpty();

        controller.getServiceUser().add(user1);
        controller.getServiceUser().add(user2);
        controller.getServiceUser().add(user3);

        try{ controller.getServicePrietenii().add(1L, 2L, LocalDateTime.now(), PrietenieState.Accepted); assert false;}
        catch (NotExistentException ignored){}
        controller.getServicePrietenii().add(4L, 5L, LocalDateTime.now(), PrietenieState.Accepted);
        controller.getServicePrietenii().add(5L, 6L, LocalDateTime.now(), PrietenieState.Accepted);
        controller.getServicePrietenii().add(4L, 6L, LocalDateTime.now(), PrietenieState.Accepted);

        assert controller.getServiceUser().findAll().size() == 3;
        assert controller.getServicePrietenii().findAll().size() == 3;

        controller.getServiceUser().remove(4L);
        assert controller.getServicePrietenii().findAll().size() == 1;
    }
}
