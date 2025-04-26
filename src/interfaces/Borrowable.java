package library.interfaces;

public interface Borrowable {
    void borrowItem();
    void returnItem();
    boolean isAvailable();
}