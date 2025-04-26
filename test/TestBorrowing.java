package test;

import src.models.Borrower;
import src.models.PaperBook;

public class TestBorrowing {
    public static void main(String[] args) {
        PaperBook book = new PaperBook("Java Programming", "John Doe", "1234567890");
        Borrower borrower = new Borrower("Asma Benali", "20250001");

        borrower.borrowBook(book);
        borrower.listBorrowedBooks();
        borrower.returnBook(book);
    }
}
