package UI;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.LayoutManager;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import ETC.Customer;
import ETC.FileHandler;

class RegisterWindow<T> extends Frame {
    final static Toolkit tk = Toolkit.getDefaultToolkit();
    final static int SCREEN_WIDTH = tk.getScreenSize().width;
    final static int SCREEN_HEIGHT = tk.getScreenSize().height;
    final static PanelManager PM = new PanelManager();  
    final static FileHandler rfh = new FileHandler("counselor.bin", FileHandler.WRITE);
    private static Dimension sizeManager;
    private static List<String> CounselorList;
    private TextField registerField;
    private T PARENT;
    private static int PROGRAM_WIDTH;
    private static int PROGRAM_HEIGHT;

    public RegisterWindow(Dimension sizeManager, T Parent, int widWeight, int hgtWeight) {
    	this.sizeManager = sizeManager;
        PROGRAM_WIDTH = (int)(sizeManager.width-widWeight);
        PROGRAM_HEIGHT = (int)(sizeManager.height-hgtWeight);
        this.PARENT = Parent;
    }

    public void DataWrite() {
    	rfh.settingWriter();
    	rfh.settingBufferedWriter();
    	String msg = "";
    	for(int i = 0; i<CounselorList.size(); i++) {
    		msg = CounselorList.get(i) + "\n";
    		rfh.BufferedFileWrite(msg);
    	}
    	
    	rfh.bwClose();
    	rfh.fwClose();
    }
    
    public void setData(List<String> datas) {
        this.CounselorList = datas;
    }

    public Dialog getDialog() {
        Dialog Register = new Dialog((Frame)PARENT, "Register");
        int posWid = (int)(SCREEN_WIDTH/2) - (int)(PROGRAM_WIDTH/2);
        int posHgt = (int)(SCREEN_HEIGHT/2) - (int)(PROGRAM_HEIGHT/2);
        Register.setBounds(posWid, posHgt, PROGRAM_WIDTH, PROGRAM_HEIGHT);
        
        Register.setLayout(new GridLayout(4,1));

        Register.add(PM.getPadding());
        Panel title = PM.getTitlePanel("Counselor Register");
        Register.add(title);

        Panel p = new Panel();
        p.setLayout(new FlowLayout());

        registerField = PM.getTextField(25);
        Button rgstBtn = PM.getButton("REGISTER");
        p.add(registerField);
        p.add(rgstBtn);

		rgstBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				String input = registerField.getText();
				if(CounselorList.contains(input)) {
					Frame ae = new Frame();
					int posWid = (int)(SCREEN_WIDTH/2) - (int)(PROGRAM_WIDTH/2/2);
			        int posHgt = (int)(SCREEN_HEIGHT/2) - (int)(PROGRAM_HEIGHT/2/2);
					ae.setBounds(posWid, posHgt, PROGRAM_WIDTH/2, PROGRAM_HEIGHT/2 );
					Panel p = new Panel();
					p.setLayout(new GridLayout(2,1));
					Button closing = PM.getButton("OK");
					
					closing.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseReleased(MouseEvent e) {
							ae.dispose();
						}
					});
					
					ae.addWindowListener(new WindowAdapter() {
						@Override
						public void windowClosing(WindowEvent e) {
							ae.dispose();
						}
					});
					
					Label tlb = PM.getLabel("Already Exist!");
					tlb.setAlignment(Label.CENTER);
					p.add(tlb);
					Panel subPn = new Panel();
					subPn.setLayout(new FlowLayout());
					subPn.add(closing);
					p.add(subPn);
					ae.add(p);
					ae.setVisible(true);
					
				}else {
					CounselorList.add(input);
					DataWrite();
					Frame ae = new Frame();
					int posWid = (int)(SCREEN_WIDTH/2) - (int)(PROGRAM_WIDTH/2/2);
			        int posHgt = (int)(SCREEN_HEIGHT/2) - (int)(PROGRAM_HEIGHT/2/2);
					ae.setBounds(posWid, posHgt, PROGRAM_WIDTH/2, PROGRAM_HEIGHT/2 );
					Panel p = new Panel();
					p.setLayout(new GridLayout(2,1));
					Button closing = PM.getButton("OK");
					
					closing.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseReleased(MouseEvent e) {
							ae.dispose();
						}
					});
					
					ae.addWindowListener(new WindowAdapter() {
						@Override
						public void windowClosing(WindowEvent e) {
							ae.dispose();
						}
					});
					
					Label tlb = PM.getLabel("register success");
					tlb.setAlignment(Label.CENTER);
					p.add(tlb);
					Panel subPn = new Panel();
					subPn.setLayout(new FlowLayout());
					subPn.add(closing);
					p.add(subPn);
					ae.add(p);
					ae.setVisible(true);
				}
			}
		});

        Register.add(p);
        
        Register.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Register.dispose();
            }
        });

        return Register;
    }
}

