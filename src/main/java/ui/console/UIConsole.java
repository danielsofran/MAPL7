package ui.console;

import controller.Controller;
import domain.parser.IdParser;
import domain.parser.Parser;
import ui.UI;

import java.util.Scanner;

public class UIConsole implements UI {

    private UIPrietenii uiPrietenii;
    private UIUseri uiUseri;
    private Controller controller;
    private Scanner scanner;
    private Parser<Long> idParser;

    /**
     * Constructor
     * @param controller - controller-ul, contine service-urile pentru entitati
     */
    public UIConsole(Controller controller){
        this.controller = controller;
        scanner = new Scanner(System.in);
        idParser = new IdParser();
        uiPrietenii = new UIPrietenii(controller, scanner, idParser);
        uiUseri = new UIUseri(controller, scanner, idParser);
    }

    /**
     * Ruleaza interfata grafica
     */
    public void run() {
        while (true){
            String[] args = scanner.nextLine().split(" ");
            execute(args);
        }
    }

    /**
     * Executa comanda
     * @param args - argumentele comenzii
     */
    private void execute(String[] args) {
        switch (args[0]){
            case "prietenii":
                uiPrietenii.execute(args);
                break;
            case "useri":
                uiUseri.execute(args);
                break;
            default:
                System.out.println("Invalid command! Try one of the following:");
                System.out.println("prietenii, useri");
        }
    }
}
