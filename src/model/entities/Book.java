package model.entities;

import model.enums.Availability;
import model.enums.Language;

public class Book {

    private Integer id;
    private String title;
    private Author author;
    private Publisher publisher;
    private Language language;
    private Availability availability;

    public Book(){

    }

    public Book(Integer id, String title, Author author, Publisher publisher, Language language, Availability availability){
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.language = language;
        this.availability = availability;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Author getAuthor() {
        return author;
    }

    public Publisher getPublisher() {
        return publisher;
    }


    public Language getLanguage() {
        return language;
    }

    public Availability getAvailability() {
        return availability;
    }

    public void changeAvailability(Availability availability) {
        this.availability = availability;
    }
}
