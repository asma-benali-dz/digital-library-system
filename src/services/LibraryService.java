package library.services;

import library.models.*;

import java.time.LocalDate;
import java.util.ArrayList;
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

    public void addBook() {
        System.out.println("إضافة كتاب جديد:");
        System.out.print("العنوان: ");
        String title = scanner.nextLine();
        System.out.print("المؤلف: ");
        String author = scanner.nextLine();
        System.out.print("رقم ISBN: ");
        String ISBN = scanner.nextLine();
        
        System.out.print("نوع الكتاب (1-ورقي / 2-إلكتروني): ");
        int type = scanner.nextInt();
        scanner.nextLine(); // consume newline
        
        if (type == 1) {
            System.out.print("عدد الصفحات: ");
            int pages = scanner.nextInt();
            scanner.nextLine();
            books.add(new PaperBook(title, author, ISBN, pages));
        } else {
            System.out.print("صيغة الملف: ");
            String format = scanner.nextLine();
            books.add(new EBook(title, author, ISBN, format));
        }
        
        System.out.println("تمت إضافة الكتاب بنجاح!");
    }

    public void addBorrower() {
        System.out.println("إضافة مستعير جديد:");
        System.out.print("الاسم: ");
        String name = scanner.nextLine();
        System.out.print("الرقم الجامعي: ");
        String studentId = scanner.nextLine();
        
        borrowers.add(new Borrower(name, studentId));
        System.out.println("تمت إضافة المستعير بنجاح!");
    }

    public void borrowBook() {
        System.out.println("إعارة كتاب:");
        System.out.print("أدخل رقم ISBN للكتاب: ");
        String isbn = scanner.nextLine();
        System.out.print("أدخل الرقم الجامعي للمستعير: ");
        String studentId = scanner.nextLine();
        
        Book book = findBookByISBN(isbn);
        Borrower borrower = findBorrowerByStudentId(studentId);
        
        if (book == null || borrower == null) {
            System.out.println("الكتاب أو المستعير غير موجود!");
            return;
        }
        
        if (!book.isAvailable()) {
            System.out.println("الكتاب غير متاح للإعارة!");
            return;
        }
        
        borrower.borrowBook(book);
        borrowingProcesses.add(new BorrowingProcess(
            book, 
            borrower, 
            LocalDate.now(), 
            LocalDate.now().plusWeeks(2)
        ));
        
        System.out.println("تمت إعارة الكتاب بنجاح!");
    }

    public void returnBook() {
        System.out.println("استرجاع كتاب:");
        System.out.print("أدخل رقم ISBN للكتاب: ");
        String isbn = scanner.nextLine();
        System.out.print("أدخل الرقم الجامعي للمستعير: ");
        String studentId = scanner.nextLine();
        
        Book book = findBookByISBN(isbn);
        Borrower borrower = findBorrowerByStudentId(studentId);
        
        if (book == null || borrower == null) {
            System.out.println("الكتاب أو المستعير غير موجود!");
            return;
        }
        
        if (!borrower.getBorrowedBooks().contains(book)) {
            System.out.println("هذا المستعير لم يقم بإعارة هذا الكتاب!");
            return;
        }
        
        borrower.returnBook(book);
        System.out.println("تمت إعادة الكتاب بنجاح!");
    }

    public void searchBook() {
        System.out.println("بحث عن كتاب:");
        System.out.print("أدخل كلمة للبحث (عنوان/مؤلف/ISBN): ");
        String query = scanner.nextLine().toLowerCase();
        
        books.stream()
            .filter(book -> 
                book.getTitle().toLowerCase().contains(query) ||
                book.getAuthor().toLowerCase().contains(query) ||
                book.getISBN().toLowerCase().contains(query))
            .forEach(System.out::println);
    }

    public void searchBorrower() {
        System.out.println("بحث عن مستعير:");
        System.out.print("أدخل كلمة للبحث (اسم/رقم جامعي): ");
        String query = scanner.nextLine().toLowerCase();
        
        borrowers.stream()
            .filter(borrower -> 
                borrower.getName().toLowerCase().contains(query) ||
                borrower.getStudentId().toLowerCase().contains(query))
            .forEach(System.out::println);
    }

    public void showBorrowedBooks() {
        System.out.println("عرض الكتب المستعارة:");
        System.out.print("أدخل الرقم الجامعي للمستعير: ");
        String studentId = scanner.nextLine();
        
        Borrower borrower = findBorrowerByStudentId(studentId);
        if (borrower == null) {
            System.out.println("المستعير غير موجود!");
            return;
        }
        
        if (borrower.getBorrowedBooks().isEmpty()) {
            System.out.println("لا يوجد كتب مستعارة لهذا المستعير.");
        } else {
            borrower.getBorrowedBooks().forEach(System.out::println);
        }
    }

    private Book findBookByISBN(String isbn) {
        return books.stream()
            .filter(book -> book.getISBN().equals(isbn))
            .findFirst()
            .orElse(null);
    }

    private Borrower findBorrowerByStudentId(String studentId) {
        return borrowers.stream()
            .filter(borrower -> borrower.getStudentId().equals(studentId))
            .findFirst()
            .orElse(null);
    }

    public void displayMenu() {
        while (true) {
            System.out.println("\nنظام إدارة المكتبة الرقمية");
            System.out.println("1. إضافة كتاب جديد");
            System.out.println("2. إضافة مستعير جديد");
            System.out.println("3. إعارة كتاب");
            System.out.println("4. استرجاع كتاب");
            System.out.println("5. البحث عن كتاب");
            System.out.println("6. البحث عن مستعير");
            System.out.println("7. عرض الكتب المستعارة لمستعير");
            System.out.println("0. خروج");
            
            System.out.print("اختر الخيار: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            
            switch (choice) {
                case 1: addBook(); break;
                case 2: addBorrower(); break;
                case 3: borrowBook(); break;
                case 4: returnBook(); break;
                case 5: searchBook(); break;
                case 6: searchBorrower(); break;
                case 7: showBorrowedBooks(); break;
                case 0: 
                    System.out.println("شكراً لاستخدامك النظام. إلى اللقاء!");
                    return;
                default:
                    System.out.println("خيار غير صحيح، يرجى المحاولة مرة أخرى.");
            }
        }
    }
}