public class MyService {
    //design pattern de singleton
    private MyService(){

    }

    private static class SINGLETON_HOLDER {
        private static final MyService INSTANCE = new MyService();
    }

    public static MyService getInstance(){
        return SINGLETON_HOLDER.INSTANCE;
    }

    //...METODE ALE CLASEI
}
