/* Copyright 2014 Esri

 All rights reserved under the copyright laws of the United States
 and applicable international laws, treaties, and conventions.

 You may freely redistribute and use this sample code, with or
 without modification, provided you include the original copyright
 notice and use restrictions.

 See the use restrictions.*/
package GUI;

import DAO.Aeronave;
import SIG_ArcGIS.MouseClickedOverlay;
import com.esri.client.local.ArcGISLocalDynamicMapServiceLayer;
import com.esri.core.geometry.Envelope;
import com.esri.map.ArcGISTiledMapServiceLayer;
import com.esri.map.GPSLayer;
import com.esri.map.Grid;
import com.esri.map.JMap;
import com.esri.map.LayerList;
import com.esri.map.MapEvent;
import com.esri.map.MapEventListener;
import com.esri.map.MapEventListenerAdapter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * This application demonstrates how to listen to a {@link MapEvent} on the
 * JMap. Events on the JMap are monitored by providing a
 * {@link MapEventListener} to the JMap. If only certain methods are to be
 * overridden, a {@link MapEventListenerAdapter} can be provided instead. A
 * mapExtentChanged event fires once a zoom has completed, panning has
 * completed, or the extent has been changed via mouse navigation or
 * programmatically.
 * <p>
 * This application also shows how to programmatically pan, zoom, and set the
 * initial map extent.
 */
public abstract class General_GUI {

    private final Color BG_COLOR = new Color(100, 200, 100, 255);
    private static final int BUTTON_WIDTH = 300;

    // amount to pan by, in map units
    final static int panAmount = 1000000;

    // resources
    final static String URL_WORLD_BASEMAP = "http://services.arcgisonline.com/ArcGIS/rest/services/World_Topo_Map/MapServer";

    //current directory
    final private String currentPath = new java.io.File(".").getCanonicalPath();

    //variables
    private final String mgrs = "NaN";
    private final String velocidad = "NaN";
    private final String altitudValue = "NaN";

    //gps layer
    protected GPSLayer gpsLayer;

    //Map's Layers
    protected LayerList layersList;

    //General panel
    protected JComponent contentPane;

    // JMap
    protected final JMap map;

    //offset to strings
    private final String offset = "                                                                     ";

    //List of conected users
    List<Aeronave> aeronaves;
    
    //model digital elevaion
    protected ArcGISLocalDynamicMapServiceLayer dynamicLayer;

    //general submenus
    protected JScrollPane userSub;
    protected JScrollPane layerSub;
    protected JScrollPane docSub;
    
    private JPanel controlPanel;
    private InformationAirship panelInformation;

    // ------------------------------------------------------------------------
    // Constructors
    // ------------------------------------------------------------------------
    public General_GUI() throws Exception {
        // application content
        contentPane = createContentPane();

        // map
        map = createMap();

        // Panel airship information
        JPanel information = this.createInformationPanel();

        
        //General buttons 
        //button users and submenu 
        userSub = this.getUsuers_submenu();
        ButtonPanel users = new ButtonPanel("users", 10, 235, userSub);
        
        //button layer and submenu 
        layerSub = getLayer_submenu();
        ButtonPanel layers = new ButtonPanel("layers", 10, 325, layerSub);

        //button documents and submenu
        docSub = this.getDocuments_submenu();
        ButtonPanel mapsLayer = new ButtonPanel("documents", 10, 415, docSub);

        ButtonPanel documents = new ButtonPanel("map", 10, 505, null);
        ButtonPanel camera = new ButtonPanel("camera", 10, 595, null);

        // add the panel to the main window
        contentPane.add(information);
        
        contentPane.add(users);
        contentPane.add(userSub);

        contentPane.add(layers);
        contentPane.add(layerSub);

        contentPane.add(documents);
        contentPane.add(docSub);

        contentPane.add(mapsLayer);

        contentPane.add(camera);
        
        
        

    }

