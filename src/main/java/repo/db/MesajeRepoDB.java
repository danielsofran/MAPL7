package repo.db;

import domain.Mesaj;
import domain.Prietenie;
import domain.validation.Validator;
import exceptii.DuplicatedElementException;
import repo.Repository;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public class MesajeRepoDB extends AbstractRepoDB<Long, Mesaj> implements Repository<Long, Mesaj> {

    /**
     * Constructorul clasei UserRepoDB
     *
     * @param validator - validatorul
     * @param url       - url-ul bazei de date
     * @param username  - username-ul bazei de date
     * @param password  - parola userului bazei de date
     */
    public MesajeRepoDB(Validator<Mesaj> validator, String url, String username, String password) {
        super(validator, url, username, password);
    }
    
    private Mesaj extractMesaj(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        Long id1 = resultSet.getLong("userFrom");
        Long id2 = resultSet.getLong("userTo");
        String content = resultSet.getString("content");
        return new Mesaj(id, id1, id2, content);
    }

    @Override
    public Mesaj findOne(Long id) throws IllegalArgumentException {
        if(id == null)
            throw new IllegalArgumentException("Id-ul unui mesaj nu poate fi null!");
        String SQL = "SELECT * FROM \"Mesaje\" WHERE id = ?";
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement(SQL)){
            statement.setLong(1, id);
            try(ResultSet resultSet = statement.executeQuery()){
                if(resultSet.next()){
                    return extractMesaj(resultSet);
                }
            }
        }
        catch (SQLException e){
            System.err.println("Mesaje findone predicate");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Mesaj findOne(Predicate<Mesaj> predicate) throws IllegalArgumentException {
        if(predicate == null)
            throw new IllegalArgumentException("Predicate-ul nu poate fi null!");
        return findAll().stream().filter(predicate).findFirst().orElse(null);
    }

    @Override
    public Collection<Mesaj> findAll() {
        Set<Mesaj> mesaje = new HashSet<>();
        String SQL = "SELECT * FROM \"Mesaje\" ORDER BY moment";
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement(SQL)){
            try(ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next())
                    mesaje.add(extractMesaj(resultSet));
            }
        }
        catch (SQLException e){
            System.err.println("Mesaje findall");
            e.printStackTrace();
        }
        return mesaje;
    }

    @Override
    public Mesaj save(Mesaj mesaj) throws IllegalArgumentException, DuplicatedElementException {
        if(mesaj == null)
            throw new IllegalArgumentException("Mesajul este null!");
        validator.validate(mesaj);
        String SQL = "INSERT INTO \"Mesaje\"(\"userFrom\", \"userTo\", content, moment) VALUES(?, ?, ?, NOW()) RETURNING id";
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement(SQL)){
            statement.setLong(1, mesaj.getUserFrom());
            statement.setLong(2, mesaj.getUserTo());
            statement.setString(3, mesaj.getContent());
            statement.execute();
            ResultSet rs = statement.getResultSet();
            rs.next();
            Long id = rs.getLong(1);
            mesaj.setId(id);

            return mesaj;
        }
        catch (SQLException ex){
            System.err.println("Mesaje save");
            if(ex.getMessage().contains("duplicate key value violates unique constraint"))
                throw new DuplicatedElementException("Mesajul exista deja!");
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public Mesaj delete(Long id) throws IllegalArgumentException {
        if(id == null)
            throw new IllegalArgumentException("Id-ul nu poate fi null!");
        Mesaj mesaj = findOne(id);
        if(mesaj == null)
            return null;
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("DELETE FROM \"Mesaje\" WHERE id = ?")) {
            statement.setLong(1, id);
            statement.executeUpdate();
            return mesaj;
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Mesaj delete(Predicate<Mesaj> predicate) throws IllegalArgumentException {
        if(predicate == null)
            throw new IllegalArgumentException("Predicate-ul nu poate fi null!");
        Mesaj mesaj = findOne(predicate);
        if(mesaj == null)
            return null;
        return delete(mesaj.getId());
    }

    @Override
    public Mesaj update(Long id, Mesaj mesaj) throws IllegalArgumentException {
        if(id == null)
            throw new IllegalArgumentException("Id-ul nu poate fi null!");
        if(mesaj == null)
            throw new IllegalArgumentException("Entitatea nu poate fi null!");
        String sql = "UPDATE \"Mesaje\" SET \"userFrom\" = ?, \"userTo\" = ?, \"content\" = ?, moment=NOW() WHERE id = ?";
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, mesaj.getUserFrom());
            statement.setLong(2, mesaj.getUserTo());
            statement.setString(3, mesaj.getContent());
            statement.setLong(4, id);
            int changes = statement.executeUpdate();
            if(changes == 0)
                return null;
            return mesaj;
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void clear() {
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("DELETE FROM \"Mesaje\"")) {
            statement.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
}
