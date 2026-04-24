//nu poate sa extinda clase dar poate implementa interfete

//enum este cel mai bun exemplu de singleton (design pattern de singleton)
public enum Device implements I1 {


    LAPTOP("Laptop"){
        @Override
        public double price() {
            return 1000;
        }
    },
    TABLET("Tablet"){
        @Override
        public double price() {
            return 500;
        }
    },
    SMARTPHONE("Smartphone"){
        @Override
        public double price() {
            return 800;
        }
    };
    private String name;

    Device(String name) {
        this.name = name;
    }
    //    @Override
//    public double price() {
//        return 20;
//    }

}
class C1{

}

interface I1{
    double price();
}