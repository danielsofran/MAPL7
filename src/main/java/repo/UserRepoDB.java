package repo;

import domain.User;
import domain.validation.Validator;
import exceptii.DuplicatedElementException;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public class UserRepoDB extends AbstractRepoDB<Long, User> implements Repository<Long, User>{
    /**
     * Constructorul clasei UserRepoDB
     *
     * @param validator - validatorul
     * @param url       - url-ul bazei de date
     * @param username  - username-ul bazei de date
     * @param password  - parola userului bazei de date
     */
    public UserRepoDB(Validator<User> validator, String url, String username, String password) {
        super(validator, url, username, password);
    }

    /**
     * gaseste un user dupa id
     * @param id - id-ul entitatii
     * @return - entitatea gasita sau null daca nu exista
     * @throws IllegalArgumentException - daca id-ul este null
     */
    @Override
    public User findOne(Long id) throws IllegalArgumentException {
        if (id == null) {
            throw new IllegalArgumentException("Id-ul nu poate fi null!");
        }
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"Useri\" WHERE id = ?")) {
            statement.setLong(1, id);
            try(ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String nume = resultSet.getString("nume");
                    String password = resultSet.getString("password");
                    String email = resultSet.getString("email");
                    return new User(id, nume, password, email);
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * gaseste entitatea care respecta conditia data
     * @param predicate - predicatul dupa care se filtreaza
     * @return - entitatea gasita sau null daca nu exista
     * @throws IllegalArgumentException - daca predicatul este null
     */
    @Override
    public User findOne(Predicate<User> predicate) throws IllegalArgumentException {
        if (predicate == null) {
            throw new IllegalArgumentException("Predicatul nu poate fi null!");
        }
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"Useri\"")) {
            try(ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Long id = resultSet.getLong("id");
                    String nume = resultSet.getString("nume");
                    String password = resultSet.getString("password");
                    String email = resultSet.getString("email");
                    User user = new User(id, nume, password, email);
                    if (predicate.test(user)) {
                        return user;
                    }
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * gaseste toate entitatile
     * @return - toate entitatile
     */
    @Override
    public Collection<User> findAll() {
        Set<User> users = new HashSet<>();
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"Useri\"");
            ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String nume = resultSet.getString("nume");
                String password = resultSet.getString("password");
                String email = resultSet.getString("email");
                User user = new User(id, nume, password, email);
                users.add(user);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return users;
    }

    /**
     * salveaza o entitate
     * @param entity - entitatea de salvat
     * @return - entitatea salvata sau null daca nu a fost salvata
     * @throws IllegalArgumentException - daca entitatea este null
     * @throws DuplicatedElementException - daca entitatea exista deja
     */
    @Override
    public User save(User entity) throws IllegalArgumentException, DuplicatedElementException {
        validator.validate(entity);
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("INSERT INTO \"Useri\" VALUES (?, ?, ?, ?)")) {
            statement.setLong(1, entity.getId());
            statement.setString(2, entity.getName());
            statement.setString(3, entity.getPassword());
            statement.setString(4, entity.getEmail());
            statement.executeUpdate();
            return entity;
        }
        catch (SQLException e) {
            if(e.getMessage().contains("duplicate key value violates unique constraint")) {
                throw new DuplicatedElementException("Userul exista deja!");
            }
            e.printStackTrace();
            return null;
        }
    }

    /**
     * sterge o entitate
     * @param id - id-ul entitatii de sters
     * @return - entitatea stearsa sau null daca nu exista
     * @throws IllegalArgumentException - daca id-ul este null
     */
    @Override
    public User delete(Long id) throws IllegalArgumentException {
        if (id == null) {
            throw new IllegalArgumentException("Id-ul nu poate fi null!");
        }
        User user = findOne(id);
        if (user == null) {
            return null;
        }
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("DELETE FROM \"Useri\" WHERE id = ?")) {
            statement.setLong(1, id);
            statement.executeUpdate();
            return user;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * sterge prima entitate care respecta conditia data
     * @param predicate - predicatul dupa care se filtreaza
     * @return - entitatea stearsa sau null daca nu exista
     * @throws IllegalArgumentException - daca predicatul este null
     */
    @Override
    public User delete(Predicate<User> predicate) throws IllegalArgumentException {
        if(predicate == null) {
            throw new IllegalArgumentException("Predicatul nu poate fi null!");
        }
        User user = findOne(predicate);
        if (user == null) {
            return null;
        }
        return delete(user.getId());
    }

    /**
     * actualizeaza o entitate cu noile date
     * @param id - id-ul entitatii de actualizat
     * @param entity - entitatea cu noile date
     * @return - entitatea actualizata sau null daca nu exista
     * @throws IllegalArgumentException - daca id-ul sau entitatea sunt null
     */
    @Override
    public User update(Long id, User entity) throws IllegalArgumentException {
        if (id == null) {
            throw new IllegalArgumentException("Id-ul nu poate fi null!");
        }
        validator.validate(entity);
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("UPDATE \"Useri\" SET nume = ?, password = ?, email = ? WHERE id = ?")) {
            statement.setString(1, entity.getName());
            statement.setString(2, entity.getPassword());
            statement.setString(3, entity.getEmail());
            statement.setLong(4, id);
            int rowschanged = statement.executeUpdate();
            return (rowschanged == 0) ? null : entity;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * sterge toate entitatile
     */
    @Override
    public void clear() {
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("DELETE FROM \"Useri\"")) {
            statement.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
