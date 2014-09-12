package GUI;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import javax.swing.*;
import com.sun.pdfview.*;
import java.io.FileNotFoundException;

public class ShowerPDF extends JFrame
{
    PagePanel panelpdf;
    JFileChooser selector;
    PDFFile pdffile;
    int indice = 1;
    int numpag = 0;
    JButton bsiguiente;
 
    public ShowerPDF(String url) throws FileNotFoundException, IOException
    {
        panelpdf = new PagePanel();
        File file = new File (url);
        RandomAccessFile raf = new RandomAccessFile(file, "r");
        FileChannel channel = raf.getChannel();
        ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY,0, channel.size());
        pdffile = new PDFFile(buf);
        numpag = pdffile.getNumPages();
        PDFPage page = pdffile.getPage(indice);
        panelpdf.showPage(page);
        repaint();
        
        JPanel pabajo = new JPanel();
        bsiguiente = new JButton("Siguiente");
        bsiguiente.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                if(numpag!=indice)
                {
                    indice++;
                    PDFPage page = pdffile.getPage(indice);
                    panelpdf.showPage(page);
                }
            }

        });
        JButton banterior = new JButton("Anterior");
        banterior.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                if(numpag!=0)
                {
                    indice--;
                    PDFPage page = pdffile.getPage(indice);
                    panelpdf.showPage(page);
                }
            }
        });
        pabajo.add(banterior);
        pabajo.add(bsiguiente);
        add(panelpdf);
        add(pabajo,BorderLayout.SOUTH);
    }
 
    /*public static void main(String arg[]) throws FileNotFoundException, IOException
    {
        ShowerPDF p=new ShowerPDF("C:\\Users\\Jair\\Desktop\\Curriculum_Ingles.pdf");
        p.setDefaultCloseOperation(EXIT_ON_CLOSE);
        p.setVisible(true);
        p.setBounds(0, 0, 500, 500);
        p.setLocationRelativeTo(null);
    }*/
}
