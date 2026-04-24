import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
       Device[] arrDevice =  Device.values();

        System.out.println(Arrays.toString(arrDevice));

        MyService myService1 = MyService.getInstance();
        MyService myService2 = MyService.getInstance();



    }
}
