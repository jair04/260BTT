/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package GUI;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author Jair
 */
public class ScrollPanel extends JScrollPane{
    private final int xPosition;
    private final int yPosition;
    
    public ScrollPanel(JPanel panel, int xPosition, int yPosition){
       this.xPosition = xPosition;
       this.yPosition = yPosition;
       this.initComponents();
       panel.setVisible(true);
       panel.setLocation(0, 0);
       this.setViewportView(panel);
       
       this.setLocation(xPosition, yPosition);
       
    }
    
    private void initComponents(){
        if (xPosition == 185 && yPosition == 145) {
            this.setSize(135, 85);
        } else {// otherwise
            this.setSize(226, 85);
        }     
        this.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        this.setBackground(new Color(0, 0, 0, 0));
        this.setBorder(new javax.swing.border.LineBorder(new Color(0, 0, 0), 0, true));
        this.setVisible(false);
        this.getVerticalScrollBar().setPreferredSize (new Dimension(0,0));
    }
    
    
}
