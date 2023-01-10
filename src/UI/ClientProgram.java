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
import java.awt.Insets;
import java.awt.Label;
import java.awt.LayoutManager;
import java.awt.List;
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
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import ETC.FileHandler;

public class ClientProgram extends Frame{
    static Toolkit tk = Toolkit.getDefaultToolkit();
    static Dimension sizeManager = new Dimension(500, 450);
    static final int SCREEN_WIDTH = tk.getScreenSize().width;
    static final int SCREEN_HEIGHT = tk.getScreenSize().height;
    static final int PROGRAM_WIDTH = sizeManager.width;
    static final int PROGRAM_HEIGHT = sizeManager.height;
    static final PanelManager PM = new PanelManager();
    static final FileHandler fh = new FileHandler("counselor.bin", FileHandler.READ);
    static final FileHandler wfh = new FileHandler("data.bin", "data.bin");
    private ArrayList<String> CounselorList = new ArrayList<>();
    private String name;
    private String category;
    private String counselor;
    private String beforeData;
    private TextField tf;
    private TextArea resultMonitor;
    static TextArea chatWindow = new TextArea();
    static TextField chatter = new TextField();
    static String send = "";
    static String receive = "";
    static PrintWriter pw;
    
    public void loadData() {
    	wfh.settingReader();
    	wfh.settingBufferedReader();
    	
    	beforeData = wfh.BufferedFileRead();
    	
    	wfh.brClose();
    	wfh.frClose();
    }
    
    public void WriteData() {	
    	wfh.settingWriter();
    	wfh.settingBufferedWriter();
    
    	String msg;
    	msg = beforeData + name + "\t" + category + "\t" + counselor + "\n";
    	
    	wfh.BufferedFileWrite(msg);
    	
    	wfh.bwClose();
    	wfh.fwClose();
    	
    }
    
    public void loadCounselor() {
    	fh.settingReader();
    	fh.settingBufferedReader();
    	
    	String rawData = fh.BufferedFileRead();
    	String[] datas = rawData.split("\n");
    	for(int i = 0; i<datas.length; i++) {
    		CounselorList.add(datas[i]);
    	}
    	
    	fh.brClose();
    	fh.frClose();
    }
    
    public Dialog showChat() {
    	Dialog chat = new Dialog(this, "WCS Chat");
    	int pW = PROGRAM_WIDTH;
    	int ph = PROGRAM_HEIGHT+300;
    	int posWid = (int)(SCREEN_WIDTH/2) - (int)(pW/2);
        int posHgt = (int)(SCREEN_HEIGHT/2) - (int)(ph/2);
    	chat.setBounds(posWid, posHgt, pW, ph);
    	    	
    	chat.setLayout(new BorderLayout());
    	    	
    	chat.add(chatWindow, BorderLayout.CENTER);
    	
    	Panel btnBox = new Panel();
    	Button off = new Button("Counsel Off");
    	    	
    	off.addMouseListener(new MouseAdapter() {
    		@Override
    		public void mouseReleased(MouseEvent e) {
    			String msg = name + "님이 연결을 끊었습니다!";
    			pw.println("--------------------------------------------------------------------------------------------------------------------------------");
        		pw.print(msg + "\n");
        		pw.println("--------------------------------------------------------------------------------------------------------------------------------");
        		pw.flush();
        		chat.dispose();
    		}
    	});
    	
    	btnBox.setLayout(new GridLayout(2,1));
    	btnBox.add(chatter);
    	btnBox.add(off);
    	
    	chat.add(btnBox, BorderLayout.SOUTH);
    	
    	chat.setTitle("WCS-Client");
    	chat.addWindowListener(new WindowAdapter() {
    		@Override
    		public void windowClosing(WindowEvent e) {
    			String msg = name + "님이 연결을 끊었습니다!";
    			pw.println("--------------------------------------------------------------------------------------------------------------------------------");
        		pw.print(msg + "\n");
        		pw.println("--------------------------------------------------------------------------------------------------------------------------------");
        		pw.flush();
    			chat.dispose();
    		}
    	});
    	
		return chat;
    }
    
