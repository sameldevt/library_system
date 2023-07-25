package model.entities;

import java.time.LocalDate;

public class Borrow {

    private Integer bookId;
    private Integer borrowId;
    private LocalDate borrowDate;
    private Integer period;

    public Borrow(Integer borrowId, Integer bookId, LocalDate borrowDate, Integer period){
        this.borrowId = borrowId;
        this.bookId = bookId;
        this.borrowDate = borrowDate;
        this.period = period;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public Integer getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(Integer borrowId) {
        this.borrowId = borrowId;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }
}
