package ui.console;

import controller.DataController;
import domain.User;
import domain.UserDetail;
import domain.UserDetails;
import utils.Utils;

import java.util.Scanner;

public class UIUseri extends AbstractUI {
    /**
     * Constructor pentru interfata user-ilor, seteaza service-ul si scanner-ul
     *
     * @param controller - controller-ul
     * @param scanner    - scannerul
     */
    public UIUseri(DataController controller, Scanner scanner) {
        super(controller, scanner);
    }

    /**
     * Executa comanda asociata argumentelor
     * @param args - argumentele comenzii
     */
    @Override
    public void execute(String[] args) {
        if(args.length == 1){
            System.out.println("Invalid subcommand! Try one of the following:");
            System.out.println("add, remove, update, find, findall");
            return;
        }
        switch (args[1]){
            case "add":
                addUser();
                break;
            case "remove":
                removeUser();
                break;
            case "update":
                updateUser();
                break;
            case "find":
                findUser();
                break;
            case "findall":
                findAll();
                break;
            default:
                System.out.println("Invalid command! Try one of the following:");
                System.out.println("add, remove, update, find, findall");
        }
    }

    /**
     * citeste de la tastatura detaliile asociate unui user
     * @return - un map in care cheia indica campul iar valoarea este valoarea campului indicat
     */
    private UserDetails readDetails(){
        UserDetails details = new UserDetails();
        System.out.print("Introduceti numele user-ului: ");
        details.put(UserDetail.Nume, scanner.nextLine());
        System.out.print("Introduceti emailul user-ului: ");
        details.put(UserDetail.Email, scanner.nextLine());
        System.out.print("Introduceti parola user-ului: ");
        details.put(UserDetail.Password, scanner.nextLine());
        return details;
    }

    /**
     * Afiseaza toti userii
     */
    private void findAll() {
        if(controller.getServiceUser().findAll().isEmpty()){
            System.err.println("Nu exista useri!");
            return;
        }
        Utils.tryExecute(() -> controller.getServiceUser().findAll().forEach(System.out::println));
    }

    /**
     * Afiseaza user-ul cu id-ul dat
     */
    private void findUser() {
        Utils.tryExecute(() -> {
            Long id = readId(null);
            User user = controller.getServiceUser().findOne(id);
            System.out.println(user);
        });
    }

    /**
     * Actualizeaza user-ul cu id-ul dat
     */
    private void updateUser() {
        Utils.tryExecute(() -> {
            Long id = readId("Introduceti id-ul utilizatorului pe care doriti sa il actualizati:");
            UserDetails details = readDetails();
            controller.getServiceUser().update(id, details);
        });
    }

    /**
     * Sterge user-ul cu id-ul dat
     */
    private void removeUser() {
        Utils.tryExecute(() -> {
            Long id = readId("Introduceti id-ul user-ului pe care doriti sa il stergeti: ");
            controller.getServiceUser().remove(id);
        });
    }

    /**
     * Adauga un user
     */
    private void addUser() {
        UserDetails details = readDetails();
        Utils.tryExecute(() -> controller.getServiceUser().add(details));
    }
}
