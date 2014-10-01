package GUI;

import Controller.Constante;
import JMS_ActiveMQ.Publicador;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class ComandoCentral_GUI extends General_GUI{
    private final Publicador comando;
    
    public ComandoCentral_GUI() throws Exception
    {  
        this.comando = new Publicador(this);
        super.setPublicador(this.comando);
        
        super.setTypeUser(Constante.COMANDO);
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

    
    
    
}
