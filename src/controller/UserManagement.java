package controller;

import model.entities.User;
import model.exceptions.UserException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UserManagement {
    private static final String USER_DB_PATH = "src/model/resources/users.csv";
    private static final Random random = new Random();
    protected static final List<User> users = new ArrayList<>();
    public static String loginUser(String email, int password){
        for(User user : users){
            if(user.verifyEmail(email) && user.verifyPassword(password)){
                return user.getName();
            }
        }
        throw new UserException("Invalid email or password");
    }
    public static void registerUser(User user){
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
    }


    public static boolean verifyId(int id){
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(USER_DB_PATH))){
            String line = bufferedReader.readLine();
            while(line != null){
                String[] fields = line.split(",");
                if(Integer.parseInt(fields[0]) == id){
                    return true;
                }
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return false;
    }

}