class LoginWindow extends Frame{
    final static Toolkit tk = Toolkit.getDefaultToolkit();
    final static Dimension sizeManager = new Dimension(400, 250);
    final static int SCREEN_WIDTH = tk.getScreenSize().width;
    final static int SCREEN_HEIGHT = tk.getScreenSize().height;
    final static int PROGRAM_WIDTH = sizeManager.width;
    final static int PROGRAM_HEIGHT = sizeManager.height;
    final static PanelManager PM = new PanelManager();
    private ERR_MODAL<LoginWindow> EM = new ERR_MODAL<>(sizeManager, this, 150, 100);
    private RegisterWindow<LoginWindow> RW = new RegisterWindow<>(sizeManager, this, 50, 50);
    private static List<String> CounselorList = new ArrayList<>();
    private static final FileHandler fh = new FileHandler("counselor.bin", "counselor.bin");
    private TextField loginField;
    private Button logBtn, rgstBtn;

    public static void DataLoad() {
        fh.settingReader();
        fh.settingBufferedReader();

        String rawData = "";
        rawData = fh.BufferedFileRead();
        String[] parsed = rawData.split("\n");
        for(int i = 0; i<parsed.length; i++) {
            CounselorList.add(parsed[i]);
        }

        fh.brClose();
        fh.frClose();
    }

    public LoginWindow() {

        DataLoad();
        setLayout(new GridLayout(5,1));

        setMenuBar(new TopMenu().getFrame());

        add(PM.getPadding());
        add(PM.getTitlePanel("Webcash Counselor Service"));

        Panel login = new Panel();
        loginField = PM.getTextField(25);
        logBtn = PM.getButton("Login");

        login.setLayout(new FlowLayout());
        login.add(PM.getLabel("name"));
        login.add(loginField);
        login.add(logBtn);

        logBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                String input = loginField.getText();
                System.out.println(input);
                if(CounselorList.contains(input)) {
                    CounselorProgram Next = new CounselorProgram(input);
                    setVisible(false);
                }else {
                    Dialog lf = EM.ERR_WINDOW("Not Registered Counselor!");
                    lf.setVisible(true);
                };
            }
        });

        add(login);

        Panel p = new Panel();
        p.setLayout(new FlowLayout());
        rgstBtn = PM.getButton("Counselor Register");

        rgstBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                RW.setData(CounselorList);
                Dialog register = RW.getDialog();
                register.setVisible(true);
            }
        });

        p.add(rgstBtn);
        add(p);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        setResizable(false);

        int posWid = (int)(SCREEN_WIDTH/2) - (int)(PROGRAM_WIDTH/2);
        int posHgt = (int)(SCREEN_HEIGHT/2) - (int)(PROGRAM_HEIGHT/2);

        setBounds(posWid, posHgt, PROGRAM_WIDTH, PROGRAM_HEIGHT);
        setVisible(true);
    }
}

class CounselorProgram extends Frame {
    final static Toolkit tk = Toolkit.getDefaultToolkit();
    final static Dimension sizeManager = new Dimension(500, 550);
    final static int SCREEN_WIDTH = tk.getScreenSize().width;
    final static int SCREEN_HEIGHT = tk.getScreenSize().height;
    final static int PROGRAM_WIDTH = sizeManager.width;
    final static int PROGRAM_HEIGHT = sizeManager.height;
    final static PanelManager PM = new PanelManager();
    final static FileHandler rfh = new FileHandler("data.bin", FileHandler.READ);
    private ERR_MODAL<CounselorProgram> EM = new ERR_MODAL<>(sizeManager, this, 100, 100);
    private Queue<Customer> CustomerList = new LinkedList<>();
    private String me;
    private java.awt.List taskList = new java.awt.List();
    private TextArea detail;
    
