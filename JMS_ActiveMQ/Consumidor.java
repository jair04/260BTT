/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package JMS_ActiveMQ;

import Controller.Constante;
import DAO.Mensaje;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 *
 * @author Jair
 */
public class Consumidor implements MessageListener{
    
    private final ActiveMQConnectionFactory connectionFactory;
    private final Connection connection;
    private final Session session;
    private final Destination destination;
    private final MessageConsumer consumer;
    
    public Consumidor(String ip) throws JMSException{
        
        // Create a ConnectionFactory
        connectionFactory = new ActiveMQConnectionFactory("vm://"+ip+"");
        
 
        // Create a Connection
        connection = connectionFactory.createConnection();
        connection.start();
        
        // Create a Session
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
 
        // Create the destination (Topic or Queue)
        destination = session.createTopic(Constante.NOMBRE_TEMA);
 
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

    public void enviarMensaje() throws JMSException, InterruptedException{
        Destination destinationAux = this.session.createQueue(Constante.NOMBRE_COLA);
        MessageProducer producerAux = this.session.createProducer(destinationAux);
        producerAux.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        
        TextMessage message = session.createTextMessage("enviado desde aeronave");
        producerAux.send(message);
        Thread.sleep(10);
        
        producerAux.close();
    }

    @Override
    public void onMessage(Message msg) {
        System.out.println("mensaje recibido de comando");
    }
}

