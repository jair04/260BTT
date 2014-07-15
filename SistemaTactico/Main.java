package SistemaTactico;

import Controller.Constante;
import JMS_ActiveMQ.Consumidor;
import JMS_ActiveMQ.Publicador;
import DAO.Aeronave;
import DAO.Mensaje;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;


/**
 *
 * @author Jair
 */
public class Main{
    public static void main(String args[]) throws Exception{
        
        
        // Ejemplo tipo Tema
        
         
        /*Publicador comandoCentral = new Publicador("test");
        Consumidor aeronave1 = new Consumidor("189.245.241.120","test");
        Consumidor aeronave2 = new Consumidor("189.245.241.120","test");
        
        
        comandoCentral.publicarMensaje("hola como estan");        
        comandoCentral.publicarMensaje("El mensaje cambio");
        comandoCentral.publicarMensaje("Enviando otro mensaje");
        
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
        /*
        Mensaje m1 = new Mensaje(1, null);
        Mensaje m2 = new Mensaje(2, null);
        
        Publicador aeronave1 = new Publicador("189.245.241.120","test",Constante.MSN_COLA); 
        Publicador aeronave2 = new Publicador("189.245.241.120","test",Constante.MSN_COLA);
        Consumidor comandoCentral = new Consumidor("189.245.241.120","test",Constante.MSN_COLA);
        
        aeronave1.publicarMensaje("m1");
        aeronave2.publicarMensaje("m2");
        
        comandoCentral.leerMensaje();
        comandoCentral.leerMensaje();
        
        aeronave1.cerrarConexion();
        aeronave2.cerrarConexion();
        comandoCentral.cerrarConexion();
        */
        
        //Set - HashSet Example
        /*Set<Aeronave> d = new HashSet<>();
        
        Aeronave a = new Aeronave("127.0.0.1","123456",null,0);
        Aeronave b = new Aeronave("127.0.0.2","123457",null,0);
        Aeronave c = new Aeronave("127.0.0.1","123456",null,0);
        
        System.out.println(d.add(a));
        System.out.println(d.add(b));
        System.out.println(d.add(c));
        System.out.println(d.size());*/
        
        Aeronave aeronave1 = new Aeronave("127.0.0.1","127.0.0.0","123456", null);
        Aeronave aeronave2 = new Aeronave("127.0.0.2","127.0.0.0","123056", null);
        aeronave1.enviarInformacion();      
        
        /*Consumidor comando = new Consumidor("189.245.241.120",Constante.TEMA_AERONAVE_COMANDO,Constante.MSN_COLA);
        comando.leerMensaje();
        comando.cerrarConexion();*/
    }    
}
