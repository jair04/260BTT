/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package JMS_ActiveMQ;

import Controller.Constante;
import DAO.Mensaje;
import DAO.Mision;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

/**
 *
 * @author Jair
 */

/* 
 * Clase que recibe el mensaje que ha sido enviado por el consumidor o publicador 
 * una vez recibido el mensaje se procesa para determinar que es lo que se tiene 
 * que hacer con el.
 */

public class MessageReceiver implements MessageListener {

    @Override
    public void onMessage(Message message){        
        try{
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                String text = textMessage.getText();
                System.out.println("Consumidor Received: " + text);
            }else if(message instanceof ObjectMessage){
                ObjectMessage objMessage = (ObjectMessage)message;
                Mensaje mensaje = (Mensaje)objMessage.getObject();
                this.procesarMensaje(mensaje);
            }
        }catch (JMSException ex) {
                Logger.getLogger(MessageReceiver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void procesarMensaje(Mensaje mensaje){
        int tipo = mensaje.getTipo();
        
        switch(tipo){
            case Constante.CONECTAR_AERONAVE:
                System.out.println("peticion de conexion");
        }
    }
    
}
