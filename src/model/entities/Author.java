package model.entities;

public class Author{

    private Integer id;
    private String name;

    public Author(String name){
        this.name = name;
    }

    public Author(Integer id, String name){
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
