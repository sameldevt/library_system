package controller;

import model.entities.User;
import model.exceptions.UserException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class UserManagement {

    protected static final String USER_DB_PATH = "src/model/resources/users.csv";
    private static final Random random = new Random();
    protected static final List<User> users = new ArrayList<>();

    public static boolean loginUser(User user){
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(USER_DB_PATH))){
            String line = bufferedReader.readLine();
            while(line != null){
                final String[] fields = line.split(",");
                final String userName = fields[1];
                final int userPassword = Integer.parseInt(fields[3]);

                if(users.contains(user) && user.verifyPassword(userPassword)){
                    return true;
                }
                line = bufferedReader.readLine();
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return false;
    }

    public static boolean registerUser(User user){
        if(Verification.verifyUserRegister(user)){
            throw new UserException("User already registered");
        }
        users.add(user);
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(USER_DB_PATH, true))){
            bufferedWriter.write(
                    user.getId() + "," + user.getName().toUpperCase() + "," +
                        user.getEmail().toUpperCase() + "," + user.getPassword() + "\n"
            );
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return true;
    }

}
