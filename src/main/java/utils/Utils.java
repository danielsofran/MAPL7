package utils;

import exceptii.MyException;

import java.time.format.DateTimeFormatter;

public class Utils {
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    public static final DateTimeFormatter PSQL_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter PSQL_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    /**
     * metoda care executa o actiune si prinde propriile exceptii
     * @param runnable - actiunea care va fi executata
     */
    public static void tryExecute(Runnable runnable) {
        try {
            runnable.run();
            System.out.println("Success!");
        } catch (MyException e) {
            System.err.println(e.getMessage());
        }
    }
}
