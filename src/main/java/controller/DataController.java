package controller;

import config.ApplicationContext;
import domain.Prietenie;
import domain.User;
import domain.validation.PrietenieValidator;
import domain.validation.UserValidator;
import domain.validation.Validator;
import exceptii.ControllerException;
import repo.*;
import service.ServicePrietenii;
import service.ServiceUser;

public class DataController {
    private final ServiceUser userService;
    private final ServicePrietenii prietenieService;

    private final Repository<Long, User> repoUser;
    private final Repository<Long, Prietenie> repoPrietenii;

    /**
     * Constructorul clasei Service
     * initializeaza parserele, validatoarele, repository-urile, graful si serviciile
     */
    public DataController() {
        Validator<User> validatorUser = new UserValidator();
        Validator<Prietenie> validatorPrietenie = new PrietenieValidator();
        String url = ApplicationContext.getPROPERTIES().getProperty("db.url");
        String user = ApplicationContext.getPROPERTIES().getProperty("db.username");
        String pwd = ApplicationContext.getPROPERTIES().getProperty("db.password");
        repoUser = new UserRepoDB(validatorUser, url, user, pwd);
        repoPrietenii = new PrietenieRepoDB(validatorPrietenie, url, user, pwd);
        userService = new ServiceUser(repoUser, repoPrietenii);
        prietenieService = new ServicePrietenii(repoPrietenii, repoUser);
    }

    /**
     *
     * @param persistence - metoda de persistenta a datelor
     */
    public DataController(DataPersistence persistence)
    {
        Validator<User> validatorUser = new UserValidator();
        Validator<Prietenie> validatorPrietenie = new PrietenieValidator();
        switch (persistence){
            case InMemory:
                repoUser = new InMemoryRepository<>(validatorUser);
                repoPrietenii = new InMemoryRepository<>(validatorPrietenie);
            break;
            case InFile:
                String useriFile = ApplicationContext.getPROPERTIES().getProperty("file.useri");
                String prieteniiFile = ApplicationContext.getPROPERTIES().getProperty("file.prietenii");
                repoUser = new FileRepository<>(validatorUser, useriFile);
                repoPrietenii = new FileRepository<>(validatorPrietenie, prieteniiFile);
            break;
            case InDatabase:
                String url = ApplicationContext.getPROPERTIES().getProperty("db.url");
                String user = ApplicationContext.getPROPERTIES().getProperty("db.username");
                String pwd = ApplicationContext.getPROPERTIES().getProperty("db.password");
                repoUser = new UserRepoDB(validatorUser, url, user, pwd);
                repoPrietenii = new PrietenieRepoDB(validatorPrietenie, url, user, pwd);
            break;
            default:
                throw new ControllerException("Unknown data persistence method '"+persistence+"'");
        }
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
