/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import com.esri.core.geometry.Geometry;
import com.esri.core.tasks.ags.identify.IdentifyParameters;
import com.esri.core.tasks.ags.identify.IdentifyResult;
import com.esri.core.tasks.ags.identify.IdentifyTask;
import com.esri.map.JMap;
import java.awt.Toolkit;
import java.util.Map;

/**
 *
 * @author Jair
 */
public class DEM {

    private JMap jMap;

    public DEM(JMap jMap) {
        this.jMap = jMap;
    }

    public String getPixelElevationValue(Geometry geometry) {
        String pixelValue = "";
        IdentifyParameters identifyparam = new IdentifyParameters();
        identifyparam.setGeometry(geometry);
        identifyparam.setMapExtent(jMap.getExtent());
        identifyparam.setSpatialReference(jMap.getSpatialReference());
        identifyparam.setMapHeight(jMap.getHeight());
        identifyparam.setMapWidth(jMap.getWidth());
        identifyparam.setLayerMode(IdentifyParameters.ALL_LAYERS);
        identifyparam.setDPI(Toolkit.getDefaultToolkit().getScreenResolution());

        IdentifyTask task = new IdentifyTask((jMap.getLayers().get(1)).getUrl());
        try {
            IdentifyResult[] results = task.execute(identifyparam);
            pixelValue = results(results);
        } catch (Exception e) {
        }

        return pixelValue;
    }

    private String results(IdentifyResult[] results) {

        String pixelValue = "";

        for (IdentifyResult result : results) {
            // if layer id = 3 (DEM layer): show attributes including pixel value (elevation in feet)
            for (Map.Entry<String, Object> attribute : result.getAttributes().entrySet()) {

                if (attribute.getKey().equals("Pixel Value") && !attribute.getValue().equals("NoData")) {
                    pixelValue = attribute.getValue().toString();
                }

            }
        }
        return pixelValue;
    }
}
