package view;

import controller.LibraryManagement;
import controller.UserManagement;
import model.entities.Author;
import model.entities.Book;
import model.entities.Publisher;
import model.entities.User;
import model.enums.Availability;
import model.enums.Language;

import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

public class UI {

    private static final Random random = new Random();
    private static final Scanner sc = new Scanner(System.in);

    public static String loginUser(){
        System.out.println("======= USER LOGIN ======");
        System.out.print("Enter your email: ");
        final String email = sc.nextLine();
        if(Objects.equals(email, "")){
            return "GUEST";
        }
        System.out.print("Enter your password: ");
        final int password = sc.nextInt();

        return UserManagement.loginUser(email, password);
    }
    public void registerUser(){
        System.out.println("======= USER REGISTER ======");
        int id = random.nextInt(0, 99);
        System.out.print("Enter a name: ");
        final String name = sc.nextLine();
        System.out.print("Enter a email: ");
        final String email = sc.nextLine();
        System.out.print("Enter a password: ");
        final int password = sc.nextInt();

        while(UserManagement.verifyId(id)){
            id = random.nextInt(0, 99);
        }

        User user = new User(id, name, email, password);

        UserManagement.registerUser(user);
    }

    public void registerBook(){
        System.out.println("======= BOOK REGISTER ======");
        System.out.print("Enter the title: ");
        final String title = sc.nextLine();
        int bookId = random.nextInt(10000, 20000);
        while(LibraryManagement.verifyId(bookId)){
            bookId = random.nextInt(10000, 20000);
        }
        System.out.print("Enter the author: ");
        final String authorName = sc.nextLine();
        int authorId = random.nextInt(1000, 2000);
        while(LibraryManagement.verifyId(authorId)){
            authorId = random.nextInt(1000, 2000);
        }
        System.out.print("Enter the publisher: ");
        final String publisherName = sc.nextLine();
        int publisherId = random.nextInt(2001, 3000);
        while(LibraryManagement.verifyId(publisherId)){
            publisherId = random.nextInt(2001, 3000);
        }
        System.out.print("Enter the language: ");
        final Language language = Language.valueOf(sc.nextLine());

        Author author = new Author(authorId, authorName);
        Publisher publisher = new Publisher(publisherId, publisherName);

        Book book = new Book(bookId, title, author, publisher, language, Availability.AVAILABLE);

        LibraryManagement.registerBook(book);
    }

    public void registerAuthor(){
        System.out.println("======= BOOK AUTHOR ======");
        System.out.print("Enter author name: ");
        final String authorName = sc.nextLine();
        int authorId = random.nextInt(1000, 2000);
        while(LibraryManagement.verifyId(authorId)){
            authorId = random.nextInt(1000, 2000);
        }
        Author author = new Author(authorId, authorName);
        LibraryManagement.registerAuthor(author);
    }

    public void registerPublisher(){
        System.out.println("======= BOOK AUTHOR ======");
        System.out.print("Enter publisher name: ");
        final String publisherName = sc.nextLine();
        int publisherId = random.nextInt(2001, 3000);
        while(LibraryManagement.verifyId(publisherId)){
            publisherId = random.nextInt(2001, 3000);
        }

        Publisher publisher = new Publisher(publisherId, publisherName);
        LibraryManagement.registerPublisher(publisher);
    }

}
