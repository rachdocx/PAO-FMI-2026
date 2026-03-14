package model;

import java.util.Arrays;
import java.util.Comparator;

public class Book  {
    private String name;
    private String author;
    private String description;

    public Book(String name, String author, String description) {
        this.name = name;
        this.author = author;
        this.description = description;
    }

    public Book() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "Book{" +
                "name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

//    @Override
//    public int compare(Book o1, Book o2) {
//
//        return o1.getName().compareTo(o2.getName());
//    }

    public static Book findBookByName(Book [] books, String name){
        return Arrays.stream(books).filter(book -> book.getName().equals(name)).findFirst().orElse(null);
    }
}
