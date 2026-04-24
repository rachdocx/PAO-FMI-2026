import java.util.*;

public class Main {
    public static void main(String[] args) {
        // Collections
        List<String> myList1 = new ArrayList<>(); //lista simpla
        List<String> myList2 = new LinkedList<>(); //lista dublu inlantuita

        Collections.sort(myList1);

        //SET
        Set<String> set1 = new HashSet<>(); //nu admite duplicate
        Set<String> set2 = new TreeSet<>(); //nu admite duplicate, elementele sunt sortate

        Comparator<Elev> comparator = (e1, e2) -> e1.getNume().compareTo(e2.getNume());

        Set<Elev> set3 = new TreeSet<>(comparator);

        //MAP -> key:value
        Map<String, String> map1 = new HashMap<>(); //nu este sortat
        map1.put("key1", "value1");
        System.out.println(map1);
        Map<String, String> map2 = new TreeMap<>(); //este sortat dupa cheie



    }
    public static Set<String> m1(){
        //incalcare principiu Liskov Substitution

        return new HashSet<>();
    }
}

class Elev{
    private String nume;

    public Elev(String nume) {
        this.nume = nume;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }
}