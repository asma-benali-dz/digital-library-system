package library.models;

public class PaperBook extends Book {
    private int pageCount;

    public PaperBook(String title, String author, String ISBN, int pageCount) {
        super(title, author, ISBN);
        this.pageCount = pageCount;
    }

    public int getPageCount() { return pageCount; }

    @Override
    public String toString() {
        return "PaperBook{" +
                super.toString() +
                ", pageCount=" + pageCount +
                '}';
    }
}