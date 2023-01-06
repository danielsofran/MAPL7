package utils;

import config.ApplicationContext;
import domain.Entity;
import domain.Prietenie;
import domain.User;
import domain.parser.Parser;
import domain.parser.PrietenieParser;
import domain.parser.UserParser;
import domain.validation.PrietenieValidator;
import domain.validation.UserValidator;
import repo.FileRepository;
import repo.InMemoryRepository;
import repo.db.PrietenieRepoDB;
import repo.db.UserRepoDB;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.stream.Stream;

public class RepoMigrate {
    static <E extends Entity<Long>> void copyFromTxt(InMemoryRepository<Long, E> repo, String txtPath, Parser<E> parser) {
        Path path = Paths.get(txtPath);

        try(Stream<String> lines = Files.lines(path)){
            lines.forEach(line -> {
                String[] tokens = line.split(";");
                E entity = parser.parse(tokens);
                repo.save(entity);
            });
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    static void copyUsersFromTxtToSer(){
        String pathUseriTxt = "./data/useri.txt";
        String pathUseriSer = ApplicationContext.getPROPERTIES().getProperty("file.useri");
        InMemoryRepository<Long, User> useriRepo = new InMemoryRepository<>(new UserValidator());
        Parser<User> userParser = new UserParser();
        copyFromTxt(useriRepo, pathUseriTxt, userParser);
        FileRepository<Long, User> useriFileRepo = new FileRepository<>(new UserValidator(), pathUseriSer);
        useriRepo.findAll().forEach(useriFileRepo::save);
    }

    static void copyPrieteniiFromTxtToSer(){
        String pathPrieteniiTxt = "./data/prietenii.txt";
        String pathPrieteniiSer = ApplicationContext.getPROPERTIES().getProperty("file.prietenii");
        InMemoryRepository<Long, Prietenie> prieteniiRepo = new InMemoryRepository<>(new PrietenieValidator());
        Parser<Prietenie> prietenieParser = new PrietenieParser();
        copyFromTxt(prieteniiRepo, pathPrieteniiTxt, prietenieParser);
        FileRepository<Long, Prietenie> prieteniiFileRepo = new FileRepository<>(new PrietenieValidator(), pathPrieteniiSer);
        prieteniiRepo.findAll().forEach(prieteniiFileRepo::save);
    }

    static void copyUsersFromTxtToDB(){
        String pathUseriTxt = "./data/useri.txt";
        InMemoryRepository<Long, User> useriRepo = new InMemoryRepository<>(new UserValidator());
        Parser<User> userParser = new UserParser();
        copyFromTxt(useriRepo, pathUseriTxt, userParser);

        Properties props = ApplicationContext.getPROPERTIES();
        UserRepoDB useriDBRepo = new UserRepoDB(new UserValidator(), props.getProperty("db.url"),
                props.getProperty("db.username"), props.getProperty("db.password"));
        useriDBRepo.clear();
        assert useriDBRepo.findAll().size() == 0;
        useriRepo.findAll().forEach(user -> {
            try {
                useriDBRepo.save(user);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    static void copyPrieteniiFromTxtToDB(){
        String pathPrieteniiTxt = "./data/prietenii.txt";
        InMemoryRepository<Long, Prietenie> prieteniiRepo = new InMemoryRepository<>(new PrietenieValidator());
        Parser<Prietenie> prietenieParser = new PrietenieParser();
        copyFromTxt(prieteniiRepo, pathPrieteniiTxt, prietenieParser);

        Properties props = ApplicationContext.getPROPERTIES();
        PrietenieRepoDB prietenieRepoDB = new PrietenieRepoDB(new PrietenieValidator(), props.getProperty("db.url"),
                props.getProperty("db.username"), props.getProperty("db.password"));
        prietenieRepoDB.clear();
        assert prietenieRepoDB.findAll().size() == 0;
        prieteniiRepo.findAll().forEach(prietenie -> {
            try {
                prietenieRepoDB.save(prietenie);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void main(String[] args) {
        copyUsersFromTxtToDB();
        copyPrieteniiFromTxtToDB();
    }
}
