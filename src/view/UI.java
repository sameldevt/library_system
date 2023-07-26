package view;

import controller.LibraryManagement;
import controller.UserManagement;
import controller.Verification;
import model.entities.*;
import model.enums.Availability;
import model.enums.Language;
import model.exceptions.LibraryException;
import model.exceptions.UserException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static controller.LibraryManagement.books;
import static controller.UserManagement.users;

public class UI {

    private static final Random random = new Random();
    private static final Scanner sc = new Scanner(System.in);
    private static List<Borrow> borrows;
    private static final String BOOK_DB_PATH = "src/model/resources/books.csv";

    public static boolean showOptions(String user) {
        if(Objects.equals(user, "GUEST")){
            System.out.println("1. Search book");
            System.out.println("2. See book list");
            System.out.println("3. Exit");
            int option = sc.nextInt();
            switch (option) {
                case 1 -> searchBook();
                case 2 -> LibraryManagement.printBookList();
                default -> throw new LibraryException("Invalid option");
            }
            return true;
        }
        if(!Objects.equals(user, "ADMIN") && Objects.equals(user, "GUEST")){
            System.out.println("1. Search book");
            System.out.println("2. Borrow book");
            System.out.println("3. Return book");
            System.out.println("4. See book list");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int option = sc.nextInt();
            switch (option) {
                case 1 -> searchBook();
                case 2 -> borrowBook(user);
                case 3 -> returnBook(user);
                case 4 -> LibraryManagement.printBookList();
                default -> throw new LibraryException("Invalid option");
            }
            return true;
        }
        if(user.equals("ADMIN")){
            System.out.println("1. Register Book");
            System.out.println("2. Register Author");
            System.out.println("3. Register Publisher");
            System.out.println("4. Search book");
            System.out.println("5. Borrow book");
            System.out.println("6. Return book");
            System.out.println("7. See book list");
            System.out.println("8. Exit");
            System.out.print("Choose an option: ");
            int option = sc.nextInt();
            switch (option) {
                case 1 -> registerBook();
                case 2 -> registerAuthor();
                case 3 -> registerPublisher();
                case 4 -> searchBook();
                case 5 -> borrowBook(user);
                case 6 -> returnBook(user);
                case 7 -> printBookList();
                case 8 -> exit();
                default -> System.out.println("PLACEHOLDER");
            }
            return true;
        }
        return false;
    }

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


    public static void borrowBook(String userName) {
        System.out.println("======BORROW BOOK======");
        LibraryManagement.printShortBookList();

        System.out.print("Enter book id to borrow: ");
        int bookId = sc.nextInt();

        System.out.print("How many days? ");
        int period = sc.nextInt();

        User localUser = null;
        Book localBook = null;

        for(User user : users) {
            if(Objects.equals(user.getName(), userName)){
                localUser = user;
            }
        }
        for(Book book : books){
            if(book.getId() == bookId){
                localBook = book;
            }
        }
        LibraryManagement.borrowBook(localBook, period, localUser);
    }

    public static void registerBook(){
        System.out.println("======REGISTER BOOK======");
        System.out.println("Enter author data: ");
        System.out.print("Name: ");
        String authorName = sc.nextLine();
        LibraryManagement.generateId();
        Verification.verifyAuthorId();
        LibraryManagement.registerAuthor(new Author(authorName));

        System.out.println("Enter publisher data: ");
        System.out.print("Name: ");
        String publisherName = sc.nextLine();
        System.out.print("Email: ");
        String publisherEmail = sc.nextLine();

        libraryManagement.registerPublisher(publisherName, publisherEmail);

        System.out.println("Enter book title: ");
        String bookTitle = sc.nextLine();
        System.out.println("Enter book language: ");
        System.out.println("Available languages: PORTUGUESE, ENGLISH, DEUTSCH, JAPANESE,");
        String bookLanguage = sc.nextLine();
        System.out.println("Enter number of pages: ");
        int bookPageCount = sc.nextInt();

        libraryManagement.registerBook(
                bookTitle,
                authorName,
                publisherName,
                Language.valueOf(bookLanguage.toUpperCase()),
                bookPageCount
        );
    }
    public static void returnBook(String userName){
        System.out.println("======RETURN BOOK======");
        System.out.print("Enter book id to return: ");
        int bookId = sc.nextInt();
        User localUser = null;
        Borrow localBorrow = null;

        for(User user : users){
            if(user.getName() == userName){
                localUser = user;
            }
        }
        for(Borrow borrow : localUser.getBorrowList()) {
            if(borrow.getBookId() == bookId){
                localBorrow = borrow;
            }
        }

        LibraryManagement.returnBook(localBorrow, localUser);
    }

    public void registerBook(){
        System.out.println("======= BOOK REGISTER ======");
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
        System.out.println("======= AUTHOR REGISTER ======");
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

    public static void registerPublisher(){
        System.out.println("======= PUBLISHER REGISTER ======");
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

    public static void searchBook(){
        sc.nextLine();
        System.out.println("======SEARCH BOOK======");
        System.out.println("Enter a book description to search (TITLE, AUTHOR, PUBLISHER, AVAILABILITY, BOOK ID): ");
        String value = sc.nextLine();

        LibraryManagement.searchBook(value);
    }

}
