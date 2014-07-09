/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DAO;

import java.util.List;

/**
 *
 * @author Jair
 */
public class Mensaje {
    
    //Define si el mensaje es nuevo/viejo
    private boolean status;
    
    //define el tipo de mensaje que ha sido recibido
    private int tipo;
    
    //informacion relevante que se quiere comunicar
    private String mensaje;
    
    //puntos de interes que se han asignado 
    List<Posicion> puntosInteres;
    
    
}
