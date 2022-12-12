package xMain;

import controller.DataController;
import ui.console.UIConsole;

public class Main {
    /**
     * Functia main
     * @param args - argumentele
     */
    public static void main(String[] args) {
        DataController controller = new DataController();
        UIConsole uiConsole = new UIConsole(controller);
        uiConsole.run();
    }
}