    public ClientProgram() {
    	loadCounselor();

    	chatter.addActionListener(new ActionListener() {
    		@Override
    		public void actionPerformed(ActionEvent e) {
    			String msg = "[" + name + "]: " + chatter.getText() + "\n";
    			pw.print(msg);
    			pw.flush();
    			chatter.setText("");
    			chatWindow.append(msg);
    		}
    	});
    	
        LayoutManager lm = new GridBagLayout();
        setLayout(lm);
        
        int widPos = (int)(SCREEN_WIDTH/2) - (int)(PROGRAM_WIDTH/2);
        int hgtPos = (int)(SCREEN_HEIGHT/2) - (int)(PROGRAM_HEIGHT/2);
        setBounds(widPos, hgtPos, PROGRAM_WIDTH, PROGRAM_HEIGHT);
        setTitle("WSC");

        setMenuBar(new TopMenu().getFrame());

        GridBagConstraints total = new GridBagConstraints();
        total.fill = GridBagConstraints.NORTH;

        Panel titlePanel = PM.getTitlePanel("WebCash Service Client");
        total.gridx = 0;
        total.gridy = 0;
        add(titlePanel, total);

        Panel CasePanel = PM.getCasePanel();
        CasePanel.setLayout(new BorderLayout());

        Panel OutPanel = new Panel();
        OutPanel.setLayout(new BorderLayout());
        Panel panel = new Panel();

        panel.setLayout(new FlowLayout());
        Label lb = new Label("Your Name");
        tf = new TextField(30);
        Button btn = new Button("submit");
        
        btn.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseReleased(MouseEvent e) {
        		name = tf.getText();
        	}
        });
        
        panel.add(lb, FlowLayout.LEFT);
        panel.add(tf, FlowLayout.CENTER);
        panel.add(btn, FlowLayout.RIGHT);

        OutPanel.add(panel, BorderLayout.CENTER);
        CasePanel.add(OutPanel, BorderLayout.NORTH);

        Panel selectBox = new Panel();
        selectBox.setLayout(new GridBagLayout());
        GridBagConstraints subGbc = new GridBagConstraints();
        subGbc.fill = GridBagConstraints.BOTH;

        Label firstLabel = PM.getLabel("Category");
        subGbc.gridx = 0;
        subGbc.gridy = 0;
        selectBox.add(firstLabel, subGbc);

        Label secondLabel = PM.getLabel("Result");
        subGbc.gridx = 1;
        subGbc.gridy = 0;
        selectBox.add(secondLabel, subGbc);

        String[] Inqueries = {"에러 문의", "솔루션 도입", "솔루션 해지", "기타"};
        List selection = new List();
        for(int i = 0; i<Inqueries.length; i++) {
            selection.add(Inqueries[i]);
        }
        
        subGbc.gridx = 0;
        subGbc.gridy = 1;
        selectBox.add(selection, subGbc);

        selection.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		category = selection.getSelectedItem();
        	}
        });
        
        resultMonitor = new TextArea(10,35);
        resultMonitor.setEditable(false);

        subGbc.gridx = 1;
        subGbc.gridy = 1;
        selectBox.add(resultMonitor, subGbc);

        Button request = new Button("request");
        subGbc.gridx = 0;
        subGbc.gridy = 2;
        subGbc.gridwidth = 2;
        subGbc.insets = new Insets(10, 0, 3, 0);
        selectBox.add(request, subGbc);
        
        request.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseReleased(MouseEvent e) {
        		loadData();
        		String msg;
        		counselor = CounselorList.get((int)(Math.random() * CounselorList.size()));
        		msg = "이름: " + name + "\n" + "문의내역: " + category + "\n" + "배정 상담사: " + counselor;
        		resultMonitor.setText(msg);
        		WriteData();
        	}
        });
        
        Button chat = new Button("go Chat");
        subGbc.gridx = 0;
        subGbc.gridy = 3;
        subGbc.gridwidth = 2;
        subGbc.insets = new Insets(0, 0, 10, 0);
        selectBox.add(chat, subGbc);
        
        chat.addMouseListener(new MouseAdapter(){
        	@Override
        	public void mouseReleased(MouseEvent e) {
        		String msg = name + "님이 접속하였습니다!";
    			pw.println("--------------------------------------------------------------------");
        		pw.print(msg + "\n");
        		pw.println("--------------------------------------------------------------------");
        		pw.flush();
        		Dialog chatting = showChat();
        		chatting.setVisible(true);
        	}
        });
        
        CasePanel.add(selectBox);

        total.gridx = 0;
        total.gridy = 1;
        add(CasePanel, total);

        setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }
	
    public static void main(String[] args) {
        ClientProgram mn = new ClientProgram();
        Socket client = null;
         
		try {
			client = new Socket(InetAddress.getLocalHost(), 8000);
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		InputStream is = null;
	    InputStreamReader isr = null;
	    OutputStream os = null;
	    OutputStreamWriter osw = null;
	    BufferedReader br = null;
	    
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
					if(msg == null) break;
					chatWindow.append(msg + "\n");
				}
			}
		}catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}finally {				
			try {
				if(pw != null) pw.close();
				if(br != null) br.close();
				if(osw != null) osw.close();
				if(os != null) os.close();
				if(isr != null) isr.close();
				if(is != null) is.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
    }

}

