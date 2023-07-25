package controller;

import model.entities.*;
import model.enums.Availability;
import model.exceptions.LibraryException;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static controller.UserManagement.users;

public class LibraryManagement {

    private static final Random random = new Random();
    private static final List<Book> books = new ArrayList<>();
    private static final List<Author> authors = new ArrayList<>();
    private static final List<Publisher> publishers = new ArrayList<>();
    private static final List<Borrow> borrows = new ArrayList<>();

    private static final String BOOK_DB_PATH = "src/model/resources/books.csv";
    private static final String AUTHOR_DB_PATH = "src/model/resources/authors.csv";
    private static final String PUBLISHER_DB_PATH = "src/model/resources/publishers.csv";
    private static final String BORROWS_DB_PATH = "src/model/resources/borrows.csv";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

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
        } else if (id >= 1000 && id <= 2000) {
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
        } else if (id >= 2001 && id <= 3000) {
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
        throw new LibraryException("");
    }

    private void clearLine(int id, String path) {
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

    public static void registerBook(Book book) {
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
        publishers.add(publisher);
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(PUBLISHER_DB_PATH, true))) {
            bufferedWriter.write(
                    publisher.getId() + "," + publisher.getName().toUpperCase() + "\n"
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean borrowBook(int bookId, int period, String userName) {
        for (Book book : books) {
            if (book.getId() == bookId && book.getAvailability() == Availability.AVAILABLE) {
                int borrowId = random.nextInt(100, 201);
                LocalDateTime borrowDate = LocalDateTime.now();
                System.out.println("======BOOK BORROWED======");
                System.out.println("Book loan report: ");
                System.out.println("Book: " + book.getTitle() +
                        "\nUser: " + userName +
                        "\nBorrow date: " + borrowDate.format(DATE_TIME_FORMATTER) +
                        "\nPeriod: " + period);

                Borrow borrow = new Borrow(borrowId, bookId, borrowDate, period);
                for (User user : users) {
                    if (user.getName() == userName) {
                        user.registerBorrowBook(borrow);
                    }
                }

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
        }
        return false;
    }

    public boolean returnBook(int id, String userName) {
        for (Borrow borrow : borrows) {
            if (borrow.getBookId() == id) {

            }
        }
        return false;
    }
}
