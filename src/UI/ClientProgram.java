package UI;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import ETC.FileHandler;

class Monitor extends Frame {
    static Toolkit tk = Toolkit.getDefaultToolkit();
    static Dimension sizeManager = new Dimension(500, 400);
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
    
    
    public void loadData() {
    	wfh.settingReader();
    	wfh.settingBufferedReader();
    	
    	beforeData = wfh.BufferedFileRead();
//    	System.out.println("기존 데이터: " + beforeData);
    	
    	wfh.brClose();
    	wfh.frClose();
    }
    
    public void WriteData() {	
    	wfh.settingWriter();
    	wfh.settingBufferedWriter();
    
    	String msg;
    	msg = beforeData + name + "\t" + category + "\t" + counselor + "\n";
//    	System.out.println("입력할 데이터: " + msg);
    	
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
    
    public Monitor() {
    	
    	loadCounselor();
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
        subGbc.insets = new Insets(10, 0, 10, 0);
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
}


public class ClientProgram {
    public static void main(String[] args) {
        Monitor mn = new Monitor();
    }

}

