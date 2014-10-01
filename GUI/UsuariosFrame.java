/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import DAO.Aeronave;
import com.esri.core.geometry.Point;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Map;
import javax.swing.JPanel;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;

/**
 *
 * @author Jair
 */
public class UsuariosFrame extends javax.swing.JFrame {

    /**
     * Creates new form UsuariosFrame
     */
    private General_GUI general;
    private Point point;
    //private AddPointFrameCommand frame;

    public UsuariosFrame(int x, int y, General_GUI general, Point point, AddPointFrameCommand frame) {
        this.general = general;
        this.point = point;

        this.setUndecorated(true);
        this.setLocation(x + 175, y + 53);
        initComponents();

        String key;
        Aeronave aeronave;
        UserPanel userPanel;

        if (general.getMision() != null) {
            for (Map.Entry<String, Aeronave> entry : general.getMision().entrySet()) {
                key = entry.getKey();
                aeronave = entry.getValue();
                if (!"comando".equals(key)) {
                    userPanel = new UserPanel(aeronave.getColor(), key, general);
                    userPanel.setFrama(this);
                    userPanel.setCommandFrame(frame);
                    userPanel.setPoint(point);
                    userPanel.setTipo(1);
                    jPanel1.add(userPanel);
                }
            }
        }

        jScrollPane1.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.Y_AXIS));
        jScrollPane1.setViewportView(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }



    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
