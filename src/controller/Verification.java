package controller;

import model.entities.Book;
import model.entities.Borrow;
import model.entities.User;
import model.exceptions.LibraryException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static controller.LibraryManagement.*;
import static controller.UserManagement.USER_DB_PATH;
import static controller.UserManagement.users;

public class Verification {

    public static boolean verifyUserEmail(String email){
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(USER_DB_PATH))){
            String line = bufferedReader.readLine();
            while(line != null){
                String[] fields = line.split(",");
                if(fields[0] == email){
                    return true;
                }
                line = bufferedReader.readLine();
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return false;
    }
    public static boolean verifyUserId(int id){
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(USER_DB_PATH))){
            String line = bufferedReader.readLine();
            while(line != null){
                String[] fields = line.split(",");
                if(Integer.parseInt(fields[2]) == id){
                    return true;
                }
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return false;
    }
    public static boolean verifyUserRegister(User userToVerify){
        return users.contains(userToVerify);
    }
    public static Book verifyBookRegister(int bookId){
        for(Book book : books){
            if(book.getId() == bookId){
                return book;
            }
        }
        throw new LibraryException("Book not found");
    }

    public static boolean verifyUser(User userToVerify){
        for (User user : users) {
            if(user.getId() == userToVerify.getId()){
                return true;
            }
        }
        return false;
    }

    public static boolean verifyBorrow(Borrow borrow, User user){
        return user.getBorrowList().contains(borrow);
    }

    public static boolean verifyBook(Book bookToVerify){
        for (Book book : books) {
            if(book.getId() == bookToVerify.getId() && book.getAvailability() == bookToVerify.getAvailability()){
                return true;
            }
        }
        return false;
    }

    public static boolean verifyAuthorId(int id){
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
    public static boolean verifyPublisherId(int id){
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

    public static boolean verifyBorrowsId(int id){
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

    public static boolean verifyBookId(int id) {
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
}
