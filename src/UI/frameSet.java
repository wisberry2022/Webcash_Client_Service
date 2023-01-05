package UI;

import java.awt.Button;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

interface frameSet<T> {
    public T getFrame();
}

class ERR_MODAL<T> extends Frame {
    final static Toolkit tk = Toolkit.getDefaultToolkit();
    final static int SCREEN_WIDTH = tk.getScreenSize().width;
    final static int SCREEN_HEIGHT = tk.getScreenSize().height;
    final static PanelManager PM = new PanelManager();
    private T PARENT;
    private static int PROGRAM_WIDTH;
    private static int PROGRAM_HEIGHT;

    public ERR_MODAL(Dimension sizeManager, T Parent, int widWeight, int hgtWeight) {
        PROGRAM_WIDTH = (int)(sizeManager.width-widWeight);
        PROGRAM_HEIGHT = (int)(sizeManager.height-hgtWeight);
        this.PARENT = Parent;
    }
    
    public Dialog ERR_WINDOW(String msg) {
        Dialog ERR = new Dialog((Frame)PARENT, "ERROR");
        int posWid = (int)(SCREEN_WIDTH/2) - (int)(PROGRAM_WIDTH/2);
        int posHgt = (int)(SCREEN_HEIGHT/2) - (int)(PROGRAM_HEIGHT/2);
        ERR.setBounds(posWid, posHgt, PROGRAM_WIDTH, PROGRAM_HEIGHT);

        ERR.setLayout(new GridLayout(4,1));

        Button closing = PM.getButton("OK");
        closing.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                ERR.dispose();
            }
        });

        Panel title = new Panel();
        title.setLayout(new FlowLayout());
        title.add(PM.getTitle(msg));

        Panel btnPad = new Panel();
        btnPad.setLayout(new FlowLayout());
        btnPad.add(closing);

        ERR.add(PM.getPadding());
        ERR.add(title);
        ERR.add(btnPad);

        ERR.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                ERR.dispose();
            }
        });
        return ERR;
    }

}
