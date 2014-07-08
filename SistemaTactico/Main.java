package SistemaTactico;

import Controller.Publicador;
import Controller.Suscriptor;
import Controller.Constante;


/**
 *
 * @author Jair
 */
public class Main{
    public static void main(String args[]) throws Exception{
        
        Publicador comandoCentral = new Publicador("189.245.241.120","test",Constante.TEMA); 
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
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));
            String ip = in.readLine();
            System.out.println(ip);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        */
    }    
}
