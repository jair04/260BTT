/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package JMS_ActiveMQ;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 *
 * @author Jair
 */
public class MessageReceiver implements MessageListener {

    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
           try {
               TextMessage textMessage = (TextMessage) message;
               String text = textMessage.getText();
               System.out.println("Consumidor Received: " + text);
           } catch (JMSException ex) {
               Logger.getLogger(Consumidor.class.getName()).log(Level.SEVERE, null, ex);
           }
        }else{
            System.out.println("Received: " + message);
        }
    }
    
    private void procesarMensaje(){
    
    }
    
}
