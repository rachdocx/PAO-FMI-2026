
import model.Book;
import model.MyFirstInterface;
import model.MyFirstInterfaceImpl;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class Main{


    public static void main(String[] args) {
      Book book1 = new Book("The Great Gatsby", "F. Scott Fitzgerald", "A novel about the American dream.");
      Book book2 = new Book("To Kill a Mockingbird", "Harper Lee", "A novel about racial injustice in the Deep South.");
      Book book3 = new Book("1984", "George Orwell", "A dystopian novel about totalitarianism and surveillance.");


      //array urile au dimeansiunea fixa
      Book [] books = new Book[0];
//      books[0] = book;

        books = addBook(books, book1);
        books = addBook(books, book2);
        books = addBook(books, book3);

      //Arrays -> clasa care ne ajuta in lucru cu arrays
        System.out.println(Arrays.toString(books));
        int [] arr = {5, 2, 8, 1, 3};


        Comparator<Book> bookComparator = (b1, b2) -> b1.getName().compareTo(b2.getName());

        Arrays.sort(books, bookComparator);

        //sortare : Comparator/Comparable


        System.out.println(Arrays.toString(books));

//        MyFirstInterface myMyfirstInterface = new MyFirstInterfaceImpl();
//        myMyfirstInterface.m1();

        MyFirstInterface myFirstInterface = () -> System.out.println("m1");
        myFirstInterface.m1();

        System.out.println(Book.findBookByName(books, "1984"));

        // cititre instructiuni de la tastatura
        // 1. List All Books
        // 2. Add a Book
        // 3. Find a Book by Name

        Scanner scanner = new Scanner(System.in);
            while (true){
                System.out.println("1. List All Books");
                System.out.println("2. Add a Book");
                System.out.println("3. Find a Book by Name");
                System.out.println("4. Exit");

                int option = scanner.nextInt();
                scanner.nextLine();

                switch (option){
                    case 1:
                        System.out.println(Arrays.toString(books));
                        break;
                    case 2:
                        System.out.println("Enter book name:");
                        String name = scanner.nextLine();
                        System.out.println("Enter book author:");
                        String author = scanner.nextLine();
                        System.out.println("Enter book description:");
                        String description = scanner.nextLine();

                        Book newBook = new Book(name, author, description);
                        books = addBook(books, newBook);
                        break;
                    case 3:
                        System.out.println("Enter book name to find:");
                        String searchName = scanner.nextLine();
                        Book foundBook = Book.findBookByName(books, searchName);
                        if(foundBook != null){
                            System.out.println(foundBook);
                        } else {
                            System.out.println("Book not found!");
                        }
                        break;
                    case 4:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }
    }

    private static Book [] addBook(Book [] books, Book book ){
        Book [] newBooks = new Book[books.length + 1];

        for(int i = 0; i < books.length; i++){
            newBooks[i] = books[i];
        }

        //mai bun:
        //System.arraycopy(books, 0, newBooks, 0, books.length);

        newBooks[newBooks.length - 1] = book;
        return newBooks;
    }


}