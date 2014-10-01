/* Copyright 2014 Esri

 All rights reserved under the copyright laws of the United States
 and applicable international laws, treaties, and conventions.

 You may freely redistribute and use this sample code, with or
 without modification, provided you include the original copyright
 notice and use restrictions.

 See the use restrictions.*/
package GUI;

import DAO.Aeronave;
import DAO.Posicion;
import DAO.PuntoInteres;
import JMS_ActiveMQ.Publicador;
import SIG_ArcGIS.MouseClickedOverlay;
import com.esri.client.local.ArcGISLocalDynamicMapServiceLayer;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.FontWeight;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol.Style;
import com.esri.core.symbol.TextSymbol;
import com.esri.map.ArcGISTiledMapServiceLayer;
import com.esri.map.GPSLayer;
import com.esri.map.GraphicsLayer;
import com.esri.map.Grid;
import com.esri.map.JMap;
import com.esri.map.LayerList;
import com.esri.map.MapEvent;
import com.esri.map.MapEventListener;
import com.esri.map.MapEventListenerAdapter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
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

    //model digital elevaion
    protected ArcGISLocalDynamicMapServiceLayer dynamicLayer;

    //general submenus
    protected JScrollPane userSub;
    protected JScrollPane layerSub;
    protected JScrollPane docSub;

    //button users
    protected ButtonPanel users;

    private JPanel controlPanel;
    private InformationAirship panelInformation;

    //Graphic Layer
    protected GraphicsLayer graphicsLayer;

    //Graphic Layer update airships
    protected GraphicsLayer graphicsLayerAirship;

    //Interested points Array 
    protected List<PuntoInteres> puntosInteres;

    //type of user 
    protected int typeUser;

    //user selected 
    protected String idAeronave;
    
    //message publisher 
    protected Publicador publicador;
    
    protected HashMap<String, Aeronave> mision;

    // ------------------------------------------------------------------------
    // Constructors
    // ------------------------------------------------------------------------
    public General_GUI() throws Exception {
        // application content
        contentPane = createContentPane();

        puntosInteres = new ArrayList<PuntoInteres>();

        // map
        map = createMap();

        // Panel airship information
        JPanel information = this.createInformationPanel();

        //General buttons 
        //button users and submenu 
        HashMap<String, Aeronave> mision = new HashMap<>();
        userSub = this.getUsuers_submenu(mision);
        users = new ButtonPanel("users", 10, 235, userSub);

        //button layer and submenu 
        layerSub = getLayer_submenu();
        ButtonPanel layers = new ButtonPanel("layers", 10, 325, layerSub);

        //button documents and submenu
        docSub = this.getDocuments_submenu();
        ButtonPanel mapsLayer = new ButtonPanel("documents", 10, 415, docSub);

        //ButtonPanel documents = new ButtonPanel("map", 10, 595, null);
        ButtonPanel camera = new ButtonPanel("camera", 10, 505, null);

        // add the panel to the main window
        contentPane.add(information);

        contentPane.add(users);
        contentPane.add(userSub);

        contentPane.add(layers);
        contentPane.add(layerSub);

        //contentPane.add(documents);
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
    public void updateUsersPanel(HashMap<String, Aeronave> mision) throws IOException, InterruptedException {

        JPanel updatedPanel = this.updateUserPanel(mision);
        userSub.setViewportView(updatedPanel);

        updatedPanel.setVisible(false);
        Thread.sleep(10);
        updatedPanel.setVisible(true);
    }

    //Submenus button's users
    public final JScrollPane getUsuers_submenu(HashMap<String, Aeronave> mision) {
        List<JComponent> componets = new ArrayList<>();
        return new ScrollPanel(this.createSubmenu_panel(componets, true), 95, 235);
    }

    //function to update the information which is on the scroll
    public JPanel updateUserPanel(HashMap<String, Aeronave> mision) {
        List<JComponent> componets = new ArrayList<>();

        String key;
        Aeronave aeronave;
        JPanel pUsuario;

        for (Map.Entry<String, Aeronave> entry : mision.entrySet()) {

            key = entry.getKey();
            aeronave = entry.getValue();

            if (!"comando".equals(key)) {
                pUsuario = new UserPanel(aeronave.getColor(), key, this);
                componets.add(pUsuario);
            }
        }
        return this.createSubmenu_panel(componets, true);
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

        // Points box which is into the Layer submenu
        final JCheckBox points = new JCheckBox("Puntos Interes" + offset);
        points.setSelected(true);
        points.addMouseListener(new MouseAdapter() {
            @Override
            // event to show and hide the MGRS layer
            public void mousePressed(java.awt.event.MouseEvent evt) {
                if (!points.isSelected()) {
                    graphicsLayer.setVisible(true);
                } else {
                    graphicsLayer.setVisible(false);
                }
            }
        });

        //Adding the components to the list, which will be added to the submenu panel
        components.add(dem);
        components.add(mgrsLayer);
        components.add(points);

        return new ScrollPanel(this.createSubmenu_panel(components, true), 95, 325);
    }

    protected final JScrollPane getDocuments_submenu() {
       List<JComponent> components = new ArrayList<>();
        
        final JCheckBox a1 = new JCheckBox("Acapulco" + offset);
        a1.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt){
                if(!a1.isSelected()){
                    try{
                        createPDF_Viewer("ACA.pdf", "Acapulco");
                    }catch (IOException ex){
                        Logger.getLogger(General_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }}}
        });

        final JCheckBox a2 = new JCheckBox("Aguascalientes" + offset);
        a2.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt){
                if(!a2.isSelected()){
                    try{
                        createPDF_Viewer("AGU.pdf", "Aguascalientes");
                    }catch (IOException ex){
                        Logger.getLogger(General_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }}}
        });
        
        final JCheckBox a3 = new JCheckBox("Leon" + offset);
        a3.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt){
                if(!a3.isSelected()){
                    try{
                        createPDF_Viewer("BJX.pdf", "Leon");
                    }catch (IOException ex){
                        Logger.getLogger(General_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }}}
        });
        
        final JCheckBox a4 = new JCheckBox("Cabo San Lucas" + offset);
        a4.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt){
                if(!a4.isSelected()){
                    try{
                        createPDF_Viewer("CBS.pdf", "Cabo San Lucas");
                    }catch (IOException ex){ 
                        Logger.getLogger(General_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }}}
        });
        
        final JCheckBox a5 = new JCheckBox("Ciudad Obregon" + offset);
        a5.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt){
                if(!a5.isSelected()){
                    try{
                        createPDF_Viewer("CEN.pdf", "Ciudad Obregon");
                    }catch (IOException ex){
                        Logger.getLogger(General_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }}}    
        });
        
        final JCheckBox a6 = new JCheckBox("Ciudad Juarez" + offset);
        a6.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt){
                if(!a6.isSelected()){
                    try{
                        createPDF_Viewer("CJS.pdf", "Ciudad Juarez");
                    }catch (IOException ex){
                        Logger.getLogger(General_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }}}
        });
        
        final JCheckBox a7 = new JCheckBox("Colima" + offset);
        a7.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt){
                if(!a7.isSelected()){
                    try{
                        createPDF_Viewer("CLQ.pdf", "Colima");
                    }catch (IOException ex){
                        Logger.getLogger(General_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }}}
        });
        
        final JCheckBox a8 = new JCheckBox("Ciudad del Carmen" + offset);
        a8.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt){
                if(!a8.isSelected()){
                    try{
                        createPDF_Viewer("CME.pdf", "Ciudad del Carmen");
                    }catch (IOException ex){
                        Logger.getLogger(General_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }}}
        });
        
        final JCheckBox a9 = new JCheckBox("Campeche" + offset);
        a9.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt){
                if(!a9.isSelected()){
                    try {
                        createPDF_Viewer("CPE.pdf", "Ciudad del Carmen");
                    }catch (IOException ex){
                        Logger.getLogger(General_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }}}
        });
        
        final JCheckBox a10 = new JCheckBox("Chetumal" + offset);
        a10.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt){
                if(!a10.isSelected()){
                    try {
                        createPDF_Viewer("CTM.pdf", "Chetumal");
                    }catch (IOException ex){
                        Logger.getLogger(General_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }}}
        });
        
        final JCheckBox a11 = new JCheckBox("Culiacan" + offset);
        a11.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt){
                if(!a11.isSelected()){
                    try {
                        createPDF_Viewer("CUL.pdf", "Culiacan");
                    }catch (IOException ex){
                        Logger.getLogger(General_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }}}
        });
        
        final JCheckBox a12 = new JCheckBox("Cancun" + offset);
        a12.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt){
                if(!a12.isSelected()){
                    try {
                        createPDF_Viewer("CUN.pdf", "Cancun");
                    }catch (IOException ex){
                        Logger.getLogger(General_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }}}
        });
        
        final JCheckBox a13 = new JCheckBox("Chihuaha" + offset);
        a13.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt){
                if(!a13.isSelected()){
                    try {
                        createPDF_Viewer("CUU.pdf", "Chihuaha");
                    }catch (IOException ex){
                        Logger.getLogger(General_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }}}
        });
        
        final JCheckBox a14 = new JCheckBox("Cuernavaca" + offset);
        a14.addMouseListener(new MouseAdapter(){
            public void mousePressed(java.awt.event.MouseEvent evt){
                if(!a14.isSelected()){
                    try {
                        createPDF_Viewer("CVJ.pdf", "Cuernavaca");
                    }catch (IOException ex){
                        Logger.getLogger(General_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }}}
        });
        
        final JCheckBox a15 = new JCheckBox("Ciudad Vitoria" + offset);
        a15.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt){
                if(!a15.isSelected()){
                    try {
                        createPDF_Viewer("CVM.pdf", "Ciudad Vitoria");
                    }catch (IOException ex){
                        Logger.getLogger(General_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }}}
        });
        
        final JCheckBox a16 = new JCheckBox("Chichen-Itza" + offset);
        a16.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt){
                if(!a16.isSelected()){
                    try {
                        createPDF_Viewer("CZA.pdf", "Chichen-Itza");
                    }catch (IOException ex){
                        Logger.getLogger(General_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }}}
        });
        
        final JCheckBox a17 = new JCheckBox("Cozumel" + offset);
        a17.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt){
                if(!a17.isSelected()){
                    try {
                        createPDF_Viewer("CZM.pdf", "Cozumel");
                    }catch (IOException ex){
                        Logger.getLogger(General_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }}}
        });
        
        final JCheckBox a18 = new JCheckBox("Durango" + offset);
        a18.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt){
                if(!a18.isSelected()){
                    try {
                        createPDF_Viewer("DGO.pdf", "Durango");
                    }catch (IOException ex){
                        Logger.getLogger(General_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }}}
        });
        
        final JCheckBox a19 = new JCheckBox("Ensenada" + offset);
        a19.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt){
                if(!a19.isSelected()){
                    try {
                        createPDF_Viewer("ESE.pdf", "Ensenada");
                    }catch (IOException ex){
                        Logger.getLogger(General_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }}}
        });
        
        final JCheckBox a20 = new JCheckBox("Guadalajara" + offset);
        a20.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt){
                if(!a20.isSelected()){
                    try {
                        createPDF_Viewer("GDL.pdf", "Guadalajara");
                    }catch (IOException ex){
                        Logger.getLogger(General_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }}}
        });
        
        final JCheckBox a21 = new JCheckBox("Guaymas" + offset);
        a21.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt){
                if(!a21.isSelected()){
                    try {
                        createPDF_Viewer("GYM.pdf", "Guaymas");
                    }catch (IOException ex){
                        Logger.getLogger(General_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }}}
        });
        
        final JCheckBox a22 = new JCheckBox("Hermosillo" + offset);
        a22.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt){
                if(!a22.isSelected()){
                    try {
                        createPDF_Viewer("HMO.pdf", "Hermosillo");
                    }catch (IOException ex){
                        Logger.getLogger(General_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }}}
        });
        final JCheckBox a23 = new JCheckBox("Bahias de Huatulco" + offset);
        a23.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt){
                if(!a23.isSelected()){
                    try {
                        createPDF_Viewer("HUX.pdf", "Bahias de Huatulco");
                    }catch (IOException ex){
                        Logger.getLogger(General_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }}}
        });
        
        final JCheckBox a24 = new JCheckBox("La Paz" + offset);
        a24.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt){
                if(!a24.isSelected()){
                    try {
                        createPDF_Viewer("LAP.pdf", "La Paz");
                    }catch (IOException ex){
                        Logger.getLogger(General_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }}}
        });
        
        final JCheckBox a25 = new JCheckBox("Los Mochis" + offset);
        a25.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt){
                if(!a25.isSelected()){
                    try {
                        createPDF_Viewer("LMM.pdf", "Los Mochis");
                    }catch (IOException ex){
                        Logger.getLogger(General_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }}}
        });
        
        final JCheckBox a26 = new JCheckBox("Monclova" + offset);
        a26.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt){
                if(!a26.isSelected()){
                    try {
                        createPDF_Viewer("LOV.pdf", "Monclova");
                    }catch (IOException ex){
                        Logger.getLogger(General_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }}}
        });
        
        final JCheckBox a27 = new JCheckBox("Loreto" + offset);
        a27.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt){
                if(!a27.isSelected()){
                    try {
                        createPDF_Viewer("LTO.pdf", "Loreto");
                    }catch (IOException ex){
                        Logger.getLogger(General_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }}}
        });
        
        final JCheckBox a28 = new JCheckBox("Matamoros" + offset);
        a28.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt){
                if(!a28.isSelected()){
                    try {
                        createPDF_Viewer("MAM.pdf", "Matamoros");
                    }catch (IOException ex){
                        Logger.getLogger(General_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }}}
        });
        
        final JCheckBox a29 = new JCheckBox("Ciudad de Mexico" + offset);
        a29.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt){
                if(!a29.isSelected()){
                    try {
                        createPDF_Viewer("MEX.pdf", "Ciudad de Mexico");
                    }catch (IOException ex){
                        Logger.getLogger(General_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }}}
        });
        
        final JCheckBox a30 = new JCheckBox("Merida" + offset);
        a30.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt){
                if(!a30.isSelected()){
                    try {
                        createPDF_Viewer("MID.pdf", "Merida");
                    }catch (IOException ex){
                        Logger.getLogger(General_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }}}
        });
        
        final JCheckBox a31 = new JCheckBox("Morelia" + offset);
        a31.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt){
                if(!a31.isSelected()){
                    try {
                        createPDF_Viewer("MLM.pdf", "Morelia");
                    }catch (IOException ex){
                        Logger.getLogger(General_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }}}
        });
        
        final JCheckBox a32 = new JCheckBox("Minatitlan" + offset);
        a32.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt){
                if(!a32.isSelected()){
                    try {
                        createPDF_Viewer("MTT.pdf", "Minatitlan");
                    }catch (IOException ex){
                        Logger.getLogger(General_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }}}
        });
        
        final JCheckBox a33 = new JCheckBox("Monterrey" + offset);
        a33.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt){
                if(!a33.isSelected()){
                    try {
                        createPDF_Viewer("MTY.pdf", "Monterrey");
                    }catch (IOException ex){
                        Logger.getLogger(General_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }}}
        });
        
        final JCheckBox a34 = new JCheckBox("Mexicali" + offset);
        a34.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt){
                if(!a34.isSelected()){
                    try {
                        createPDF_Viewer("MXL.pdf", "Mexicali");
                    }catch (IOException ex){
                        Logger.getLogger(General_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }}}
        });
        
        final JCheckBox a35 = new JCheckBox("Mazatlan" + offset);
        a35.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt){
                if(!a35.isSelected()){
                    try {
                        createPDF_Viewer("MZT.pdf", "Mazatlan");
                    }catch (IOException ex){
                        Logger.getLogger(General_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }}}
        });
        
        final JCheckBox a36 = new JCheckBox("Nuevo Laredo" + offset);
        a36.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt){
                if(!a36.isSelected()){
                    try {
                        createPDF_Viewer("NLD.pdf", "Nuevo Laredo");
                    }catch (IOException ex){
                        Logger.getLogger(General_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }}}
        });
        
        final JCheckBox a37 = new JCheckBox("Monterrey" + offset);
        a37.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt){
                if(!a37.isSelected()){
                    try {
                        createPDF_Viewer("NTR.pdf", "Monterrey");
                    }catch (IOException ex){
                        Logger.getLogger(General_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }}}
        });
        
        final JCheckBox a38 = new JCheckBox("Oaxaca" + offset);
        a38.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt){
                if(!a38.isSelected()){
                    try {
                        createPDF_Viewer("OAX.pdf", "Oaxaca");
                    }catch (IOException ex){
                        Logger.getLogger(General_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }}}
        });
        
        final JCheckBox a39 = new JCheckBox("Poza Rica" + offset);
        a39.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt){
                if(!a39.isSelected()){
                    try {
                        createPDF_Viewer("PAZ.pdf", "Poza Rica");
                    }catch (IOException ex){
                        Logger.getLogger(General_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }}}
        });
        
        final JCheckBox a40 = new JCheckBox("Puebla" + offset);
        a40.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt){
                if(!a40.isSelected()){
                    try {
                        createPDF_Viewer("PBC.pdf", "Puebla");
                    }catch (IOException ex){
                        Logger.getLogger(General_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }}}
        });
        
        final JCheckBox a41 = new JCheckBox("Piedras Negras" + offset);
        a4.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt){
                if(!a41.isSelected()){
                    try {
                        createPDF_Viewer("PNG.pdf", "Piedras Negras");
                    }catch (IOException ex){
                        Logger.getLogger(General_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }}}
        });
        
        final JCheckBox a42 = new JCheckBox("Puerto Peñasco" + offset);
        a42.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt){
                if(!a42.isSelected()){
                    try {
                        createPDF_Viewer("PPE.pdf", "Puerto Peñasco");
                    }catch (IOException ex){
                        Logger.getLogger(General_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }}}
        });
        
        final JCheckBox a43 = new JCheckBox("Puerto Vallarta" + offset);
        a43.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt){
                if(!a43.isSelected()){
                    try {
                        createPDF_Viewer("PVR.pdf", "Puerto Vallarta");
                    }catch (IOException ex){
                        Logger.getLogger(General_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }}}
        });
        
        final JCheckBox a44 = new JCheckBox("Puerto Escondido" + offset);
        a44.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt){
                if(!a44.isSelected()){
                    try {
                        createPDF_Viewer("PXM.pdf", "Puerto Escondido");
                    }catch (IOException ex){
                        Logger.getLogger(General_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }}}
        });
        
        final JCheckBox a45 = new JCheckBox("Queretaro" + offset);
        a45.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt){
                if(!a45.isSelected()){
                    try {
                        createPDF_Viewer("QRO.pdf", "Queretaro");
                    }catch (IOException ex){
                        Logger.getLogger(General_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }}}
        });

        final JCheckBox a46 = new JCheckBox("Reynoa" + offset);
        a46.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt){
                if(!a46.isSelected()){
                    try {
                        createPDF_Viewer("REX.pdf", "Reynoa");
                    }catch (IOException ex){
                        Logger.getLogger(General_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }}}
        });
        
        final JCheckBox a47 = new JCheckBox("San Jose del Cabo" + offset);
        a47.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt){
                if(!a47.isSelected()){
                    try {
                        createPDF_Viewer("SJD.pdf", "San Jose del Cabo");
                    }catch (IOException ex){
                        Logger.getLogger(General_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }}}
        });
        
        final JCheckBox a48 = new JCheckBox("Saltillo" + offset);
        a48.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt){
                if(!a48.isSelected()){
                    try {
                        createPDF_Viewer("SLW.pdf", "Saltillo");
                    }catch (IOException ex){
                        Logger.getLogger(General_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }}}
        });
        
        final JCheckBox a49 = new JCheckBox("Tampico" + offset);
        a49.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt){
                if(!a49.isSelected()){
                    try {
                        createPDF_Viewer("TAM.pdf", "Tampico");
                    }catch (IOException ex){
                        Logger.getLogger(General_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }}}
        });
        
        final JCheckBox a50 = new JCheckBox("Tapachula" + offset);
        a50.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt){
                if(!a50.isSelected()){
                    try {
                        createPDF_Viewer("TAP.pdf", "Tapachula");
                    }catch (IOException ex){
                        Logger.getLogger(General_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }}}
        });
        
        final JCheckBox a51 = new JCheckBox("Tuxtla Gutierrez" + offset);
        a51.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt){
                if(!a51.isSelected()){
                    try {
                        createPDF_Viewer("TGZ.pdf", "Tuxtla Gutierrez");
                    }catch (IOException ex){
                        Logger.getLogger(General_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }}}
        });
        
        final JCheckBox a52 = new JCheckBox("Tijuana" + offset);
        a52.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt){
                if(!a52.isSelected()){
                    try {
                        createPDF_Viewer("TIJ.pdf", "Tijuana");
                    }catch (IOException ex){
                        Logger.getLogger(General_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }}}
        });
        
        final JCheckBox a53 = new JCheckBox("Toluca" + offset);
        a53.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt){
                if(!a53.isSelected()){
                    try {
                        createPDF_Viewer("TLC.pdf", "Toluca");
                    }catch (IOException ex){
                        Logger.getLogger(General_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }}}
        });
        
        final JCheckBox a54 = new JCheckBox("Tepic" + offset);
        a54.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt){
                if(!a54.isSelected()){
                    try {
                        createPDF_Viewer("TPQ.pdf", "Tepic");
                    }catch (IOException ex){
                        Logger.getLogger(General_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }}}
        });
        
        final JCheckBox a55 = new JCheckBox("Torreon" + offset);
        a55.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt){
                if(!a55.isSelected()){
                    try {
                        createPDF_Viewer("TRC.pdf", "Torreon");
                    }catch (IOException ex){
                        Logger.getLogger(General_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }}}
        });
        
        final JCheckBox a56 = new JCheckBox("Tamuin" + offset);
        a56.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt){
                if(!a56.isSelected()){
                    try {
                        createPDF_Viewer("TSL.pdf", "Tamuin");
                    }catch (IOException ex){
                        Logger.getLogger(General_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }}}
        });
        
        final JCheckBox a57 = new JCheckBox("Uruapan" + offset);
        a57.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt){
                if(!a57.isSelected()){
                    try {
                        createPDF_Viewer("UPN.pdf", "Uruapan");
                    }catch (IOException ex){
                        Logger.getLogger(General_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }}}
        });
        
        final JCheckBox a58 = new JCheckBox("Veracuz" + offset);
        a58.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt){
                if(!a58.isSelected()){
                    try {
                        createPDF_Viewer("VER.pdf", "Veracuz");
                    }catch (IOException ex){
                        Logger.getLogger(General_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }}}
        });
        
        final JCheckBox a59 = new JCheckBox("Villa Hermosa" + offset);
        a59.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt){
                if(!a59.isSelected()){
                    try {
                        createPDF_Viewer("VSA.pdf", "Villa Hermosa");
                    }catch (IOException ex){
                        Logger.getLogger(General_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }}}
        });
        
        final JCheckBox a60 = new JCheckBox("Zacatecas" + offset);
        a60.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt){
                if(!a60.isSelected()){
                    try {
                        createPDF_Viewer("ZCL.pdf", "Zacatecas");
                    }catch (IOException ex){
                        Logger.getLogger(General_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }}}
        });
        
        final JCheckBox a61 = new JCheckBox("Ixtapa-Zihuatanejo" + offset);
        a61.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt){
                if(!a61.isSelected()){
                    try {
                        createPDF_Viewer("ZIH.pdf", "Ixtapa-Zihuatanejo");
                    }catch (IOException ex){
                        Logger.getLogger(General_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }}}
        });
        
        final JCheckBox a62 = new JCheckBox("Manzanillo" + offset);
        a62.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt){
                if(!a62.isSelected()){
                    try {
                        createPDF_Viewer("ZLO.pdf", "Manzanillo");
                    }catch (IOException ex){
                        Logger.getLogger(General_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }}}
        });
        
        components.add(a1);
        components.add(a2);
        components.add(a3);
        components.add(a4);
        components.add(a5);
        components.add(a6);
        components.add(a7);
        components.add(a8);
        components.add(a9);
        components.add(a10);
        components.add(a11);
        components.add(a12);
        components.add(a13);
        components.add(a14);
        components.add(a15);
        components.add(a16);
        components.add(a17);
        components.add(a18);
        components.add(a19);
        components.add(a20);
        components.add(a21);
        components.add(a22);
        components.add(a23);
        components.add(a24);
        components.add(a25);
        components.add(a26);
        components.add(a27);
        components.add(a28);
        components.add(a29);
        components.add(a30);
        components.add(a31);
        components.add(a31);
        components.add(a32);
        components.add(a33);
        components.add(a34);
        components.add(a35);
        components.add(a36);
        components.add(a37);
        components.add(a38);
        components.add(a39);
        components.add(a40);
        components.add(a41);
        components.add(a42);
        components.add(a43);
        components.add(a44);
        components.add(a45);
        components.add(a46);
        components.add(a47);
        components.add(a48);
        components.add(a49);
        components.add(a50);
        components.add(a51);
        components.add(a51);
        components.add(a52);
        components.add(a53);
        components.add(a54);
        components.add(a55);
        components.add(a56);
        components.add(a57);
        components.add(a58);
        components.add(a59);
        components.add(a60);
        components.add(a61);
        components.add(a62);
        
        return new ScrollPanel(this.createSubmenu_panel(components, true), 95, 415);
    }
    
    //Create PDF Viewer
    protected ShowerPDF createPDF_Viewer(String path, String title) throws IOException
    {
        ShowerPDF pdf = new ShowerPDF(new java.io.File(".").getCanonicalPath() + "\\build\\classes\\Archivos\\Documentos\\" + path);
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        pdf.setBounds(0, 0, pantalla.height, pantalla.width/2);
        pdf.setTitle(title);
        pdf.setLocationRelativeTo(null);
        pdf.setVisible(true);
        return pdf;
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

    //method which paints mark points
    public void addGraphicPointAirShip(Point point, Color color, String simbolText) {
        this.graphicsLayer.setVisible(true);

        SimpleMarkerSymbol blueDiamond = new SimpleMarkerSymbol(Color.DARK_GRAY, 26, Style.CIRCLE);
        SimpleMarkerSymbol xCircle = new SimpleMarkerSymbol(color, 32, Style.CIRCLE);
        TextSymbol textSymbol = new TextSymbol(13, simbolText, Color.WHITE);
        textSymbol.setFontWeight(FontWeight.BOLD);

        ((GraphicsLayer) this.map.getLayers().get(2)).addGraphic(new Graphic(point, xCircle));
        ((GraphicsLayer) this.map.getLayers().get(2)).addGraphic(new Graphic(point, blueDiamond));
        ((GraphicsLayer) this.map.getLayers().get(2)).addGraphic(new Graphic(point, textSymbol));

        Posicion posicion = new Posicion(point.getX() + "", point.getY() + "", "", "", "", "");

        this.puntosInteres = new ArrayList<>();
        this.puntosInteres.add(new PuntoInteres(simbolText, posicion, 0));

    }
    
    //method which paints mark points
    public void addGraphicPointCommand(Point point, Color color, String simbolText) {
        this.graphicsLayer.setVisible(true);

        SimpleMarkerSymbol blueDiamond = new SimpleMarkerSymbol(Color.DARK_GRAY, 26, Style.CIRCLE);
        SimpleMarkerSymbol xCircle = new SimpleMarkerSymbol(color, 32, Style.CIRCLE);
        TextSymbol textSymbol = new TextSymbol(13, simbolText, Color.WHITE);
        textSymbol.setFontWeight(FontWeight.BOLD);

        ((GraphicsLayer) this.map.getLayers().get(2)).addGraphic(new Graphic(point, xCircle));
        ((GraphicsLayer) this.map.getLayers().get(2)).addGraphic(new Graphic(point, blueDiamond));
        ((GraphicsLayer) this.map.getLayers().get(2)).addGraphic(new Graphic(point, textSymbol));

    }

    public void paintMissionLayer(String anfitrion, HashMap<String, Aeronave> mision) {

        this.mision = mision;
        
        this.graphicsLayerAirship.removeAll();
        SimpleMarkerSymbol circle;
        Aeronave aeronave;
        Posicion posicion;
        Point mapPoint;
        Point point;
        String key;

        for (Map.Entry<String, Aeronave> entry : mision.entrySet()) {
            key = entry.getKey();
            aeronave = entry.getValue();

            if (!key.equals(anfitrion)) {
                posicion = aeronave.getPosicion();
                if (posicion != null) {
                    point = new Point(Double.valueOf(
                            posicion.getLongitud()),
                            Double.valueOf(posicion.getLatitud()));

                    mapPoint = (Point) GeometryEngine.project(
                            point,
                            SpatialReference.create(4326),
                            this.map.getSpatialReference());

                    circle = new SimpleMarkerSymbol(aeronave.getColor(), 22, Style.CIRCLE);
                    this.graphicsLayerAirship.addGraphic(new Graphic(mapPoint, circle));

                    for (PuntoInteres puntoInteres : aeronave.getPuntosInteres()) {
                        Point pointInteres = new Point(
                                Double.parseDouble(puntoInteres.getPosicion().getLatitud()),
                                Double.parseDouble(puntoInteres.getPosicion().getLongitud()));
                        this.addGraphicPointAirShip(pointInteres, aeronave.getColor(), puntoInteres.getDescripcion());
                    }
                }

            }
        }
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

        controlPanel.repaint(1);

        return controlPanel;
    }

    /**
     * Creates a window.
     *
     * @return a window.
     */
    public JFrame createWindow() {
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

        //Extention
        jMap.setExtent(new Envelope(-11257768.21, 2127705.43, -10840965.29, 2343124.81));

        //Layer lists
        layersList = jMap.getLayers();

        //Base map Layer: 0
        final ArcGISTiledMapServiceLayer baseLayer = new ArcGISTiledMapServiceLayer(URL_WORLD_BASEMAP);
        layersList.add(baseLayer);

        //Dinamic Layer(Digital Elevation Model) : 1
        String path = this.currentPath + "\\build\\classes\\Archivos\\Mapas\\mapa.mpk";
        dynamicLayer = new ArcGISLocalDynamicMapServiceLayer(path);
        dynamicLayer.setOpacity(0.5f);
        layersList.add(dynamicLayer);

        //Graphics Layer : 2
        this.graphicsLayer = new GraphicsLayer();
        layersList.add(this.graphicsLayer);

        //Graphic Layer Airships: 3
        this.graphicsLayerAirship = new GraphicsLayer();
        layersList.add(this.graphicsLayerAirship);

        jMap.getGrid().setType(Grid.GridType.MGRS);

        // listen to mouse events for drawing the interesting point. 
        jMap.addMapOverlay(new MouseClickedOverlay(this, this.typeUser));
        return jMap;
    }

    public InformationAirship getControlPanel() {
        return panelInformation;
    }

    public JComponent getContentPane() {
        return contentPane;
    }

    public void setContentPane(JComponent contentPane) {
        this.contentPane = contentPane;
    }

    public List<PuntoInteres> getPuntosInteres() {
        return puntosInteres;
    }

    public GPSLayer getGpsLayer() {
        return gpsLayer;
    }

    public JMap getMap() {
        return map;
    }

    public void setPuntosInteres(List<PuntoInteres> puntosInteres) {
        this.puntosInteres = puntosInteres;
    }

    public int getTypeUser() {
        return typeUser;
    }

    public void setTypeUser(int typeUser) {
        this.typeUser = typeUser;
    }

    public JScrollPane getUserSub() {
        return userSub;
    }

    public void setUserSub(JScrollPane userSub) {
        this.userSub = userSub;
    }

    public ButtonPanel getUsers() {
        return users;
    }

    public void setUsers(ButtonPanel users) {
        this.users = users;
    }

    public void setIdAeronave(String idAeronave) {
        this.idAeronave = idAeronave;
    }

    public String getIdAeronave() {
        return idAeronave;
    }

    public Publicador getPublicador() {
        return publicador;
    }

    public void setPublicador(Publicador publicador) {
        this.publicador = publicador;
    }

    public HashMap<String, Aeronave> getMision() {
        return mision;
    }

    public void setMision(HashMap<String, Aeronave> mision) {
        this.mision = mision;
    }
    
    

}
