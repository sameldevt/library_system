package view;

import controller.LibraryManagement;
import controller.UserManagement;
import controller.Verification;
import model.entities.Author;
import model.entities.Book;
import model.entities.Publisher;
import model.entities.User;
import model.enums.Availability;
import model.enums.Language;
import model.exceptions.UserException;

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
            System.out.println("LOGGED AS GUEST");
            return "GUEST";
        }
        if(Objects.equals(email, "ADMIN")){
            return "ADMIN";
        }
        System.out.print("Enter your password: ");
        final int password = sc.nextInt();

        User user = new User(email, password);

        if(!UserManagement.loginUser(user)){
            throw new UserException("Invalid email or password");
        }
        return ""+user.getName();
    }

    public static boolean registerUser(){
        System.out.println("======= USER REGISTER ======");
        int id = random.nextInt(0, 99);
        System.out.print("Enter a name: ");
        final String name = sc.nextLine();
        System.out.print("Enter a email: ");
        final String email = sc.nextLine();
        if(Verification.verifyUserEmail(email)){
            throw new UserException("User email already registered");
        }
        System.out.print("Enter a password: ");
        final int password = sc.nextInt();

        while(Verification.verifyUserId(id)){
            id = random.nextInt(0, 99);
        }

        User user = new User(id, name, email, password);

        return UserManagement.registerUser(user);
    }

    public void registerBook(){
        int bookId = random.nextInt(10000, 20000);
        int authorId = random.nextInt(1000, 2000);
        int publisherId = random.nextInt(2001, 3000);
        System.out.println("======= BOOK REGISTER ======");
        System.out.print("Enter the title: ");
        final String title = sc.nextLine();
        while(Verification.verifyBookId(bookId)){
            bookId = random.nextInt(10000, 20000);
        }
        System.out.print("Enter the author: ");
        final String authorName = sc.nextLine();
        while(Verification.verifyAuthorId(authorId)){
            authorId = random.nextInt(1000, 2000);
        }
        System.out.print("Enter the publisher: ");
        final String publisherName = sc.nextLine();
        while(Verification.verifyPublisherId(publisherId)){
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
        int authorId = random.nextInt(1000, 2000);
        System.out.println("======= BOOK AUTHOR ======");
        System.out.print("Enter author name: ");
        final String authorName = sc.nextLine();
        while(Verification.verifyAuthorId(authorId)){
            authorId = random.nextInt(1000, 2000);
        }
        Author author = new Author(authorId, authorName);
        LibraryManagement.registerAuthor(author);
    }

    public void registerPublisher(){
        int publisherId = random.nextInt(2001, 3000);
        System.out.println("======= BOOK AUTHOR ======");
        System.out.print("Enter publisher name: ");
        final String publisherName = sc.nextLine();
        while(Verification.verifyPublisherId(publisherId)){
            publisherId = random.nextInt(2001, 3000);
        }

        Publisher publisher = new Publisher(publisherId, publisherName);
        LibraryManagement.registerPublisher(publisher);
    }

}
