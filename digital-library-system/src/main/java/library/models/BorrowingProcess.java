package library.models;

import java.time.LocalDate;

public class BorrowingProcess {
    private Book book;
    private Borrower borrower;
    private LocalDate borrowDate;
    private LocalDate returnDate;

    public BorrowingProcess(Book book, Borrower borrower, LocalDate borrowDate, LocalDate returnDate) {
        this.book = book;
        this.borrower = borrower;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
    }

    // Getters
    public Book getBook() { return book; }
    public Borrower getBorrower() { return borrower; }
    public LocalDate getBorrowDate() { return borrowDate; }
    public LocalDate getReturnDate() { return returnDate; }

    @Override
    public String toString() {
        return "BorrowingProcess{" +
                "book=" + book +
                ", borrower=" + borrower +
                ", borrowDate=" + borrowDate +
                ", returnDate=" + returnDate +
                '}';
    }
}