package Server;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import ETC.Counselor;
import ETC.Customer;
import ETC.FileHandler;

public class Supplier {
    private static Counselor me;
    private static Map<String, Counselor> counselorData = new HashMap<>();
    private static FileHandler cfh = new FileHandler("counselor.bin", "counselor.bin");
    private static FileHandler sfh = new FileHandler("data.bin", FileHandler.READ);

    public static void main(String[] args) {
        System.out.println("웹케시 고객센터 Counselor ver0.1.0");
        String select;
        dataRead();

        // 상담사 등록
        if(login()) {
            while(true) {
                Scanner sc = new Scanner(System.in);
                System.out.print("1.배정고객목록   0.종료 > ");
                select = sc.nextLine();
                if(select.equals("1")) {
                    Queue<Customer> taskList = me.getTaskList();
                    Iterator<Customer> ite = taskList.iterator();
                    System.out.println("----------------------------------------------------------");
                    while(ite.hasNext()) {
                        Customer cs = ite.next();
                        System.out.println(cs.customerId + "\t" + cs.name + "\t" + cs.inquiry + "\t" + cs.counselor);
                    }
                    System.out.println("----------------------------------------------------------");
                }else if(select.equals("0")) {
                    System.out.println("이용해주셔서 감사합니다!");
                    return;
                }
            }
        }else {
            System.out.println("이용해주셔서 감사합니다!");
            return;
        }
    }

    public static void dataRead() {
        cfh.settingReader();

        String msg = cfh.FileRead();

        String[] rawData = msg.split("\n");
        for(int i = 0; i<rawData.length; i++) {
            counselorData.put(rawData[i], new Counselor(rawData[i]));
        }

        cfh.frClose();
    }

    public static void dataWrite() {
        cfh.settingWriter();

        String writeMSG = "";
        Set<String> names = counselorData.keySet();
        Iterator<String> ite = names.iterator();

        while(ite.hasNext()) {
            String name = ite.next() + "\n";
            writeMSG += name;
        }

        cfh.FileWrite(writeMSG);

        cfh.fwClose();
    }

    // data.bin read 이후 로그인한 상담사에게 배정된 고객들을 저장
    public static void loadTask() {
        sfh.settingReader();
        sfh.settingBufferedReader();

        String msg = sfh.BufferedFileRead();
        String[] rawData = msg.split("\n");

        for(int i = 0; i<rawData.length; i++) {
            String[] data = rawData[i].split("\t");
            if(data[3].equals(me.getName())) {
                Customer cs = new Customer(data[1]);
                cs.inquiry = data[2];
                cs.counselor = data[3];
                me.addCustomer(cs);
            }
        }

        sfh.brClose();
        sfh.frClose();
    }

    // 상담사 로그인
    public static boolean login() {
        System.out.println("로그인 화면");
        String select = "";

        while(true) {
            Scanner sc = new Scanner(System.in);
            System.out.print("0.로그인    1.상담원 등록     2.취소    >  ");
            select = sc.nextLine();
            if(select.equals("0")) {
                String name;
                while(true) {
                    System.out.println("이전 메뉴로 돌아가기 - (0)");
                    System.out.print("이름> ");
                    name = sc.nextLine();
                    if(name.equals("0")) {
                        break;
                    }
                    if(counselorData.containsKey(name)) {
                        System.out.println("환영합니다! " + name + " 상담원님!");
                        me = counselorData.get(name);
                        loadTask();
                        return true;
                    }else {
                        System.out.println("등록되지 않은 상담원입니다!");
                        break;
                    }
                }
            }else if(select.equals("1")) {
                String name;
                while(true) {
                    System.out.println("이전 메뉴로 돌아가기 - (0)");
                    System.out.print("이름> ");
                    name = sc.nextLine();
                    if(name.equals("0")) {
                        break;
                    }
                    if(counselorData.containsKey(name)) {
                        System.out.println("이미 존재하는 상담원입니다! 다시 입력해주세요!");
                    }else {
                        Counselor cs = new Counselor(name);
                        counselorData.put(name, cs);
                        System.out.println("환영합니다! " + name + " 상담원님!");
                        dataWrite();
                        return true;
                    }
                }

            }else if(select.equals("2")) {
                System.out.println("시스템을 종료합니다!");
                return false;
            }else {
                System.out.println("잘못된 입력입니다! 다시 한 번 입력해주세요!");
            }
        }


    }

}

