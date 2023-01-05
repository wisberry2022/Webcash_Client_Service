package Client;

import java.util.*;
import java.io.*;
import ETC.Counselor;
import ETC.Customer;
import ETC.FileHandler;

public class Client {
    static ArrayList<String> counselors = new ArrayList<>();
    static String[] Inqueries = new String[] {"에러문의", "솔루션 도입문의", "솔루션 해지문의", "기타"};
    static Queue<Customer> Service = new LinkedList<>();
    static FileHandler fh = new FileHandler("data.bin", "data.bin");

    public static void loadCounselorList() {
        FileHandler cl = new FileHandler("counselor.bin", FileHandler.READ);
        cl.settingReader();

        String msg = cl.FileRead();
        System.out.println(msg);
        String[] rawData = msg.split("\n");

        for(int i = 0; i<rawData.length; i++) {
            counselors.add(rawData[i]);
        }

        cl.frClose();
    }

    // 프로그램 시작 시 db에 있는 고객 대기 정보 파일 큐에 저장
    public static void load() {
        fh.settingInput();
        fh.settingBis();

        String rawData = fh.Bufferead();

        if(rawData.isEmpty()) {
            fh.bisClose();
            fh.iClose();
            return;
        }else {
            String[] rawToParse = rawData.split("\n");
            for(int i = 0; i<rawToParse.length; i++) {
                String[] data = rawToParse[i].split("\t");

                String name = data[1];
                String inquiry = data[2];
                String counselor = data[3];
                Customer cs = new Customer(name);
                cs.inquiry = inquiry;
                cs.counselor = counselor;
                Service.add(cs);
            }
        }


        fh.bisClose();
        fh.iClose();
    }


    // 프로그램 종료 시 큐에 있던 고객 대기 정보 파일 db로 출력
    public static void save() {
        fh.settingOutput();
        fh.settingBos();

        String msg = "";
        Iterator<Customer> ite = Service.iterator();
        while(ite.hasNext()) {
            Customer cs = ite.next();
            msg += cs.customerId + "\t" + cs.name + "\t" + cs.inquiry + "\t" + cs.counselor + "\n";
        }

        fh.bufferWrite(msg);
        fh.bosClose();
        fh.oClose();
    }

    public static void main(String[] args) {
        loadCounselorList();
        // 프로그램 시작 시 db 로드
        load();
        System.out.println("웹케시 고객센터 Client ver0.2.0");
        String select;

        while(true) {
            Scanner sc = new Scanner(System.in);
            boolean isFinish = true;
            System.out.print("0.상담신청         1.신청내역         2.상담내용수정         3.종료   > ");
            select = sc.nextLine();
            if(select.equals("0")) {
                String name;
                System.out.print("이름을 입력해주세요! >  ");
                name = sc.nextLine();
                Customer you = new Customer(name);
                while(isFinish) {
                    System.out.println("------------------------------------------------");
                    System.out.println("0.에러문의    1.솔루션 도입문의    2.솔루션 해지문의    3.기타    4.취소");
                    select = sc.nextLine();
                    if("01234".contains(select)) {
                        int idx = Integer.parseInt(select);
                        if(idx == 4) {
                            System.out.println("이용해주셔서 감사합니다.");
                            Customer.wait--;
                            break;
                        }else {
                            you.inquiry = Inqueries[idx];
                            you.counselor = counselors.get((int)(Math.random()*counselors.size()));
                            Service.offer(you);
                        }
                    }
                    else {
                        System.out.println("0번에서 4번까지의 번호만 입력할 수 있습니다. 다시 확인해주세요!");
                        System.out.println("------------------------------------------------");
                        continue;
                    }
                    System.out.println("대기번호: " + Customer.wait + "번");
                    System.out.println("신청자 성함: " + you.name + "님");
                    System.out.println("배정상담사: " + you.counselor + " 상담사");
                    System.out.println("상담내용: " + you.inquiry);
                    System.out.println("------------------------------------------------");
                    isFinish = false;
                }
            }else if(select.equals("1")) {
                String name;
                System.out.println("------------------------------------------------");
                System.out.print("이름을 입력하세요> ");
                name = sc.nextLine();
                Iterator<Customer> ite = Service.iterator();
                if(ite.hasNext()) {
                    boolean isNotCheck = true;
                    while(ite.hasNext()) {
                        Customer you = ite.next();
                        if(you.name.equals(name)) {
                            System.out.println("대기번호: " + you.customerId + "번");
                            System.out.println("신청자 성함: " + you.name + "님");
                            System.out.println("배정상담사: " + you.counselor + " 상담사");
                            System.out.println("상담내용: " + you.inquiry);
                            System.out.println("------------------------------------------------");
                            isNotCheck = false;
                            break;
                        }
                    }
                    if(isNotCheck) {
                        System.out.println("일치하는 이름이 없습니다! 먼저 상담신청부터 해주세요!");
                        System.out.println("------------------------------------------------");
                    }
                }else {
                    System.out.println("신청한 상담내용이 없습니다!");
                }
            }else if(select.equals("2")) {
                System.out.println("------------------------------------------------");
                boolean finish = false;
                String name;
                System.out.print("이름을 입력하세요!> ");
                name = sc.nextLine();
                Iterator<Customer> ite = Service.iterator();
                while(ite.hasNext()) {
                    Customer you = ite.next();
                    if(you.name.equals(name)) {
                        int modify;
                        System.out.println("수정하고자 하는 상담내용번호를 입력해주세요");
                        for(int i = 0; i<Inqueries.length; i++) {
                            if(you.inquiry.equals(Inqueries[i])) {
                                System.out.print(i+ "." + Inqueries[i] + "(기존신청내역)  ");
                            }else {
                                System.out.print(i+ "." + Inqueries[i] + "   ");
                            }
                        }
                        System.out.print(" >  ");
                        modify = sc.nextInt();
                        you.inquiry = Inqueries[modify];
                        finish = true;
                        break;
                    }
                }
                if(finish) {
                    System.out.println("------------------------------------------------");
                }else {
                    System.out.println("일치하는 이름이 없습니다! 먼저 상담신청부터 해주세요!");
                    System.out.println("------------------------------------------------");
                }
            }else if(select.equals("3")) {
                save();
                System.out.println("이용해주셔서 감사합니다");
                return;
            }else {
                System.out.println("0번과 1번 메뉴 중 하나만 선택해주세요!");
            }
        }
    }

}

