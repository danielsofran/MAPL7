package ui.console;

import controller.DataController;
import domain.parser.IdParser;
import domain.parser.Parser;
import ui.UI;

import java.util.Scanner;

public class UIConsole implements UI {

    private final UIPrietenii uiPrietenii;
    private final UIUseri uiUseri;
    private final Scanner scanner;

    /**
     * Constructor
     * @param controller - controller-ul, contine service-urile pentru entitati
     */
    public UIConsole(DataController controller){
        scanner = new Scanner(System.in);
        Parser<Long> idParser = new IdParser();
        uiPrietenii = new UIPrietenii(controller, scanner);
        uiUseri = new UIUseri(controller, scanner);
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
