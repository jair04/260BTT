/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package JMS_ActiveMQ;

/**
 *
 * @author Jair
 */

import Controller.Constante;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;

public class Publicador implements MessageListener{
    
    
    private final ActiveMQConnectionFactory connectionFactory;
    private final Connection connection;
    private final Session session;
    private final Destination destination;
    private final MessageProducer producer;
    private final String ip;

    /*
        ip: ip donde se esta ejecutando la aplicacion del comando central
        nombreTema: nombre del tema donde se estaran publicando los temas
        tipo: tipo del mensaje por cola o tema 
    */
    public Publicador(String ip) throws JMSException, IOException, Exception{        
        //get the public ip
        //this.ip = this.getIP();
        this.ip = ip;
        
        //Embebbed message broker
         BrokerService broker = new BrokerService();
         broker.setPersistent(false);
         broker.setUseJmx(false);
         broker.addConnector("tcp://"+this.ip+":61616");
         broker.start();

        // Create a ConnectionFactory
        connectionFactory = new ActiveMQConnectionFactory("tcp://"+this.ip+":61616");
        
        // Create a Connection
        connection = connectionFactory.createConnection();
        connection.start();
        
        // Create a Session
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // Create the destination (Topic or Queue)
        destination = session.createTopic(Constante.NOMBRE_TEMA);
        
        //Create a MessageProducer from the Session to send the airship information set
        producer = session.createProducer(destination);        
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        
        //Create a MessageConsumer from de session to receive the airship requests
        Destination destinationAux = this.session.createQueue(Constante.NOMBRE_COLA);
        MessageConsumer consumer = this.session.createConsumer(destinationAux);
        consumer.setMessageListener(this);
    }
    
    public void enviarMensaje(String mensaje) throws JMSException, InterruptedException{
        // Create a messages
        TextMessage message = session.createTextMessage(mensaje);
       
        // Tell the producer to send the message
        System.out.println("Sent message: "+ message.hashCode());
        producer.send(message);
        Thread.sleep(10);
    }
    
    public void cerrarConexion() throws JMSException{
        // Clean up
        session.close();
        connection.close();
    }
    
    private String getIP() throws MalformedURLException, IOException{
        URL whatismyip = new URL("http://checkip.amazonaws.com");
        BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
        String ip = in.readLine();
        return ip;
    }

    @Override
    public void onMessage(Message msg) {
        System.out.println("mensaje recibido de aeronave");
    }

}
