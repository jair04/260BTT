/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package SistemaTactico;

import GUI.ComandoCentral_GUI;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author Jair
 */
public class Comando_main {
    public static void main(String args[]) 
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    // instance of this application
                    ComandoCentral_GUI central = new ComandoCentral_GUI();

                    // create the UI, including the map, for the application.
                    JFrame appWindow = central.createWindow();
                    appWindow.add(central.createUI());
                    appWindow.setExtendedState(JFrame.MAXIMIZED_BOTH);
                    appWindow.setVisible(true);
                } 
                catch (Exception e)
                {
                    System.out.println(e.toString());
                }
            }
        });
    }
}
