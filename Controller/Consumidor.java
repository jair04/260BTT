/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controller;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 *
 * @author Jair
 */
public class Consumidor implements ExceptionListener{
    
    private final ActiveMQConnectionFactory connectionFactory;
    private final Connection connection;
    private final Session session;
    private Destination destination;
    private final MessageConsumer consumer;
    
    public Consumidor(String ip, String nombreTema,int tipo) throws JMSException{
        // Create a ConnectionFactory
        connectionFactory = new ActiveMQConnectionFactory("vm://"+ip+"");
 
        // Create a Connection
        connection = connectionFactory.createConnection();
        connection.start();
 
        connection.setExceptionListener(this);
 
        // Create a Session
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
 
        // Create the destination (Topic or Queue)
        if(tipo==Constante.TEMA){
            destination = session.createTopic(nombreTema);
        }else if(tipo==Constante.COLA){
            destination = session.createQueue(nombreTema);
        }
 
        // Create a MessageConsumer from the Session to the Topic or Queue
        consumer = session.createConsumer(destination);
    }
    
    public void leerMensaje() throws JMSException{
        // Wait for a message
        Message message = consumer.receive();
                              
        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            String text = textMessage.getText();
            System.out.println("Received: " + text);
        }else{
            System.out.println("Received: " + message);
        }
    }
    
    public void cerrarConexion() throws JMSException{
        //Clean up
        consumer.close();
        session.close();
        connection.close();
    }
 
        public synchronized void onException(JMSException ex) {
            System.out.println("JMS Exception occured.  Shutting down client.");
        }
    }

