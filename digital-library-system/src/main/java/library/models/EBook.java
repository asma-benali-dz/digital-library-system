package library.models;

public class EBook extends Book {
    private String fileFormat;

    public EBook(String title, String author, String ISBN, String fileFormat) {
        super(title, author, ISBN);
        this.fileFormat = fileFormat;
    }

    public String getFileFormat() { return fileFormat; }

    @Override
    public String toString() {
        return "EBook{" +
                super.toString() +
                ", fileFormat='" + fileFormat + '\'' +
                '}';
    }
}