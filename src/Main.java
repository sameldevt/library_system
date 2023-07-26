import model.exceptions.LibraryException;
import view.UI;

import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Scanner;

import controller.DataBaseManagement;

public class Main {
    public static void main(String[] args) {
        String user = "";
        try (Scanner sc = new Scanner(System.in)) {
            DataBaseManagement.loadBookDataBase();
            System.out.println("======LIBRARY SYSTEM======");
            do {
                System.out.println("1. Login\n2. Register\n");
                System.out.print("Choose an option: ");
                int loginOption = sc.nextInt();

                switch (loginOption) {
                    case 1 -> user = UI.loginUser();
                    case 2 -> UI.registerUser();
                }
            } while (Objects.equals(user, ""));

            System.out.printf("Logged as %s\n", user);

            while (UI.showOptions(user)) {
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