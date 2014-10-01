package GUI;

import com.sun.pdfview.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import javax.swing.*;

public class ShowerPDF extends JFrame
{
    private JPanel panel;
    private JFileChooser selector;
    private PDFFile pdffile;
    private PDFPage page;
    private int indice = 1, numpag = 0, zoom = 0;
    private JButton bsiguiente, banterior, bmas, bmenos;
    private Image image;
    private boolean bandera = false;
 
    public ShowerPDF(String url) throws FileNotFoundException, IOException
    {
        panel = new JPanel();
        File file = new File (url);
        RandomAccessFile raf = new RandomAccessFile(file, "r");
        FileChannel channel = raf.getChannel();
        ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY,0, channel.size());
        pdffile = new PDFFile(buf);
        numpag = pdffile.getNumPages();
        page = pdffile.getPage(indice);
                
        JPanel pabajo = new JPanel();
        Icon next = new ImageIcon(new java.io.File(".").getCanonicalPath() + "\\build\\classes\\Archivos\\Imagenes\\next.png");
        bsiguiente = new JButton();
        bsiguiente.setIcon(next);
        bsiguiente.setBorder(null);
        bsiguiente.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                if(indice<numpag)
                {
                    banterior.setEnabled(true);
                    indice++;
                    page = pdffile.getPage(indice);
                    zoom = 100;
                    PDFtoImage(page, zoom);
                }
                else
                    bsiguiente.setEnabled(false);
                
            }

        });
        
        Icon back = new ImageIcon(new java.io.File(".").getCanonicalPath() + "\\build\\classes\\Archivos\\Imagenes\\back.png");
        banterior = new JButton();
        banterior.setIcon(back);
        banterior.setBorder(null);
        banterior.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                if(indice>1)
                {
                    bsiguiente.setEnabled(true);
                    indice--;
                    page = pdffile.getPage(indice);
                    zoom = 100;
                    PDFtoImage(page, zoom);
                }
                else
                    banterior.setEnabled(false);
            }
        });
        
        Icon plus = new ImageIcon(new java.io.File(".").getCanonicalPath() + "\\build\\classes\\Archivos\\Imagenes\\plus.png");
        bmas = new JButton();
        bmas.setIcon(plus);
        bmas.setBorder(null);
        bmas.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                if(zoom<300)
                {
                    bmenos.setEnabled(true);
                    page = pdffile.getPage(indice);
                    zoom += 25;
                    PDFtoImage(page, zoom);
                }
                else
                    bmas.setEnabled(false);
            }
        });
        
        Icon minus = new ImageIcon(new java.io.File(".").getCanonicalPath() + "\\build\\classes\\Archivos\\Imagenes\\minus.png");
        bmenos = new JButton();
        bmenos.setIcon(minus);
        bmenos.setBorder(null);
        bmenos.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                if(zoom>25)
                {
                    bmas.setEnabled(true);
                    page = pdffile.getPage(indice);
                    zoom -= 25;
                    PDFtoImage(page, zoom);
                }
                else
                    bmenos.setEnabled(false);
            }
        });

        banterior.setEnabled(false);
        pabajo.add(banterior);
        pabajo.add(Box.createRigidArea(new Dimension(10,30)));
        pabajo.add(bsiguiente);
        pabajo.add(Box.createRigidArea(new Dimension(50,30)));
        pabajo.add(bmas);
        pabajo.add(Box.createRigidArea(new Dimension(10,30)));
        pabajo.add(bmenos);
        panel.setLayout(new BorderLayout());
        panel.add(pabajo, BorderLayout.SOUTH);
        zoom = 100;
        PDFtoImage(page, zoom);
        bandera = true;
    } 
    
    public void PDFtoImage(PDFPage page, int zoom)
    {
        Rectangle2D r2d = page.getBBox ();
        double width = r2d.getWidth ();
        double height = r2d.getHeight ();
        width /= 80.0;
        height /= 80.0;
        width *= zoom;
        height *= zoom;
        image = page.getImage((int)width, (int)height, r2d, null, true, true);
        final JLabel pagina = new JLabel (new ImageIcon (image));
        pagina.setVerticalAlignment (JLabel.TOP);
        pagina.addMouseMotionListener(new MouseMotionAdapter()
        {
            public void mouseDragged(MouseEvent e) 
            {
                pagina.setLocation(pagina.getX() + e.getX() - pagina.getWidth()/2 , pagina.getY() + e.getY() - pagina.getHeight()/2);
            } 
        });
        JScrollPane scroll = new JScrollPane(pagina);
        if(bandera)
            panel.remove(1);
        panel.add(scroll, BorderLayout.CENTER);
        this.add(panel);
        this.setVisible (true);  
    }    
}
