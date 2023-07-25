package controller;

import model.entities.*;
import model.enums.Availability;
import model.enums.Language;
import model.exceptions.LibraryException;
import model.exceptions.UserException;

import java.io.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static controller.UserManagement.users;

public class LibraryManagement {

    private static final Random random = new Random();
    private static final List<Book> books = new ArrayList<>();
    private static final List<Author> authors = new ArrayList<>();
    private static final List<Publisher> publishers = new ArrayList<>();

    private static final String BOOK_DB_PATH = "src/model/resources/books.csv";
    private static final String AUTHOR_DB_PATH = "src/model/resources/authors.csv";
    private static final String PUBLISHER_DB_PATH = "src/model/resources/publishers.csv";
    private static final String BORROWS_DB_PATH = "src/model/resources/borrows.csv";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private static void clearLine(int id, String path) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path))) {
            String lineInfo = bufferedReader.readLine();
            while (lineInfo != null) {
                String[] fields = lineInfo.split(",");
                if (Integer.parseInt(fields[0]) == id) {
                    try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path))) {
                        bufferedWriter.write("");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                lineInfo = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int loadAuthorDataBase(final String AUTHOR_NAME){
        try(BufferedReader authorReader = new BufferedReader(new FileReader(AUTHOR_DB_PATH))){
            final String LINE = authorReader.readLine();
            while(LINE != null){
                final String[] FIELDS = LINE.split(",");
                if(FIELDS[1] == AUTHOR_NAME){
                    return Integer.parseInt(FIELDS[0]);
                }
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
        throw new LibraryException("PLACEHOLDER");
    }

    public static int loadPublisherDataBase(final String PUBLISHER_NAME){
        try(BufferedReader authorReader = new BufferedReader(new FileReader(PUBLISHER_DB_PATH))){
            final String LINE = authorReader.readLine();
            while(LINE != null){
                final String[] FIELDS = LINE.split(",");
                if(FIELDS[1] == PUBLISHER_NAME){
                    return Integer.parseInt(FIELDS[0]);
                }
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
        throw new LibraryException("PLACEHOLDER");
    }
    public static void loadBookDataBase(){
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(BOOK_DB_PATH))) {
            String line = bufferedReader.readLine();
            while (line != null) {
                final String[] FIELDS = line.split(",");
                final int BOOK_ID = Integer.parseInt(FIELDS[0]);
                final String BOOK_TITLE = FIELDS[1];

                final String BOOK_AUTHOR_NAME = FIELDS[2];
                final int BOOK_AUTHOR_ID =  loadAuthorDataBase(BOOK_AUTHOR_NAME);
                
                final String BOOK_PUBLISHER_NAME = FIELDS[3];
                final int BOOK_PUBLISHER_ID = loadPublisherDataBase(BOOK_PUBLISHER_NAME);
                
                final Language BOOK_LANGUAGE = Language.valueOf(FIELDS[4]);
                final Availability BOOK_AVAILABILITY = Availability.valueOf(FIELDS[5]);

                final Publisher BOOK_PUBLISHER = new Publisher(BOOK_PUBLISHER_ID, BOOK_PUBLISHER_NAME);
                final Author BOOK_AUTHOR = new Author(BOOK_AUTHOR_ID, BOOK_AUTHOR_NAME);
                final Book BOOK = new Book(BOOK_ID, BOOK_TITLE, BOOK_AUTHOR, BOOK_PUBLISHER, BOOK_LANGUAGE, BOOK_AVAILABILITY);

                books.add(BOOK);
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Book verifyBookId(int bookId){
        for(Book book : books){
            if(book.getId() == bookId){
                return book;
            }
        }
        throw new LibraryException("Book not found");
    }

    public static void deleteBook(Book book){
        if(!books.contains(book)){
            throw new LibraryException("Book not registered!");
        }
        clearLine(book.getId(), BOOK_DB_PATH);
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
    
    public boolean borrowBook(Book book, int period, User user) {
        if(!verifyBook(book)){
            throw new LibraryException("Invalid book");
        }
        int borrowId = random.nextInt(100, 201);
        
        while(verifyId(borrowId)){
            borrowId = random.nextInt(100, 201);
        }
        LocalDateTime borrowDate = LocalDateTime.now();
        
        Borrow borrow = new Borrow(borrowId, book.getId(), borrowDate, period);
        user.registerBorrow(borrow);

        System.out.println("======BOOK BORROWED======");
        System.out.println("Book loan report: ");
        System.out.println(
                "Borrow id: " + borrowId +
                "\nBook: " + book.getTitle() +
                "\nUser: " + user.getName() +
                "\nBorrow date: " + borrowDate.format(DATE_TIME_FORMATTER) +
                "\nPeriod: " + period);

        clearLine(book.getId(), BOOK_DB_PATH);

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
                    borrowId + "," + book.getId() + "," + borrowDate.format(DATE_TIME_FORMATTER) + "," + period + "\n"
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean returnBook(final Borrow BORROW, final User USER) {
        if(!verifyUser(USER)){
            throw new UserException("Invalid user");
        }
        if(!verifyBorrow(BORROW, USER)){
            throw new LibraryException("Invalid borrow");
        }
        clearLine(BORROW.getBorrowId(), BORROWS_DB_PATH);
        USER.removeBorrow(BORROW);

        final LocalDateTime RETURN_DATE = LocalDateTime.now();
        final LocalDateTime BORROW_DATE = BORROW.getBorrowDate();
        System.out.println("======BOOK RETURNED======");
        if(RETURN_DATE.isAfter(BORROW_DATE)){
            System.out.println("!PENALTY REPORT!");
            System.out.println("DELAY: " + PERIOD.between(BORROW.getBorrowDate().getDayOfMonth(), returnDate.getDayOfMonth());
        }
        return true;
    }

    public boolean verifyUser(User userToVerify){
        for (User user : users) {
            if(user.getId() == userToVerify.getId()){
                return true;
            }
        }
        return false;
    }

    public boolean verifyBorrow(Borrow borrow, User user){
        return user.getBorrowList().contains(borrow);
    }

    public boolean verifyBook(Book bookToVerify){
        for (Book book : books) {
            if(book.getId() == bookToVerify.getId() && book.getAvailability() == bookToVerify.getAvailability()){
                return true;
            }
        }
        return false;
    }

    public static boolean verifyId(int id) {
        if (id >= 10000 && id <= 20000) {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(BOOK_DB_PATH))) {
                String line = bufferedReader.readLine();
                while (line != null) {
                    String[] fields = line.split(",");
                    if (Integer.parseInt(fields[0]) == id) {
                        return true;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        } 
        else if (id >= 1000 && id <= 2000) {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(AUTHOR_DB_PATH))) {
                String line = bufferedReader.readLine();
                while (line != null) {
                    String[] fields = line.split(",");
                    if (Integer.parseInt(fields[0]) == id) {
                        return true;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        } 
        else if (id >= 2001 && id <= 3000) {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(PUBLISHER_DB_PATH))) {
                String line = bufferedReader.readLine();
                while (line != null) {
                    String[] fields = line.split(",");
                    if (Integer.parseInt(fields[0]) == id) {
                        return true;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
        else if(id >= 100 && id <= 201){
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(BORROWS_DB_PATH))) {
                String line = bufferedReader.readLine();
                while (line != null) {
                    String[] fields = line.split(",");
                    if (Integer.parseInt(fields[0]) == id) {
                        return true;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
        throw new LibraryException("PLACEHOLDER");
    }
}
