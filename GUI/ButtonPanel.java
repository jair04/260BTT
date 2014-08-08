/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.io.IOException;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author Jair
 */
public class ButtonPanel extends JPanel {

    private int state = -1;
    final private String currentPath;
    private final JScrollPane submenu;

    public ButtonPanel(final String image, int x, int y, JScrollPane  submenu) throws IOException {
        this.currentPath = new java.io.File(".").getCanonicalPath();
        this.submenu = submenu;
        
        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        this.setLayout(boxLayout);
        this.setLocation(x, y);
        this.setSize(85, 85);
        this.setBackground(new Color(0,0,0, 200));

        //Label which contain the image
        final JLabel imageLabel = new JLabel();

        //Absolute images path
        String imagesPath = currentPath + "\\build\\classes\\Archivos\\Imagenes\\";
        final String iconImageEvent = imagesPath;

        //Setting icon image 
        imageLabel.setIcon(new ImageIcon(imagesPath + image + ".png"));

        //button effect 
        imageLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                state = state * -1;
                if (state == -1) {
                    imageLabel.setIcon(new ImageIcon(iconImageEvent + image + ".png"));
                    hideSubmenu();
                } else {
                    imageLabel.setIcon(new ImageIcon(iconImageEvent + image + "Clicked.png"));
                    showSubmenu();
                }                
            }
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                Cursor cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
                imageLabel.setCursor(cursor);
            }

        });

        //adding imageLabel to controlPanel
        this.add(imageLabel);
    }

    public void showSubmenu(){
        submenu.setVisible(true);
    }
    public void hideSubmenu(){
        submenu.setVisible(false);
    }

}
