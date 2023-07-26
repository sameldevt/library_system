package controller;

import model.entities.*;
import model.enums.Availability;
import model.exceptions.LibraryException;
import model.exceptions.UserException;

import java.io.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class LibraryManagement {

    public static final Random random = new Random();
    public static final List<Book> books = new ArrayList<>();
    public static final List<Author> authors = new ArrayList<>();
    public static final List<Publisher> publishers = new ArrayList<>();

    public static final String BOOK_DB_PATH = "model/resources/books.csv";
    public static final String AUTHOR_DB_PATH = "model/resources/authors.csv";
    public static final String PUBLISHER_DB_PATH = "model/resources/publishers.csv";
    public static final String BORROWS_DB_PATH = "model/resources/borrows.csv";
    protected static final DateTimeFormatter DATE_PATTERN = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static int generateBookId(String path){
        Random random = new Random();

        // generates a new random book id
        int generatedId = random.nextInt(1000, 3001);

        // verifies the generated book id and generates another one if it already exists
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(path))){
            String lineInfo = bufferedReader.readLine();

            while(lineInfo != null) {
                String[] fields = lineInfo.split(",");
                if(Objects.equals(fields[0], String.valueOf(generatedId))){
                    generatedId = random.nextInt(1000, 3001);
                }

                lineInfo = bufferedReader.readLine();
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return generatedId;
    }
    public static void deleteBook(Book book){
        if(!books.contains(book)){
            throw new LibraryException("Book not registered!");
        }
        DataBaseManagement.clearDataBaseLine(book.getId(), BOOK_DB_PATH);
        books.remove(book);
    }

    public static void registerBook(Book book) {
        if(books.contains(book)){
            throw new LibraryException("Book already registered!");
        }
        books.add(book);
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(BOOK_DB_PATH, true))) {
            bufferedWriter.write(
                    book.getId() + "," + book.getTitle().toUpperCase() + "," + book.getAuthor().getName().toUpperCase() + "," +
                            book.getPublisher().getName().toUpperCase() + "," + book.getLanguage() + "," + book.getAvailability() + "\n"
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void registerAuthor(Author author) {
        if(authors.contains(author)){
            throw new LibraryException("Author already registered!");
        }
        authors.add(author);
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(AUTHOR_DB_PATH, true))) {
            bufferedWriter.write(
                    author.getId() + "," + author.getName().toUpperCase() + "\n"
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void registerPublisher(Publisher publisher) {
        if(publishers.contains(publisher)){
            throw new LibraryException("Publisher already registered!");
        }
        publishers.add(publisher);
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(PUBLISHER_DB_PATH, true))) {
            bufferedWriter.write(
                    publisher.getId() + "," + publisher.getName().toUpperCase() + "\n"
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static boolean borrowBook(Book book, int period, User user) {
        if(!Verification.verifyBook(book)){
            throw new LibraryException("Invalid book");
        }
        int borrowId = random.nextInt(100, 201);
        
        while(Verification.verifyBorrowsId(borrowId)){
            borrowId = random.nextInt(100, 201);
        }
        LocalDate borrowDate = LocalDate.now();
        
        Borrow borrow = new Borrow(borrowId, book.getId(), borrowDate, period);
        user.registerBorrow(borrow);

        System.out.println("======BOOK BORROWED======");
        System.out.println("Book loan report: ");
        System.out.println(
                "Borrow id: " + borrowId +
                "\nBook: " + book.getTitle() +
                "\nUser: " + user.getName() +
                "\nBorrow date: " + borrowDate.format(DATE_PATTERN) +
                "\nPeriod: " + period);

        DataBaseManagement.clearDataBaseLine(book.getId(), BOOK_DB_PATH);

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(BOOK_DB_PATH, true))) {
            bufferedWriter.write(
                    book.getId() + "," + book.getTitle().toUpperCase() + "," +
                            book.getAuthor().getName().toUpperCase() + "," +
                            book.getPublisher().getName().toUpperCase() + "," + book.getLanguage() + "," +
                            Availability.UNAVAILABLE + "\n"
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(BORROWS_DB_PATH, true))) {
            bufferedWriter.write(
                    borrowId + "," + book.getId() + "," + borrowDate.format(DATE_PATTERN) + "," + period + "\n"
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean returnBook(final Borrow BORROW, final User USER) {
        if(!Verification.verifyUser(USER)){
            throw new UserException("Invalid user");
        }
        if(!Verification.verifyBorrow(BORROW, USER)){
            throw new LibraryException("Invalid borrow");
        }
        DataBaseManagement.clearDataBaseLine(BORROW.getBorrowId(), BORROWS_DB_PATH);
        USER.removeBorrow(BORROW);

        final LocalDate RETURN_DATE = LocalDate.now();
        final LocalDate BORROW_DATE = BORROW.getBorrowDate();
        final Period PERIOD = Period.between(BORROW_DATE, RETURN_DATE);

        System.out.println("======BOOK RETURNED======");
        if(RETURN_DATE.isAfter(BORROW_DATE)){
            System.out.println("!PENALTY REPORT!");
            System.out.println("DELAY: " + PERIOD);
        }
        return true;
    }

    public static void searchBook(String value){
        // searches the database for any value which is equals to input value
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(BOOK_DB_PATH))){
            String bookInfo = bufferedReader.readLine();
            while(bookInfo != null) {
                String[] fields = bookInfo.split(",");

                for(String field : fields){
                    if(value.toUpperCase().equals(field)){
                        System.out.println(bookInfo);
                    }
                }
                bookInfo = bufferedReader.readLine();
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        catch (LibraryException e){
            System.out.println("Error: " + e.getMessage());
        }
        throw new LibraryException("Book not found");
    }

    public static void printBookList(){
        for(Book book : books){
            System.out.println(book);
        }
    }
    public static void printShortBookList(){
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(BOOK_DB_PATH))){
            String bookInfo = bufferedReader.readLine();
            while(bookInfo != null) {
                String[] fields = bookInfo.split(",");
                String bookId = fields[0];
                String title = fields[1];
                String availability = fields[4];

                System.out.println(bookId + " - " + title + " - " + availability);

                bookInfo = bufferedReader.readLine();
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
