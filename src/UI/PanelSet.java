package UI;

import java.awt.*;

class PanelManager extends Frame{
    Label lb;
    Panel panel;
    Font font;
    Button btn;
    TextField tf;

    PanelManager() {}

    Label getTitle(String name) {
        this.lb = new Label();
        lb.setText(name);
        return this.lb;
    }

    Panel getTitlePanel(String name) {
        panel = new Panel();
        LayoutManager fl = new FlowLayout();
        panel.setLayout(fl);
        lb = getTitle(name);
        Font lbFont = getFont("Serif", "bold", 24);
        lb.setFont(lbFont);
        panel.add(lb);
        return panel;
    }

    Panel getGridPanel(String name, int gridX, int gridY) {
    	panel = new Panel();
    	panel.setLayout(new GridLayout(gridX, gridY));
    	lb = new Label(name);
    	lb.setAlignment(Label.CENTER);
    	lb.setFont(getFont("Serif", "bold", 24));
    	return panel;
    }
    
    Button getButton(String name) {
        btn = new Button(name);
        return btn;
    }

    Font getFont(String fontname, String style, int size) {
        if(style.equals("italic")) font = new Font(fontname, Font.ITALIC, size);
        else if(style.equals("bold")) font = new Font(fontname, Font.BOLD, size);
        else if(style.equals("plain")) font = new Font(fontname, Font.PLAIN, size);
        else font = new Font(fontname, Font.PLAIN, size);
        return font;
    }

    Label getPadding() {
        lb = new Label();
        return lb;
    }

    Panel getInputField() {
        Panel CasePanel = new Panel();
        CasePanel.setLayout(new BorderLayout());
        panel = new Panel();

        panel.setLayout(new FlowLayout());
        Label lb = _getLabel("Your Name");
        TextField tf = new TextField(30);
        btn = _getButton("submit");
        panel.add(lb, FlowLayout.LEFT);
        panel.add(tf, FlowLayout.CENTER);
        panel.add(btn, FlowLayout.RIGHT);

        CasePanel.add(panel, BorderLayout.CENTER);

        return CasePanel;
    }

    TextField getTextField(int size) {
        tf = new TextField(size);
        return tf;
    }

    List getSelectMenu() {
        String[] Inqueries = {"ERROR Occur", "Solution Adopt", "Solution Close", "ETC"};
        List list = new List();
        for(int i = 0; i<Inqueries.length; i++) {
            list.addItem(Inqueries[i]);
        }
        return list;
    }

    public Label getLabel(String name) {
        lb = new Label(name);
        return lb;
    }

    private Label _getLabel(String name) {
        lb = new Label(name);
        return lb;
    }

    private Button _getButton(String name) {
        btn = new Button(name);
        return btn;
    }

    Panel getCasePanel() {
        panel = new Panel();
        return panel;
    }

}
