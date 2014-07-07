package SistemaTactico;

import Controller.Publicador;
import Controller.Suscriptor;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jair
 */
public class Main{
    public static void main(String args[]) throws Exception{
        
        Publicador comandoCentral = new Publicador("127.0.0.1","test"); 
        Suscriptor aeronave1 = new Suscriptor("127.0.0.1","test");
        Suscriptor aeronave2 = new Suscriptor("127.0.0.1","test");
        
        comandoCentral.publicarMensaje("hola como estan");
        aeronave1.leerMensaje();
        aeronave2.leerMensaje();
        
        comandoCentral.publicarMensaje("mensaje cambiado");
        aeronave1.leerMensaje();
        
        comandoCentral.cerrarConexion();
        aeronave1.cerrarConexion();
        aeronave2.cerrarConexion();
        
        
    }    
}
