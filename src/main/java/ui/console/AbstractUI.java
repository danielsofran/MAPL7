package ui.console;

import controller.Controller;

import java.util.Scanner;

public abstract class AbstractUI {
    protected final Scanner scanner;
    protected final Controller controller;

    /**
     * Constructor pentru interfata user-ilor, seteaza service-ul si scanner-ul
     *
     * @param controller - controller-ul
     * @param scanner    - scannerul
     */
    public AbstractUI(Controller controller, Scanner scanner) {
        this.controller = controller;
        this.scanner = scanner;
    }

    /**
     * citeste de la tastatura si parseaza un id
     * @param msg - mesajul afisat
     * @return - id-ul parsat
     */
    protected Long readId(String msg) {
        if(msg == null) msg = "Introduceti id-ul";
        System.out.print(msg + " ");
        return scanner.nextLong();
    }

    /**
     * Executa comanda asociata argumentelor
     * @param args - argumentele comenzii
     */
    public abstract void execute(String[] args);
}
