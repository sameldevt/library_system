import model.exceptions.LibraryException;
import view.UI;

import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            String user = "";
            System.out.println("======LIBRARY SYSTEM======");
            do {
                System.out.println("1. Login\n2. Register\n");
                System.out.print("Choose an option: ");
                int loginOption = sc.nextInt();
                UI.clearScreen();

                switch (loginOption) {
                    case 1 -> user = UI.loginUser();
                    case 2 -> UI.registerUser();
                }
            } while (Objects.equals(user, ""));

            System.out.printf("Logged as %s\n", user);

            while (UI.showOptions(user)) {
                UI.clearScreen();
                System.out.printf("Logged as %s\n", user);
                UI.showOptions(user);
            }
        } catch (LibraryException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (InputMismatchException e) {
            e.printStackTrace();
        }
    }
}