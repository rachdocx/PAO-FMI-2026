import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
//        //primitive types
//        //int, char, bool, byte, short, long, float, double
//
//        //wrapper classes
//        //Integer, Character, Boolean, Byte, Short, Long, Float, Double
//
//        int n1 = 10;
//        Integer n2 = 20;
//        Integer n3 = Integer.valueOf(30);
//
//        System.out.println(n3.compareTo(n2));
//
//
//        String str = "Hello"; //clasa imutabila
//
//        var n5 = 10;
//
//        var n6 = "abc";
//
//        var list = new ArrayList<
//               Integer
//                >();
//      list.add("abc");
//        list.add(10);
//
//        float f1 = 10.5f;
//        Float f = 10.5f;
//
//        int a1 = 5;
//        Integer a2 = 10;
//
//        a1 = a2;
//        a2 = a1;
//
//        //instructiuni control
//        //switch -> instructiune ce difera de alte limbaje
//        //operatorul ternar (operatorul Elvis)
//
//        int [] arr1 = {1, 2, 3, 4, 5};
//        int n7 = a1 > a2 ? a1 : a2;
//
//        Arrays.stream(arr1)
//                .filter(x -> x % 2 == 0)
//                .map(e -> e * e)
//                .forEach(el -> System.out.println(el));

    try {
        Scanner sc = new Scanner(new File("/Users/gabrielrachieru/IdeaProjects/PAO-FMI-2026/Laboratoare/Laborator2/lab1/src/util/data.txt"));
        System.out.println("Enter a number: ");
        int a = sc.nextInt();
        System.out.println("The absolute value is: " + f1(a));
    }
    catch (FileNotFoundException e){
        System.out.println("File not found!");
    }

    //arrays

        int [] arr2 = {1, 2, 3, 4, 5};

        
    }
    private static int f1(int a){
        if(a > 0){
            return a;
        } else {
            return -a;
        }
    }
}