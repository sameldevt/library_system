package controller;

import model.entities.Author;
import model.entities.Book;
import model.entities.Publisher;
import model.enums.Availability;
import model.enums.Language;
import model.exceptions.LibraryException;

import java.io.*;

import static controller.LibraryManagement.*;

public class DataBaseManagement {

    public static void clearDataBaseLine(int id, String path) {
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
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(AUTHOR_DB_PATH))){
            String line = bufferedReader.readLine();
            while(line != null){
                final String[] FIELDS = line.split(",");
                if(FIELDS[1] == AUTHOR_NAME){
                    return Integer.parseInt(FIELDS[0]);
                }
                line = bufferedReader.readLine();
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
        throw new LibraryException("PLACEHOLDER");
    }

    public static int loadPublisherDataBase(final String PUBLISHER_NAME){
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(PUBLISHER_DB_PATH))){
            String line = bufferedReader.readLine();
            while(line != null){
                final String[] FIELDS = line.split(",");
                if(FIELDS[1] == PUBLISHER_NAME){
                    return Integer.parseInt(FIELDS[0]);
                }
                line = bufferedReader.readLine();
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
}
