package test;

import config.ApplicationContext;
import domain.User;
import domain.validation.UserValidator;
import domain.validation.Validator;
import exceptii.DuplicatedElementException;
import exceptii.NotExistentException;
import repo.Repository;
import repo.UserRepoDB;

public class TestDB {
    private static Repository<Long, User> repo;

    private static void testConnection() {
        Validator<User> validator = new UserValidator();
        String url = ApplicationContext.getPROPERTIES().getProperty("db.url");
        String username = ApplicationContext.getPROPERTIES().getProperty("db.username");
        String password = ApplicationContext.getPROPERTIES().getProperty("db.password");
        repo = new UserRepoDB(validator, url, username, password);
    }

    private static void testFindAll() {
        User user1 = new User(1L, "John", "1234", "john@gmail.com");
        User user2 = new User(2L, "Mary", "1234", "mary@gmail.com");
        repo.clear();
        repo.save(user1);
        repo.save(user2);
        try {
            repo.save(user1);
            assert false;
        }
        catch(DuplicatedElementException ignored) {}
        repo.delete(user1.getId());
        try {
            repo.delete(user1.getId());
            assert false;
        }
        catch (NotExistentException ignored) {}
        repo.update(user2.getId(), user1);
        repo.findAll().forEach(System.out::println);
        User fuser = repo.findOne(user2.getId());
        assert fuser.getId().equals(user2.getId());
        fuser.setId(user1.getId());
        assert fuser.equals(user1);
    }

    public static void main(String[] args) {
        testConnection();
        testFindAll();
    }
}
