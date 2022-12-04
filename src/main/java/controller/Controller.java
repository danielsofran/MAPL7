package controller;

import config.ApplicationContext;
import domain.Prietenie;
import domain.User;
import domain.parser.IdParser;
import domain.parser.Parser;
import domain.validation.PrietenieValidator;
import domain.validation.UserValidator;
import domain.validation.Validator;
import repo.PrietenieRepoDB;
import repo.Repository;
import repo.UserRepoDB;
import service.ServicePrietenii;
import service.ServiceUser;

public class Controller {
    private final ServiceUser userService;
    private final ServicePrietenii prietenieService;

    private final Repository<Long, User> repoUser;
    private final Repository<Long, Prietenie> repoPrietenii;

    /**
     * Constructorul clasei Service
     * initializeaza parserele, validatoarele, repository-urile, graful si serviciile
     */
    public Controller() {
        Parser<Long> idParser = new IdParser();
        Validator<User> validatorUser = new UserValidator();
        Validator<Prietenie> validatorPrietenie = new PrietenieValidator();
//        repoUser = new InMemoryRepository<>(validatorUser);
//        repoPrietenii = new InMemoryRepository<>(validatorPrietenie);
//        String useriFile = ApplicationContext.getPROPERTIES().getProperty("file.useri");
//        String prieteniiFile = ApplicationContext.getPROPERTIES().getProperty("file.prietenii");
//        repoUser = new FileRepository<>(validatorUser, useriFile);
//        repoPrietenii = new FileRepository<>(validatorPrietenie, prieteniiFile);
        String url = ApplicationContext.getPROPERTIES().getProperty("db.url");
        String user = ApplicationContext.getPROPERTIES().getProperty("db.username");
        String pwd = ApplicationContext.getPROPERTIES().getProperty("db.password");
        repoUser = new UserRepoDB(validatorUser, url, user, pwd);
        repoPrietenii = new PrietenieRepoDB(validatorPrietenie, url, user, pwd);
        userService = new ServiceUser(repoUser, repoPrietenii);
        prietenieService = new ServicePrietenii(repoPrietenii, repoUser);
    }

    /**
     * @return serviciul de user
     */
    public ServiceUser getServiceUser(){
        return userService;
    }

    /**
     * @return serviciul de prietenie
     */
    public ServicePrietenii getServicePrietenii(){
        return prietenieService;
    }

    /**
     * sterge toate datele stocate din repository-uri
     */
    public void clear(){
        repoUser.clear();
        repoPrietenii.clear();
    }
}
