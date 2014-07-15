/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package JMS_ActiveMQ;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 *
 * @author Jair
 */
public class Consumidor extends MessageReceiver{
    
    private final ActiveMQConnectionFactory connectionFactory;
    private final Connection connection;
    private final Session session;
    private Destination destination;
    private final MessageConsumer consumer;
    
    public Consumidor(String ip, String nombreTema) throws JMSException{
        // Create a ConnectionFactory
        connectionFactory = new ActiveMQConnectionFactory("vm://"+ip+"");
 
        // Create a Connection
        connection = connectionFactory.createConnection();
        connection.start();
        
        // Create a Session
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
 
        // Create the destination (Topic or Queue)
        destination = session.createTopic(nombreTema);
        
        //establish conexion with the "Comando Central"
        this.realizarPeticionConexion(destination, session);
 
        // Create a MessageConsumer from the Session to the Topic or Queue
        consumer = session.createConsumer(destination);
        consumer.setMessageListener(this);
    }
    
    public void cerrarConexion() throws JMSException{
        //Clean up
        consumer.close();
        session.close();
        connection.close();
    }

    private void realizarPeticionConexion(Destination destination, Session session) throws JMSException{
        TextMessage message = session.createTextMessage("Quiero conectarme");
        MessageProducer producer = session.createProducer(destination);
        producer.send(message);
    }
}

