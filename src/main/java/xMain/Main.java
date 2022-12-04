package xMain;

import controller.Controller;
import ui.console.UIConsole;

public class Main {
    /**
     * Functia main
     * @param args - argumentele
     */
    public static void main(String[] args) {
        Controller controller = new Controller();
        UIConsole uiConsole = new UIConsole(controller);
        uiConsole.run();
    }
}