/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controller;

/**
 *
 * @author Jair
 */

/*
    Clase que nos permite generar o ejecutar un proceso/clase
    dentro de un hilo de ejecucion
*/
public class Hilo {
     public Hilo(Runnable runnable, boolean daemon) throws Exception{
        Thread brokerThread = new Thread(runnable);
        brokerThread.setDaemon(daemon);
        brokerThread.start();
    }

}
