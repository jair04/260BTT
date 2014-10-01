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
import DAO.Aeronave;
import DAO.Mensaje;
import GUI.ButtonPanel;
import GUI.General_GUI;
import GUI.InformationAirship;
import com.esri.core.geometry.CoordinateConversion;
import com.esri.core.geometry.Point;
import java.awt.Color;
import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;

public class Publicador implements MessageListener {

    private ActiveMQConnectionFactory connectionFactory;
    private Connection connection;
    private Session session;
    private Destination destination;
    private MessageProducer producer;
    private MessageProducer replyProducer;
    private final String ip;
    HashMap<String, Aeronave> mision = new HashMap<>();
    private final General_GUI general;
    private final Color[] color = {Color.RED,Color.YELLOW,Color.MAGENTA, Color.GREEN,Color.ORANGE, Color.LIGHT_GRAY};
    private int i = 0;

    /*
     ip: ip donde se esta ejecutando la aplicacion del comando central
     */
    public Publicador(General_GUI general) {
        this.general = general;
        this.ip = "tcp://0.0.0.0:61616";

        Aeronave comando = new Aeronave("comando", null, null);
        mision.put("comando", comando);
    }

    public void startServer() throws Exception {
        //Embebbed message broker
        BrokerService broker = new BrokerService();
        broker.setPersistent(false);
        broker.setUseJmx(false);
        broker.addConnector(this.ip);
        broker.start();

        // Create a ConnectionFactory
        connectionFactory = new ActiveMQConnectionFactory(this.ip);

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

        //Create a MessageProducer which will works to reply to the airship
        this.replyProducer = session.createProducer(null);
        this.replyProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        //Create a MessageConsumer from de session to receive the airship requests
        Destination destinationAux = this.session.createQueue(Constante.NOMBRE_COLA);
        MessageConsumer consumer = this.session.createConsumer(destinationAux);
        consumer.setMessageListener(this);
    }

    public void enviarMensaje(final Serializable mensaje) throws JMSException, InterruptedException {
        // Create a messages
        ObjectMessage objMessage = session.createObjectMessage();
        objMessage.setObject(mensaje);

        // Tell the producer to send the message
        producer.send(objMessage);
        //Thread.sleep(10);
    }

    public void cerrarConexion() throws JMSException {
        // Clean up
        session.close();
        connection.close();
    }

    @Override
    public void onMessage(Message msg) {

        //Message received from the airships
        try {
            //Object message
            ObjectMessage objMessage = (ObjectMessage) msg;
            Mensaje objeto = (Mensaje) objMessage.getObject();

            //processing the petition 
            this.procesarPeticion(objeto, msg);

        } catch (JMSException | InterruptedException | IOException ex) {
            Logger.getLogger(Publicador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void procesarPeticion(Mensaje mensaje, Message msg) throws InterruptedException, IOException {
        try {
            if (mensaje.getTipo() == Constante.CONECTAR_AERONAVE) {
                //reply of the request
                Mensaje respuestaPeticion = new Mensaje(Constante.RESPUESTA_PETICION_CONEXION, null);

                //showing message 
                int opc = JOptionPane.showConfirmDialog(null, "¿Aceptar aeronave no. " + mensaje.getAeronave().getMatricula() + " ?");
                boolean resultOpc = false;

                //0 when the petition has been acepted 
                if (opc == 0) {
                    //adding the ariship to the mission
                    resultOpc = true;
                    mensaje.getAeronave().setColor(color[i]);
                    mision.put(mensaje.getAeronave().getMatricula(), mensaje.getAeronave());

                    this.general.updateUsersPanel(mision);
                    i++;
                }

                //True if the command has acepted the petition, otherwise false
                respuestaPeticion.setPeticionAceptada(resultOpc);

                //Sending the response´s request to the airship                 
                ObjectMessage response = this.session.createObjectMessage();
                response.setObject(respuestaPeticion);
                response.setJMSCorrelationID(msg.getJMSCorrelationID());
                this.replyProducer.send(msg.getJMSReplyTo(), response);

                System.out.println("Peticion conectarAeronave  [" + mensaje.getAeronave().getMatricula() + "] al comando central");

            } else if (mensaje.getTipo() == Constante.ACTUALIZAR_AERONAVE) {

                //searching the airship and then updating its position
                this.mision.get(mensaje.getAeronave().getMatricula()).setPosicion(mensaje.getAeronave().getPosicion());
                this.mision.get(mensaje.getAeronave().getMatricula()).setPuntosInteres(mensaje.getAeronave().getPuntosInteres());

                Mensaje misionUpdated = new Mensaje(Constante.MISION_ACTUALIZADA, null);
                misionUpdated.setMision(this.mision);

                this.general.paintMissionLayer("comando", this.mision);
                this.enviarMensaje(misionUpdated);

                if (this.general.getIdAeronave() != null) {
                    Aeronave aeronave = this.mision.get(this.general.getIdAeronave());

                    //getting the panel from the General_GUI where is showing the updated information
                    InformationAirship info = general.getControlPanel();
                    
                    double altitud = Double.parseDouble(aeronave.getPosicion().getAltitud());
                    double altura = Double.parseDouble(aeronave.getPosicion().getAltura());
                    
                    DecimalFormat decimalE = new DecimalFormat("#.#");
                    
                    //Showing the information in the jpanel
                    info.setTextTextField(aeronave.getMatricula());
                    info.getMgrsLabel().setText("MGRS: " + aeronave.getPosicion().getMgrs() + "         ");
                    info.getAlturaLabel().setText("Altura: " + aeronave.getPosicion().getAltura() + " mts.");
                    info.getAltitudLabel().setText("Altitud: " + aeronave.getPosicion().getAltitud() + " mts.");
                    info.getLongitudLabel().setText("Longitud: " + aeronave.getPosicion().getLongitud());
                    info.getLatitudLabel().setText("Latitud: " + aeronave.getPosicion().getLatitud());
                    info.getVelocidadLabel().setText("Velocidad: " + aeronave.getPosicion().getVelocidad() + " m/s");
                    info.getElevacion().setText(decimalE.format(altitud-altura) + " mts.");
                }
            }

        } catch (JMSException ex) {
            Logger.getLogger(Publicador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
