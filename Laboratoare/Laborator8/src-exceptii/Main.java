import exceptions.MyFirstException;
import exceptions.MySecondException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        /**
         * exceptii
         * obiecte ->object
         * excepetii -> throwable -> exceptii checked: execeptii care trebuiesc tarate obligatoriu(ex:
         *                        -> exceptii unchecked: NullPointerException
         *
         * throws -> aruncam exceptia mai sus
         * throw -> atuncam o exceptie
         *
         * Throwable
         *
         * Exception    Errors: nu tin de codul sursa ci mai mult de partea de hardware
         *
         * RuntimeException
         *
         *
         */
        try{
            FileInputStream fileInputStream = new FileInputStream("data/data.txt");
        }
        catch (MyFirstException | MySecondException e){
            throw new RuntimeException(e);
        }
        catch(Exception e){
            throw new RuntimeException(e);      //pentru mai multe exceptii se vor trata de la cea mai putin prioritara la cea mai prioritara
        }
        finally {
            // de regula aici se inchid resursele
        }

        //try with resources -> stie sa inchida resursele automat
        try(var fileInputStream = new FileInputStream("data/data.txt");) {

        }catch(IOException e){
             throw new RuntimeException(e);
        }



    }
}