    /**
     * ********************************************************
     * Abstract methods
     *
     * ********************************************************* @return
     */
    //method to implements the button icons whiche will be at the application comandoCentral
    public abstract JComponent createUI();

    //show mgrs layer
    protected void showMGRSlayer() {
        map.getGrid().setVisibility(true);
    }

    //hide magrs layer
    protected void hideMGRSlayer() {
        map.getGrid().setVisibility(false);
    }

    // ------------------------------------------------------------------------
    // Private methods
    // ------------------------------------------------------------------------

    /*
     * Creating generals submenu panels
     */
    
    //Submenus button's users
    protected final JScrollPane getUsuers_submenu() {
        List<JComponent> componets = new ArrayList<>();

        final JCheckBox navegacion = new JCheckBox("Usuario 1" + offset);
        navegacion.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                if (!navegacion.isSelected()) {
                    gpsLayer.setMode(GPSLayer.Mode.NAVIGATION);
                } else {
                    gpsLayer.setMode(GPSLayer.Mode.OFF);
                }
            }
        });

        final JCheckBox estado = new JCheckBox("Uruario 2" + offset);
        estado.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                if (!estado.isSelected()) {
                    //startGPS("COM7");
                } else {
                    //stopGPS();
                }
            }
        });
        
        final JCheckBox uno = new JCheckBox("Uruario 3" + offset);
        uno.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                if (!estado.isSelected()) {
                    //startGPS("COM7");
                } else {
                    //stopGPS();
                }
            }
        });
        
        final JCheckBox dos = new JCheckBox("Uruario 3" + offset);
        dos.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                if (!estado.isSelected()) {
                    //startGPS("COM7");
                } else {
                    //stopGPS();
                }
            }
        });

        componets.add(navegacion);
        componets.add(estado);
        componets.add(uno);
        componets.add(dos);
        /*
        JPanel p = this.createSubmenu_panel(componets, true);
        p.setLayout(new GridLayout(4, 2));*/
        
        return new ScrollPanel(this.createSubmenu_panel(componets, true), 95, 235);
        
        

        //return this.createSubmenu_panel(95, 235, componets, true);
    }

    protected final JScrollPane getLayer_submenu() {
        List<JComponent> components = new ArrayList<>();

        //DEM Check box which is into the Layer submenu 
        final JCheckBox dem = new JCheckBox("DEM" + offset);
        dem.setSelected(true);
        dem.addMouseListener(new MouseAdapter() {
            @Override
            // avent to show and hide the DEM layer
            public void mousePressed(java.awt.event.MouseEvent evt) {
                if (!dem.isSelected()) {
                    dynamicLayer.setOpacity(0.5f);
                } else {
                    dynamicLayer.setOpacity(0.0f);
                }
            }
        });

        // MGRS Check box which is into the Layer submenu
        final JCheckBox mgrsLayer = new JCheckBox("MGRS" + offset);
        mgrsLayer.setSelected(true);
        mgrsLayer.addMouseListener(new MouseAdapter() {
            @Override
            // event to show and hide the MGRS layer
            public void mousePressed(java.awt.event.MouseEvent evt) {
                if (!mgrsLayer.isSelected()) {
                    map.getGrid().setVisibility(true);
                } else {                    
                    map.getGrid().setVisibility(false);
                }
            }
        });

        //Adding the components to the list, which will be added to the submenu panel
        components.add(dem);
        components.add(mgrsLayer);

        //creating the submenu: setting postition, list of components
        return new ScrollPanel(this.createSubmenu_panel(components, true), 95, 325);
    }

    protected final JScrollPane getDocuments_submenu() {
        List<JComponent> components = new ArrayList<>();
        final JCheckBox navegacion = new JCheckBox("Doc 1" + offset);
        navegacion.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                if (!navegacion.isSelected()) {
                    gpsLayer.setMode(GPSLayer.Mode.NAVIGATION);
                } else {
                    gpsLayer.setMode(GPSLayer.Mode.OFF);
                }
            }
        });

        final JCheckBox estado = new JCheckBox("Doc 2" + offset);
        estado.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                if (!estado.isSelected()) {
                    //startGPS("COM7");
                } else {
                    //stopGPS();
                }
            }
        });

        components.add(navegacion);
        components.add(estado);

        return new ScrollPanel(this.createSubmenu_panel(components, true), 95, 415);
    }

    //create submenu Jpanel
    protected JPanel createSubmenu_panel(List<JComponent> components, boolean group) {
        JPanel controlPanelSubmenu = new JPanel();
        BoxLayout boxLayout = new BoxLayout(controlPanelSubmenu, BoxLayout.Y_AXIS);
        controlPanelSubmenu.setBorder(new javax.swing.border.LineBorder(new Color(0, 0, 0), 0, true));
        controlPanelSubmenu.setLayout(boxLayout);
        controlPanelSubmenu.setLocation(0, 0);

        Color color = new Color(0, 116, 166, 255);
        controlPanelSubmenu.setBackground(new Color(0, 0, 0, 60));

        for (JComponent component : components) {
            component.setBackground(color);
            component.setForeground(Color.WHITE);

            controlPanelSubmenu.add(component);
            controlPanelSubmenu.add(Box.createRigidArea(new Dimension(0, 3)));
        }

        controlPanelSubmenu.setVisible(false);

        return controlPanelSubmenu;
    }


    /*
     * Crea el panel que contiene la informacion obtenida por el GPS
     */
    private JPanel createInformationPanel() {
        // group the above UI items into a panel
        
        
        panelInformation = new InformationAirship();
        controlPanel = panelInformation;
        
        controlPanel.setLocation(10, 5);
        controlPanel.setSize(BUTTON_WIDTH + 10, 135);
        controlPanel.setBackground(new Color(0, 0, 0, 200));
        

        return controlPanel;
    }

    /**
     * Creates a window.
     *
     * @return a window.
     */
    protected JFrame createWindow() {
        JFrame window = new JFrame("Map Events Application");
        window.setBounds(100, 100, 1000, 700);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.getContentPane().setLayout(new BorderLayout(0, 0));

        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                super.windowClosing(windowEvent);
                map.dispose();
            }
        });
        return window;
    }

    /**
     * Creates a content pane.
     *
     * @return a content pane.
     */
    private static JLayeredPane createContentPane() {
        JLayeredPane contentPane = new JLayeredPane();
        contentPane.setBounds(100, 100, 1000, 700);
        contentPane.setLayout(new BorderLayout(0, 0));
        contentPane.setVisible(true);
        return contentPane;
    }

    /**
     * Creates a map.
     *
     * @return a map.
     */
    private JMap createMap() throws Exception {
        //final map
        JMap jMap = new JMap();

        //Layer lists
        layersList = jMap.getLayers();

        //Base map
        final ArcGISTiledMapServiceLayer baseLayer = new ArcGISTiledMapServiceLayer(URL_WORLD_BASEMAP);
        layersList.add(baseLayer);

        //Extention
        jMap.setExtent(new Envelope(-11257768.21, 2127705.43, -10840965.29, 2343124.81));

        //Dinamic Layer: Digital Elevation Model
        String path = this.currentPath + "\\build\\classes\\Archivos\\Mapas\\mapa.mpk";
        dynamicLayer = new ArcGISLocalDynamicMapServiceLayer(path);
        dynamicLayer.setOpacity(0.5f);
        layersList.add(dynamicLayer);

        jMap.getGrid().setType(Grid.GridType.MGRS);

        // listen to mouse events for drawing the interesting point. 
        jMap.addMapOverlay(new MouseClickedOverlay(contentPane));
        return jMap;
    }

    public InformationAirship getControlPanel() {
        return panelInformation;
    }   
    
}
