package library.services;

import library.models.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class LibraryService {
    private List<Book> books;
    private List<Borrower> borrowers;
    private List<BorrowingProcess> borrowingProcesses;
    private Scanner scanner;

    public LibraryService() {
        this.books = new ArrayList<>();
        this.borrowers = new ArrayList<>();
        this.borrowingProcesses = new ArrayList<>();
        this.scanner = new Scanner(System.in);
    }
    // ======================= الكتب =======================
    public void addBook() {
        System.out.println("\n=== إضافة كتاب جديد ===");
        System.out.print("رقم ISBN: ");
        String ISBN = scanner.nextLine();
        
        if (findBookByISBN(ISBN) != null) {
            System.out.println("!هذا الكتاب مسجل مسبقًا");
            return;
        }

        System.out.print("العنوان: ");
        String title = scanner.nextLine();
        System.out.print("المؤلف: ");
        String author = scanner.nextLine();

        System.out.print("نوع الكتاب (1-ورقي / 2-إلكتروني): ");
        int type = getIntInput();

        if (type == 1) {
            System.out.print("عدد الصفحات: ");
            int pages = getIntInput();
            books.add(new PaperBook(title, author, ISBN, pages));
        } else if (type == 2) {
            System.out.print("صيغة الملف: ");
            String format = scanner.nextLine();
            books.add(new EBook(title, author, ISBN, format));
        } else {
            System.out.println("خيار غير صحيح!");
        }
        
        System.out.println("تمت إضافة الكتاب بنجاح ✓");
    }

    // ======================= المستعيرين =======================
    public void addBorrower() {
        System.out.println("\n=== إضافة مستعير جديد ===");
        System.out.print("الرقم الجامعي: ");
        String studentId = scanner.nextLine();
        
        if (findBorrowerByStudentId(studentId) != null) {
            System.out.println("!هذا المستعير مسجل مسبقًا");
            return;
        }

        System.out.print("الاسم: ");
        String name = scanner.nextLine();
        
        borrowers.add(new Borrower(name, studentId));
        System.out.println("تمت إضافة المستعير بنجاح ✓");
    }

    // ======================= الإعارة =======================
    public void borrowBook() {
        System.out.println("\n=== إعارة كتاب ===");
        System.out.print("رقم ISBN للكتاب: ");
        String isbn = scanner.nextLine();
        System.out.print("الرقم الجامعي للمستعير: ");
        String studentId = scanner.nextLine();
        
        Book book = findBookByISBN(isbn);
        Borrower borrower = findBorrowerByStudentId(studentId);

        if (book == null) {
            System.out.println("!الكتاب غير موجود");
            return;
        }
        if (borrower == null) {
            System.out.println("!المستعير غير مسجل");
            return;
        }
        if (!book.isAvailable()) {
            System.out.println("!الكتاب غير متاح حاليًا");
            return;
        }

        book.setAvailable(false);
        borrower.borrowBook(book);
        borrowingProcesses.add(new BorrowingProcess(
            book, 
            borrower, 
            LocalDate.now(), 
            LocalDate.now().plusWeeks(2)
        ));
        System.out.println("تمت إعارة الكتاب بنجاح ✓");
    }

    // ======================= الإرجاع =======================
    public void returnBook() {
        System.out.println("\n=== إرجاع كتاب ===");
        System.out.print("رقم ISBN للكتاب: ");
        String isbn = scanner.nextLine();
        System.out.print("الرقم الجامعي للمستعير: ");
        String studentId = scanner.nextLine();
        
        Book book = findBookByISBN(isbn);
        Borrower borrower = findBorrowerByStudentId(studentId);

        if (book == null || borrower == null) {
            System.out.println("!البيانات غير صحيحة");
            return;
        }
        if (!borrower.getBorrowedBooks().contains(book)) {
            System.out.println("!هذا المستعير لم يُعِر هذا الكتاب");
            return;
        }

        book.setAvailable(true);
        borrower.returnBook(book);
        updateBorrowingProcess(book, borrower);
        System.out.println("تم إرجاع الكتاب بنجاح ✓");
    }

    // ======================= البحث =======================
    public void searchBook() {
        System.out.println("\n=== بحث عن كتاب ===");
        System.out.print("الكلمة المفتاحية: ");
        String query = scanner.nextLine().toLowerCase();
        
        books.stream()
            .filter(book -> matchesQuery(book, query))
            .forEach(book -> System.out.println(book.getDetails()));
    }

    public void searchBorrower() {
        System.out.println("\n=== بحث عن مستعير ===");
        System.out.print("الكلمة المفتاحية: ");
        String query = scanner.nextLine().toLowerCase();
        
        borrowers.stream()
            .filter(borrower -> 
                borrower.getName().toLowerCase().contains(query) ||
                borrower.getStudentId().toLowerCase().contains(query))
            .forEach(borrower -> System.out.println(borrower.getDetails()));
    }

    // ======================= الواجهة الرئيسية =======================
    public void displayMenu() {
        try {
            while (true) {
                System.out.println("\n╔═══════════════════════════╗");
                System.out.println("║   نظام إدارة المكتبة      ║");
                System.out.println("╠═══════════════════════════╣");
                System.out.println("║ 1. إضافة كتاب            ║");
                System.out.println("║ 2. إضافة مستعير          ║");
                System.out.println("║ 3. إعارة كتاب            ║");
                System.out.println("║ 4. إرجاع كتاب            ║");
                System.out.println("║ 5. بحث عن كتاب          ║");
                System.out.println("║ 6. بحث عن مستعير        ║");
                System.out.println("║ 0. خروج                 ║");
                System.out.println("╚═══════════════════════════╝");
                
                System.out.print("الاختيار: ");
                int choice = getIntInput();
                
                switch (choice) {
                    case 1 -> addBook();
                    case 2 -> addBorrower();
                    case 3 -> borrowBook();
                    case 4 -> returnBook();
                    case 5 -> searchBook();
                    case 6 -> searchBorrower();
                    case 0 -> {
                        System.out.println("شكرًا لاستخدام النظام!");
                        return;
                    }
                    default -> System.out.println("!خيار غير صحيح");
                }
            }
        } finally {
            scanner.close();
        }
    }

    // ======================= الدوال المساعدة =======================
    private int getIntInput() {
        while (true) {
            try {
                int input = scanner.nextInt();
                scanner.nextLine();
                return input;
            } catch (InputMismatchException e) {
                System.out.println("!يرجى إدخال رقم صحيح");
                scanner.nextLine();
            }
        }
    }

    private boolean matchesQuery(Book book, String query) {
        return book.getTitle().toLowerCase().contains(query) ||
               book.getAuthor().toLowerCase().contains(query) ||
               book.getISBN().toLowerCase().contains(query);
    }

    private Book findBookByISBN(String isbn) {
        return books.stream()
            .filter(b -> b.getISBN().equals(isbn))
            .findFirst()
            .orElse(null);
    }

    private Borrower findBorrowerByStudentId(String studentId) {
        return borrowers.stream()
            .filter(b -> b.getStudentId().equals(studentId))
            .findFirst()
            .orElse(null);
    }

    private void updateBorrowingProcess(Book book, Borrower borrower) {
        borrowingProcesses.stream()
            .filter(bp -> bp.getBook().equals(book) && 
                         bp.getBorrower().equals(borrower) && 
                         bp.getReturnDate() == null)
            .findFirst()
            .ifPresent(bp -> bp.setReturnDate(LocalDate.now()));
    }
}