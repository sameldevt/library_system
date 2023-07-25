package model.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import model.exceptions.UserException;

public class User {

    private Integer id;
    private String name;
    private String email;
    private Integer password;
    private List<Borrow> borrows = new ArrayList<>();

    public User(String email, Integer password){
        this.email = email;
        this.password = password;
    }

    public User(Integer id, String name, String email, Integer password){
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }


    public void registerBorrow(Borrow borrow){
        borrows.add(borrow);
    }

    public List<Borrow> getBorrowList(){
        return borrows;
    }

    public void removeBorrow(Borrow borrow){
        borrows.remove(borrow);
    }

    public boolean verifyPassword(int password) {
        if(password == this.password){
            return true;
        }
        return false;
    }

    public boolean verifyEmail(String email) {
        if(Objects.equals(email, this.email)){
            return true;
        }
        return false;
    }

    public void changeEmail(String email){
        this.email = email;
    }
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Integer getPassword(){
        return password;
    }
}
