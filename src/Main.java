package library;

import library.services.LibraryService;

public class Main {
    public static void main(String[] args) {
        LibraryService libraryService = new LibraryService();
        libraryService.displayMenu();
    }
}