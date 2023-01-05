package UI;

import java.awt.Frame;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

class MenuSet implements Comparable<Object> {
    private int idx;
    private String mainMenu;
    private ArrayList<String> subSet = new ArrayList<>();

    MenuSet(int idx, String name) {
        this.idx = idx;
        mainMenu = name;
    }

    String getMainMenu() {
        return this.mainMenu;
    }

    ArrayList<String> getSubMenu() {
        return this.subSet;
    }

    void addMenu(String menuName) {
        this.subSet.add(menuName);
    }

    @Override
    public int compareTo(Object o) {
        return this.idx - ((MenuSet)(o)).idx;
    }
}

class TopMenu extends Frame implements frameSet<MenuBar>{
    MenuBar mb = new MenuBar();

    public TopMenu() {
        String[][] items = {{"File","log export", "log import"},{"Info", "WSC help"}};

        Set<MenuSet> menuList = new TreeSet<>();

        for(int i = 0; i<items.length; i++) {
            String[] meta = items[i];
            MenuSet menu = new MenuSet(i, meta[0]);
            for(int j = 1; j<meta.length; j++) {
                menu.addMenu(meta[j]);
            }
            menuList.add(menu);
        }

        Iterator<MenuSet> ite = menuList.iterator();
        while(ite.hasNext()) {
            MenuSet menu = ite.next();
            Menu item = new Menu(menu.getMainMenu());
            ArrayList<String> subs = menu.getSubMenu();
            for(int i = 0; i<subs.size(); i++) {
                MenuItem mi = new MenuItem(subs.get(i));
                item.add(mi);
            }
            mb.add(item);
        }
    }

    public MenuBar getFrame() {
        return mb;
    }
}

