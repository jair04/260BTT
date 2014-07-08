package SistemaTactico;

import Controller.Constante;
import Controller.Publicador;
import Controller.Suscriptor;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;


/**
 *
 * @author Jair
 */
public class Main{
    public static void main(String args[]) throws Exception{
        
        Publicador comandoCentral = new Publicador("189.245.241.120","test"); 
        Suscriptor aeronave1 = new Suscriptor("189.245.241.120","test");
        Suscriptor aeronave2 = new Suscriptor("189.245.241.120","test");
        
        comandoCentral.publicarMensaje("hola como estan");
        aeronave1.leerMensaje();
        aeronave2.leerMensaje();
        
        comandoCentral.publicarMensaje("mensaje cambiado");
        aeronave1.leerMensaje();
        
        comandoCentral.cerrarConexion();
        aeronave1.cerrarConexion();
        aeronave2.cerrarConexion();       
        
        //obtener ip externa
        /*
        URL whatismyip = new URL("http://checkip.amazonaws.com");
        BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
        String ip = in.readLine();
        System.out.println(ip);
        */
    }    
}
