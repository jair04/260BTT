/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIG_ArcGIS;

import GUI.General_GUI;
import GUI.AddPointFrame;
import com.esri.core.geometry.Point;
import com.esri.map.MapOverlay;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author Jair
 */
public class MouseClickedOverlay extends MapOverlay{

    private final JComponent contentPane;
    General_GUI general_GUI;
    AddPointFrame addpoint;
    int x;
    int y;

    public MouseClickedOverlay(General_GUI general) {
        this.general_GUI = general;
        this.contentPane = general.getContentPane();

    }

    @Override
    public void onMouseClicked(MouseEvent event) {
        if (SwingUtilities.isRightMouseButton(event)) {
            this.x = event.getX();
            this.y = event.getY();

            if(addpoint != null){addpoint.setVisible(false);}            
            
            Point mapPoint = this.getMap().toMapPoint(this.x, this.y);
           
           //hjgjhg 
            
            addpoint = new AddPointFrame(this.x, this.y, this.general_GUI,mapPoint);
            addpoint.setVisible(true);           
            
        }else{
            if(addpoint != null){
                addpoint.setVisible(false);
            }
        }

    }

}