    public void loadData() {
    	rfh.settingReader();
    	rfh.settingBufferedReader();
    	
    	String rawData;
    	rawData = rfh.BufferedFileRead();
    	
    	String[] parsed = rawData.split("\n");
    	for(int i = 0; i<parsed.length; i++) {
    		String[] data = parsed[i].split("\t");
    		if(me.equals(data[data.length-1])) {
    			Customer cs = new Customer(data[0]);
    			cs.inquiry = data[1];
    			CustomerList.add(cs);
    		}
    	}
    	
    	String msg;
    	
    	Iterator<Customer> ite = CustomerList.iterator();
    	while(ite.hasNext()) {
    		Customer cs = ite.next();
    		msg = "배정 고객: " + cs.name;
    		taskList.add(msg);
    	}
    	
    	rfh.brClose();
    	rfh.frClose();
    }
    
    public Dialog chatWindow() {
    	Dialog chat = new Dialog(this, "WCS Chat");
    	int pw = PROGRAM_WIDTH-50;
    	int ph = PROGRAM_HEIGHT;
    	int posWid = (int)(SCREEN_WIDTH/2) - (int)(pw/2);
        int posHgt = (int)(SCREEN_HEIGHT/2) - (int)(ph/2);
    	chat.setBounds(posWid, posHgt, pw, ph);
    	
    	chat.setLayout(new GridBagLayout());
    	GridBagConstraints gbc = new GridBagConstraints();
    	gbc.fill = GridBagConstraints.BOTH;
    	
    	TextArea chatWindow = new TextArea();
    	gbc.gridx = 0;
    	gbc.gridy = 0;
    	gbc.gridwidth = -1;
    	gbc.weighty = 5;
    	chat.add(chatWindow, gbc);
    	chatWindow.setText(me + " 상담사님 반갑습니다!");
    	
    	TextField chatter = new TextField();
    	gbc.gridx = 0;
    	gbc.gridy = 1;
    	chat.add(chatter, gbc);
    	
    	chat.addWindowListener(new WindowAdapter() {
    		@Override
    		public void windowClosing(WindowEvent e) {
    			chat.dispose();
    		}
    	});
    	
    	return chat;
    }
    
    public CounselorProgram(String name) {
    	me = name;
    	loadData();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        setTitle("WCS");
        LayoutManager lm = new BorderLayout();
        setLayout(lm);
        
        Panel title = new Panel();
        title.setLayout(new GridLayout(2,1));
        Label tlb = PM.getLabel("Webcash Counselor Service");
        tlb.setAlignment(Label.CENTER);
        tlb.setFont(PM.getFont("serif", "bold", 24));
        title.add(tlb);
        Label lb = PM.getLabel(me + " Counselor");
        lb.setAlignment(Label.CENTER);
        title.add(lb);
        
        Label pad = PM.getPadding();
        
        add(title, BorderLayout.NORTH);

        Panel p = new Panel();
        LayoutManager plm = new GridLayout(1,2,5,0);
        p.setLayout(plm);
        
        detail = new TextArea();
        
        taskList.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		String msg = taskList.getSelectedItem();
        		String parsed = msg.split(":")[msg.split(":").length-1].trim();
        		System.out.println(parsed.trim());
        		Iterator<Customer> ite = CustomerList.iterator();
        		while(ite.hasNext()) {
        			String monitor;
        			Customer cs = ite.next();
        			if(cs.name.equals(parsed)) {
        				monitor = "대기번호: " + cs.customerId + "\n" + "이름: " + cs.name + "\n" + "문의내역: " + cs.inquiry;
            			detail.setText(monitor);	
        			}
        		}
        	}
        });
        
        p.add(taskList);
        p.add(detail);
        
        add(p, BorderLayout.CENTER);
        
        Panel btnPn = new Panel();
        btnPn.setLayout(new GridLayout(2,1,0,5));
        
        Button goChat = PM.getButton("Chat");
        
        goChat.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseReleased(MouseEvent e) {
        		Dialog chat = chatWindow();
        		chat.setVisible(true);
        	}
        });
        
        Button done = PM.getButton("Counsel Done");
        btnPn.add(goChat);
        btnPn.add(done);
        add(btnPn, BorderLayout.SOUTH);
        
        int posWid = (int)(SCREEN_WIDTH/2) - (int)(PROGRAM_WIDTH/2);
        int posHgt = (int)(SCREEN_HEIGHT/2) - (int)(PROGRAM_HEIGHT/2);
        setBounds(posWid, posHgt, PROGRAM_WIDTH, PROGRAM_HEIGHT);
        setVisible(true);
    }
}

public class SupplierProgram {

    public static void main(String[] args) {
        LoginWindow lw = new LoginWindow();
//    	CounselorProgram cp = new CounselorProgram("왕인서");

    }

}
