package GUI;

import DAO.Comandante;
import JMS_ActiveMQ.Publicador;
import SIG_ArcGIS.GeoPositionListener;
import com.esri.core.gps.BaudRate;
import com.esri.core.gps.GPSException;
import com.esri.core.gps.Parity;
import com.esri.core.gps.SerialPortGPSWatcher;
import com.esri.core.gps.SerialPortInfo;
import com.esri.core.gps.StopBits;
import com.esri.map.GPSLayer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

public class ComandoCentral_GUI extends General_GUI{
    private final Publicador comando;
    
    public ComandoCentral_GUI() throws Exception
    {  
        this.comando = new Publicador(this);
    }
    
    
    public JComponent createUI() 
    {
        ButtonPanel server = null;
        try 
        {
            server = new ButtonPanel("server", 10, 145, null);
            server.setPublicador(this.comando);
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(ComandoCentral_GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        super.contentPane.add(server);  
        super.contentPane.add(super.map);

        return super.contentPane;
    }

    
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
