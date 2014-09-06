/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JMS_ActiveMQ;

import Controller.Constante;
import DAO.Aeronave;
import DAO.Mensaje;
import java.io.Serializable;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.swing.JOptionPane;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 *
 * @author Jair
 */
public class Consumidor implements MessageListener{

    private ActiveMQConnectionFactory connectionFactory;
    private Connection connection;
    private Session session;
    private Destination destination;
    private MessageConsumer consumer;
    private final Aeronave aeronave;
    private Mensaje mensaje;
    private String ip;

    public Consumidor(String ip, Aeronave aeronave){
        mensaje = new Mensaje();
        this.aeronave = aeronave;
        this.ip = ip;       
    }

    public void startConnection() throws JMSException{                
        // Create a ConnectionFactory
        connectionFactory = new ActiveMQConnectionFactory("tcp://" + this.ip + ":61616");

        // Create a Connection
        connection = connectionFactory.createConnection();
        connection.start();

        // Create a Session
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE); 
    }
    
    public void closeConnection() throws JMSException {
        //Clean up
        consumer.close();
        session.close();
        connection.close();
    }

    public void sendConnectionRequest() throws JMSException, InterruptedException{
        Mensaje request = new Mensaje(Constante.CONECTAR_AERONAVE,this.aeronave);
        this.sendMessage(request);
    }
     
    public void sendMessage(final Serializable mensaje) throws JMSException, InterruptedException {
        Destination destinationAux = this.session.createQueue(Constante.NOMBRE_COLA);
        MessageProducer producerAux = this.session.createProducer(destinationAux);

        Destination tempDest = this.session.createTemporaryQueue();
        MessageConsumer responseConsumer = session.createConsumer(tempDest);

        responseConsumer.setMessageListener(this);
        ObjectMessage objMessage = session.createObjectMessage();
        objMessage.setObject(mensaje);

        objMessage.setJMSReplyTo(tempDest);

        String correlationId = this.createRandomString();
        objMessage.setJMSCorrelationID(correlationId);

        producerAux.send(objMessage);
        Thread.sleep(10);
    }
    
    private String createRandomString() {
        Random random = new Random(System.currentTimeMillis());
        long randomLong = random.nextLong();
        return Long.toHexString(randomLong);
    }
 
    private void establishConnection() throws JMSException{
        // Create the destination (Topic or Queue)
        destination = session.createTopic(Constante.NOMBRE_TEMA);

        // Create a MessageConsumer from the Session to the Topic or Queue
        consumer = session.createConsumer(destination);
        consumer.setMessageListener(this);
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Mensaje getMensaje() {
        return mensaje;
    }

    public void setMensaje(Mensaje mensaje) {
        this.mensaje = mensaje;
    }   
    
    @Override
    public void onMessage(Message msg) {
        try {            
            ObjectMessage objMessage = (ObjectMessage) msg;
            Mensaje mensajeResponse = (Mensaje) objMessage.getObject();
            
            if(mensajeResponse.getTipo()==Constante.RESPUESTA_PETICION_CONEXION){
                if(mensajeResponse.isPeticionAceptada()){
                    JOptionPane.showMessageDialog(null, "Peticion Aceptada", "Informacion", JOptionPane.INFORMATION_MESSAGE);
                    this.mensaje.setPeticionAceptada(true);
                    this.establishConnection();
                }else{
                    JOptionPane.showMessageDialog(null, "Peticion rechazada", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }else if(mensajeResponse.getTipo()== Constante.MISION_ACTUALIZADA){
                System.out.println("mision actualizada");
            }

        } catch (JMSException ex) {
            Logger.getLogger(Publicador.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
