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
import org.apache.activemq.ActiveMQConnectionFactory;
 
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

public class Publicador{
    
    
    private final ActiveMQConnectionFactory connectionFactory;
    private final Connection connection;
    private final Session session;
    private final Destination destination;
    private final MessageProducer producer;

    public Publicador(String ip, String nombreTema) throws JMSException{        
        // Create a ConnectionFactory
        connectionFactory = new ActiveMQConnectionFactory("vm://"+ip+"");
        
        // Create a Connection
        connection = connectionFactory.createConnection();
        connection.start();
        
        // Create a Session
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // Create the destination (Topic or Queue)
        destination = session.createTopic(nombreTema);
        
        //Create a MessageProducer from the Session to the Topic or Queue
        producer = session.createProducer(destination);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);      
    }
    
    public void publicarMensaje(String mensaje) throws JMSException{
        // Create a messages
        TextMessage message = session.createTextMessage(mensaje);
        
        // Tell the producer to send the message
        System.out.println("Sent message: "+ message.hashCode());
        producer.send(message);
    }
    
    public void cerrarConexion() throws JMSException{
        // Clean up
        session.close();
        connection.close();
    }
}
