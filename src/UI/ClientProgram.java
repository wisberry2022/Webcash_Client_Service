package UI;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.LayoutManager;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class Monitor extends Frame {
    static Toolkit tk = Toolkit.getDefaultToolkit();
    static Dimension sizeManager = new Dimension(500, 400);
    static final int SCREEN_WIDTH = tk.getScreenSize().width;
    static final int SCREEN_HEIGHT = tk.getScreenSize().height;
    static final int PROGRAM_WIDTH = sizeManager.width;
    static final int PROGRAM_HEIGHT = sizeManager.height;
    static final PanelManager PM = new PanelManager();

    public Monitor() {
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

        Panel nameField = PM.getInputField();

        CasePanel.add(nameField, BorderLayout.NORTH);

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

        // 에러 메뉴
        java.awt.List selection = PM.getSelectMenu();
        subGbc.gridx = 0;
        subGbc.gridy = 1;
        selectBox.add(selection, subGbc);

        // 상담신청 출력창
        TextArea resultMonitor = new TextArea(10,35);
        resultMonitor.setEditable(false);

        subGbc.gridx = 1;
        subGbc.gridy = 1;
        selectBox.add(resultMonitor, subGbc);

        // 상담신청
        Button btn = new Button("request");
        subGbc.gridx = 0;
        subGbc.gridy = 2;
        subGbc.gridwidth = 2;
        subGbc.insets = new Insets(10, 0, 10, 0);
        selectBox.add(btn, subGbc);

        CasePanel.add(selectBox);

        total.gridx = 0;
        total.gridy = 1;
        add(CasePanel, total);

        setVisible(true);

        // 닫기 event
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

