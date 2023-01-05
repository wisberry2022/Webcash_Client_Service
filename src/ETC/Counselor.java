package ETC;

import java.util.LinkedList;
import java.util.Queue;

public class Counselor{
    private static int taskOrder = 0;
    private static int id = 0;
    private String name;
    private Queue<Customer> taskList = new LinkedList<>();

    public Counselor(String name) {
        id++;
        this.name = name;
    }

    public void addCustomer(Customer ctm) {
        taskList.add(ctm);
    }

    public String getName() {
        return this.name;
    }

    public Queue<Customer> getTaskList() {
        return this.taskList;
    }
}

