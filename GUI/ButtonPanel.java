/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import JMS_ActiveMQ.Consumidor;
import JMS_ActiveMQ.Publicador;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.jms.JMSException;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 *
 * @author Jair
 */
public class ButtonPanel extends JPanel {

    private int state = -1;
    final private String currentPath;
    private JScrollPane submenu;
    private final String image;
    private Consumidor consumidor;
    private Publicador publicador;
    private int counter = 0;
    private final JLabel imageLabel;
    private final String iconImageEvent;
    private JFileChooser chooser;
    private static String choosertitle = "";
    private JPanel panel;

    public ButtonPanel(final String image, int x, int y, JScrollPane submenu) throws IOException {
        this.image = image;
        this.currentPath = new java.io.File(".").getCanonicalPath();
        this.submenu = submenu;

        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        this.setLayout(boxLayout);
        this.setLocation(x, y);
        this.setSize(85, 85);
        this.setBackground(new Color(0, 0, 0, 200));

        //Label which contain the image
        imageLabel = new JLabel();

        //Absolute images path
        String imagesPath = currentPath + "\\build\\classes\\Archivos\\Imagenes\\";
        iconImageEvent = imagesPath;

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
                    try {
                        showSubmenu();
                    } catch (JMSException ex) {
                        JOptionPane.showMessageDialog(null, "No se encontro el servidor", "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (InterruptedException ex) {
                        System.out.println("1");
                    } catch (UnknownHostException ex) {
                        System.out.println("2");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Servidor en uso", "Error", JOptionPane.ERROR_MESSAGE);
                    }
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

    public void showSubmenu() throws JMSException, InterruptedException, UnknownHostException, Exception {
        if (this.image.equals("conectServer")) {
            String[] options = {"Conectar", "Cancelar"};
            JPanel panel = new JPanel();
            JLabel lbl = new JLabel("Dirección IP: ");
            JTextField txt = new JTextField(10);
            panel.add(lbl);
            panel.add(txt);
            int selectedOption = JOptionPane.showOptionDialog(null, panel, "Informacion", JOptionPane.NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

            if (selectedOption == 0) {
                this.consumidor.setIp(txt.getText());
                this.consumidor.startConnection();
                this.consumidor.sendConnectionRequest();
            }
        } else if (this.image.equals("server")) {
            this.publicador.startServer();
            JOptionPane.showMessageDialog(null, "Servidor INICIADO: ");
        } else if (this.image.equals("users")) {
            submenu.setVisible(true);
        } else if (this.image.equals("camera")) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
            Date date = new Date();
            JPanel panelChooser = new JPanel();

            if (choosertitle.equals("")) {
                chooser = new JFileChooser();
                
                chooser.setDialogTitle(choosertitle);
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                // disable the "All files" option.
                chooser.setAcceptAllFileFilterUsed(false);

                if (chooser.showOpenDialog(panelChooser) == JFileChooser.APPROVE_OPTION) {
                    choosertitle = chooser.getSelectedFile() + "";
                }
            }

            BufferedImage image1 = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
            ImageIO.write(image1, "png", new File(choosertitle+"/Screen_" + dateFormat.format(date) + ".png"));

            JOptionPane.showMessageDialog(null, "ScreenShot: "+"Screen_"+dateFormat.format(date)+".png");
            imageLabel.setIcon(new ImageIcon(iconImageEvent + image + ".png"));
            this.state = -1;

        }else if(this.image.equals("map")){
            this.panel.setVisible(true);
        } else {
            submenu.setVisible(true);
        }

    }

    public void hideSubmenu() {
        if (submenu != null) {
            submenu.setVisible(false);
        }
        
        if(this.panel != null){
            this.panel.setVisible(false);
        }
    }

    public Consumidor getConsumidor() {
        return consumidor;
    }

    public void setConsumidor(Consumidor consumidor) {
        this.consumidor = consumidor;
    }

    public void setPublicador(Publicador publicador) {
        this.publicador = publicador;
    }

    public JScrollPane getSubmenu() {
        return submenu;
    }

    public void setSubmenu(JScrollPane submenu) {
        this.submenu = submenu;
    }

    public JPanel getPanel() {
        return panel;
    }

    public void setPanel(JPanel panel) {
        this.panel = panel;
    }

    
}
