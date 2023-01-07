package UI;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.LayoutManager;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
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
                if(CounselorList.contains(input)) {
                    CounselorProgram next = new CounselorProgram(input);
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


public class CounselorProgram extends Frame{
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
    private String currentCustom;
    private java.awt.List taskList = new java.awt.List();
    private TextArea detail;
	private boolean keep = true;
	static String send = "";
	static String receive = "";
	static TextArea chatWindow = new TextArea();
	static TextField chatter = new TextField();
	static PrintWriter pw;
	
    public void refresh() {
    	String[] itms = taskList.getItems();
    	for(int i = 0; i<itms.length; i++) {
    		taskList.remove(itms[i]);
    	}
    	Iterator<Customer> temp = CustomerList.iterator();
    	Collection<Customer> temp2 = new ArrayList<>();
    	while(temp.hasNext()) {
    		temp2.add(temp.next());
    	}
    	CustomerList.removeAll(temp2);
    }
    
    public void loadData() {
    	rfh.settingReader();
    	rfh.settingBufferedReader();
    	
    	String rawData;
    	rawData = rfh.BufferedFileRead();
    	
    	String[] parsed = rawData.split("\n");
    	int idx = 1;
    	for(int i = 0; i<parsed.length; i++) {
    		String[] data = parsed[i].split("\t");
    		if(me.equals(data[data.length-1])) {
    			Customer cs = new Customer(data[0]);
    			cs.customerId = idx++;
    			cs.inquiry = data[1];
    			CustomerList.add(cs);
    		}
    	}
    	
    	String msg = "";
    	
    	Iterator<Customer> ite = CustomerList.iterator();
    	while(ite.hasNext()) {
    		Customer cs = ite.next();
    		msg = "배정 고객: " + cs.name;
    		taskList.add(msg);
    	}
    	
    	rfh.brClose();
    	rfh.frClose();
    }
    
    public Dialog showChat() {
    	Dialog chat = new Dialog(this, "WCS Chat");
    	int pw = PROGRAM_WIDTH-50;
    	int ph = PROGRAM_HEIGHT;
    	int posWid = (int)(SCREEN_WIDTH/2) - (int)(pw/2);
        int posHgt = (int)(SCREEN_HEIGHT/2) - (int)(ph/2);
    	chat.setBounds(posWid, posHgt, pw, ph);
    	    	
    	chat.setLayout(new BorderLayout());
    	
    	Button off = new Button("Counsel Off");
    	chat.setTitle("WCS-Counselor");
    	
    	Panel btnBox =new Panel();
    	btnBox.setLayout(new GridLayout(2,1));
    	btnBox.add(chatter);
    	btnBox.add(off);
    	
    	off.addMouseListener(new MouseAdapter() {
    		@Override
    		public void mouseReleased(MouseEvent e) {
    			keep = false;
    		}
    	});
    	    	
//    	chatter.addKeyListener(new KeyAdapter() {
//    		@Override
//    		public void keyReleased(KeyEvent e) {
//    			if(e.getKeyCode() == 10) {
//    				send = "[상담사]: " + chatter.getText() + "\n";
//    				chatWindow.append(send);
//    				chatter.setText("");
//    			}
//    		}
//    	});
    	
    	chat.add(chatWindow, BorderLayout.CENTER);
    	chat.add(btnBox, BorderLayout.SOUTH);
    	
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
    	
    	chatter.addActionListener(new ActionListener() {
    		@Override
    		public void actionPerformed(ActionEvent e) {
    			String msg = "[상담사]: " + chatter.getText() + "\n";
    			pw.print(msg);
    			pw.flush();
    			chatter.setText("");
    			chatWindow.append(msg);
    		}
    	});
    	
    	
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
        		Iterator<Customer> ite = CustomerList.iterator();
        		while(ite.hasNext()) {
        			String monitor;
        			Customer cs = ite.next();
        			if(cs.name.equals(parsed)) {
        				monitor = "대기번호: " + cs.customerId + "\n" + "이름: " + cs.name + "\n" + "문의내역: " + cs.inquiry;
        				currentCustom =cs.name; 
            			detail.setText(monitor);	
        			}
        		}
        	}
        });
        
        p.add(taskList);
        p.add(detail);
        
        add(p, BorderLayout.CENTER);
        
        Panel btnPn = new Panel();
        btnPn.setLayout(new GridLayout(3,1,0,5));
        
        Button goChat = PM.getButton("Chat");
        
        goChat.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseReleased(MouseEvent e) {
        		Dialog chatting = showChat();
        		chatting.setVisible(true);
        	}
        });
        
        Button done = PM.getButton("Counsel Done");
        Button refresh = PM.getButton("refresh");
        
        refresh.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseReleased(MouseEvent e) {
        		refresh();
        		loadData();
        	}
        });
        
        btnPn.add(goChat);
        btnPn.add(done);
        btnPn.add(refresh);
        add(btnPn, BorderLayout.SOUTH);
        
        int posWid = (int)(SCREEN_WIDTH/2) - (int)(PROGRAM_WIDTH/2);
        int posHgt = (int)(SCREEN_HEIGHT/2) - (int)(PROGRAM_HEIGHT/2);
        setBounds(posWid, posHgt, PROGRAM_WIDTH, PROGRAM_HEIGHT);
        setVisible(true);
    }
	
	
    public static void main(String[] args) {
    	ServerSocket serverSock = null;
    	Socket client = null;
    	InputStream is = null;
		InputStreamReader isr = null;
		OutputStream os = null;
		OutputStreamWriter osw = null;
		BufferedReader br = null;
		
    	try {
			serverSock = new ServerSocket(8000);
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
        LoginWindow lw = new LoginWindow();
        
        try {
        	client = serverSock.accept();

  			
			
			System.out.println("client 연결여부: " + client.isConnected());
			try {
				is = client.getInputStream();
    			isr = new InputStreamReader(is);
				os = client.getOutputStream();
				osw = new OutputStreamWriter(os);
				br = new BufferedReader(isr);
				pw = new PrintWriter(osw);
				while(true) {					
					String msg;
					while(true) {
						msg = br.readLine();
						if(msg.equals("exit")) break;
						chatWindow.append(msg + "\n");
					}
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}finally {
				if(pw != null) pw.close();
				if(br != null) br.close();
				if(osw != null) osw.close();
				if(os != null) os.close();
				if(isr != null) isr.close();
				if(is != null) is.close();
				if(serverSock != null) serverSock.close();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    }
}

