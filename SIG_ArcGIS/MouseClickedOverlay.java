/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIG_ArcGIS;

import GUI.PanelAddPoint;
import com.esri.map.MapOverlay;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JToolTip;

/**
 *
 * @author Jair
 */
public class MouseClickedOverlay extends MapOverlay {

    private JComponent contentPane;

    public MouseClickedOverlay(JComponent contentPane) {
        
        this.contentPane = contentPane;
        
    }

    @Override
    public void onMouseClicked(MouseEvent event) {

        JPanel submenu = new PanelAddPoint(event.getX()-53, event.getY()-53);
        contentPane.add(submenu);
        System.out.println(event.getX() + "," + event.getY());
        
    }


    
    
    
    

}
