/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SistemaTactico;

import DAO.Aeronave;
import GUI.Aeronave_GUI;
import GUI.UserInformation;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author Jair
 */
public class Aeronave_main {

    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    Aeronave aeronaveDAO = new Aeronave();
                    aeronaveDAO.readFileInformation();

                    if (aeronaveDAO.getCounter() == 0) {
                        UserInformation uInformation = new UserInformation();
                        uInformation.setVisible(true);
                    } else {
                        // instance of this application
                        Aeronave_GUI aeronave = new Aeronave_GUI();

                        // create the UI, including the map, for the application.
                        JFrame appWindow = aeronave.createWindow();
                        appWindow.add(aeronave.createUI());
                        appWindow.setExtendedState(JFrame.MAXIMIZED_BOTH);
                        appWindow.setVisible(true);
                    }

                } catch (Exception e) {
                    System.out.println(e.toString());
                }
            }
        });
        
        //System.out.println(getClass().getClassLoader().getResource(".").getPath());
    }
        
    
}
