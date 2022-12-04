package repo;

import domain.Prietenie;
import domain.PrietenieState;
import domain.validation.Validator;
import exceptii.DuplicatedElementException;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public class PrietenieRepoDB implements Repository<Long, Prietenie> {
    protected final Validator<Prietenie> validator;
    protected final String url;
    protected final String username;
    protected final String password;

    /**
     * Constructorul clasei PrietenieRepoDB
     * @param validator - validatorul
     * @param url - url-ul bazei de date
     * @param username - username-ul bazei de date
     * @param password - parola userului bazei de date
     */
    public PrietenieRepoDB(Validator<Prietenie> validator, String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    /**
     * creeaza o prietenie dintr-un rezultat de tip ResultSet
     * @param resultSet - rezultatul din baza de date
     * @return - prietenia creata
     * @throws SQLException - daca rezultatul nu este valid
     */
    private Prietenie extractPrietenie(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        Long id1 = resultSet.getLong("id_user1");
        Long id2 = resultSet.getLong("id_user2");
        LocalDate data = resultSet.getDate("data").toLocalDate();
        LocalTime ora = resultSet.getTime("time").toLocalTime();
        PrietenieState status = PrietenieState.valueOf(resultSet.getString("prietenieState"));
        return new Prietenie(id, id1, id2, LocalDateTime.of(data, ora), status);
    }

    /**
     * gaseste o prietenie dupa id
     * @param id - id-ul entitatii
     * @return - entitatea gasita sau null daca nu exista
     * @throws IllegalArgumentException - daca id-ul este null
     */
    @Override
    public Prietenie findOne(Long id) throws IllegalArgumentException {
        if(id == null)
            throw new IllegalArgumentException("Id-ul nu poate fi null!");
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"Prietenii\" WHERE id = ?")) {
            statement.setLong(1, id);
            try(ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return extractPrietenie(resultSet);
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Prietenie findOne(Predicate<Prietenie> predicate) throws IllegalArgumentException {
        if(predicate == null)
            throw new IllegalArgumentException("Predicate-ul nu poate fi null!");
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"Prietenii\"")) {
            try(ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Prietenie prietenie = extractPrietenie(resultSet);
                    if(predicate.test(prietenie))
                        return prietenie;
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * gaseste toate prietenile
     * @return - toate prietenile
     */
    @Override
    public Collection<Prietenie> findAll() {
        Set<Prietenie> prietenii = new HashSet<>();
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"Prietenii\"")) {
            try(ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Long id = resultSet.getLong("id");
                    Long id1 = resultSet.getLong("id_user1");
                    Long id2 = resultSet.getLong("id_user2");
                    LocalDate data = resultSet.getDate("data").toLocalDate();
                    LocalTime time = resultSet.getTime("time").toLocalTime();
                    PrietenieState state = PrietenieState.valueOf(resultSet.getString("prietenieState"));
                    prietenii.add(new Prietenie(id, id1, id2, LocalDateTime.of(data, time), state));
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return prietenii;
    }

    /**
     * salveaza o prietenie in baza de date
     * @param prietenie - entitatea de salvat
     * @return - entitatea salvata sau null daca nu a fost salvata
     * @throws IllegalArgumentException - daca entitatea este null
     * @throws DuplicatedElementException - daca entitatea exista deja
     */
    @Override
    public Prietenie save(Prietenie prietenie) throws IllegalArgumentException, DuplicatedElementException {
        if(prietenie == null)
            throw new IllegalArgumentException("Prietenia nu poate fi null!");
        validator.validate(prietenie);
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("INSERT INTO \"Prietenii\" VALUES (?, ?, ?, ?, ?, ?)")) {
            statement.setLong(1, prietenie.getId());
            statement.setLong(2, prietenie.getFirst());
            statement.setLong(3, prietenie.getSecond());
            statement.setDate(4, Date.valueOf(prietenie.getFriendsFrom().toLocalDate()));
            statement.setTime(5, Time.valueOf(prietenie.getFriendsFrom().toLocalTime()));
            statement.setString(6, prietenie.getState().toString());
            statement.executeUpdate();
            return prietenie;
        }
        catch (SQLException e){
            if(e.getMessage().contains("duplicate key value violates unique constraint"))
                throw new DuplicatedElementException("Prietenia exista deja!");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * sterge o prietenie din baza de date
     * @param id - id-ul entitatii de sters
     * @return - entitatea stearsa sau null daca nu exista
     * @throws IllegalArgumentException - daca id-ul este null
     */
    @Override
    public Prietenie delete(Long id) throws IllegalArgumentException {
        if(id == null)
            throw new IllegalArgumentException("Id-ul nu poate fi null!");
        Prietenie prietenie = findOne(id);
        if(prietenie == null)
            return null;
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("DELETE FROM \"Prietenii\" WHERE id = ?")) {
            statement.setLong(1, id);
            statement.executeUpdate();
            return prietenie;
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * sterge o prietenie din baza de date dupa un predicat
     * @param predicate - predicatul dupa care se filtreaza
     * @return - entitatea stearsa sau null daca nu exista
     * @throws IllegalArgumentException - daca predicatul este null
     */
    @Override
    public Prietenie delete(Predicate<Prietenie> predicate) throws IllegalArgumentException {
        if(predicate == null)
            throw new IllegalArgumentException("Predicate-ul nu poate fi null!");
        Prietenie prietenie = findOne(predicate);
        if(prietenie == null)
            return null;
        return delete(prietenie.getId());
    }

    /**
     * actualizeaza o prietenie din baza de date
     * @param id - id-ul entitatii de actualizat
     * @param entity - entitatea cu noile date
     * @return - entitatea actualizata sau null daca nu exista
     * @throws IllegalArgumentException - daca id-ul sau entitatea sunt null
     */
    @Override
    public Prietenie update(Long id, Prietenie entity) throws IllegalArgumentException {
        if(id == null)
            throw new IllegalArgumentException("Id-ul nu poate fi null!");
        if(entity == null)
            throw new IllegalArgumentException("Entitatea nu poate fi null!");
        String sql = "UPDATE \"Prietenii\" SET id_user1 = ?, id_user2 = ?, \"data\" = ?, \"time\" = ?, \"prietenieState\" = ? WHERE id = ?";
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, entity.getFirst());
            statement.setLong(2, entity.getSecond());
            statement.setDate(3, Date.valueOf(entity.getFriendsFrom().toLocalDate()));
            statement.setTime(4, Time.valueOf(entity.getFriendsFrom().toLocalTime()));
            statement.setString(5, entity.getState().toString());
            statement.setLong(6, id);
            int changes = statement.executeUpdate();
            if(changes == 0)
                return null;
            return entity;
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * sterge toate prietenile din baza de date, dar si toti userii
     */
    @Override
    public void clear() {
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("DELETE FROM \"Prietenii\"")) {
            statement.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
}
