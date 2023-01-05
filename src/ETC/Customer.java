package ETC;

public class Customer {
    public static int wait = 0;
    public int customerId;
    public String name;
    public String inquiry;
    public String counselor;

    public Customer(String name){
        this.name = name;
        customerId = ++wait;
    }
}

