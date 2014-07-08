package SistemaTactico;

import Controller.Constante;
import Controller.Publicador;
import Controller.Consumidor;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;


/**
 *
 * @author Jair
 */
public class Main{
    public static void main(String args[]) throws Exception{
        
        /*
        // Ejemplo tipo Tema
        
        Publicador comandoCentral = new Publicador("189.245.241.120","test",Constante.MSN_TEMA); 
        Consumidor aeronave1 = new Consumidor("189.245.241.120","test",Constante.MSN_TEMA);
        Consumidor aeronave2 = new Consumidor("189.245.241.120","test",Constante.MSN_TEMA);
        
        comandoCentral.publicarMensaje("hola como estan");
        aeronave1.leerMensaje();
        aeronave2.leerMensaje();
        
        comandoCentral.publicarMensaje("mensaje cambiado");
        aeronave1.leerMensaje();
        
        comandoCentral.cerrarConexion();
        aeronave1.cerrarConexion();
        aeronave2.cerrarConexion();       
        */
        
        
        //obtener ip externa
        /*
        URL whatismyip = new URL("http://checkip.amazonaws.com");
        BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
        String ip = in.readLine();
        System.out.println(ip);
        */
        
        
        
        //Ejemplo tipo Cola
        Publicador aeronave1 = new Publicador("189.245.241.120","test",Constante.MSN_COLA); 
        Publicador aeronave2 = new Publicador("189.245.241.120","test",Constante.MSN_COLA);
        Consumidor comandoCentral = new Consumidor("189.245.241.120","test",Constante.MSN_COLA);
        
        aeronave1.publicarMensaje("aeronave1");
        aeronave2.publicarMensaje("aeronave2");
        
        comandoCentral.leerMensaje();
        comandoCentral.leerMensaje();
        
        aeronave1.cerrarConexion();
        aeronave2.cerrarConexion();
        comandoCentral.cerrarConexion();
        
    }    
}
