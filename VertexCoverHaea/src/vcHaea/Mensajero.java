/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.Properties;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author JuanCamilo
 */
public class Mensajero {

    public String dirOrigen;
    public String dirDestino;
    public String asunto;
    public String mensaje;
    public String servidor;
    public String passw;

    public Mensajero() {
        dirOrigen="fumigamosyservimos@yahoo.com";
        passw="cucaracha";
        asunto="Vencimiento Fumigacion";
    }

    public Mensajero(String ori, String des, String asun, String msg, String pass) {
        dirOrigen = ori;
        dirDestino = des;
        asunto = asun;
        mensaje = msg;
        passw = pass;
    }

    public void setMsg(String nombreEmpresa, int diasF) {
        mensaje = "Estimado cliente,\n "
                + "Le enviamos este mensaje con la intencion de invitarlo a utilizar de nuevo"
                + " nuetros servicios y productos, debido a que su empresa " + nombreEmpresa
                + " le restan " + diasF + " de vigencia en la certificacion."
                +"\n \n \n Atentamente: Fumigamos y Servimos \n mensaje enviado"
                + "automaticamente a través de F&System";
    }

    public boolean enviar() throws MessagingException {
        Properties props = new Properties();
        props.setProperty("mail.smtp.host", "smtp.mail.yahoo.com");
        props.setProperty("mail.smtp.ssl.enable", "true");
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.auth", "true");

        // Preparamos la sesion
        Session session = Session.getDefaultInstance(props);

        //Recoger los datos
        String str_De = dirOrigen;
        String str_PwRemitente = passw;
        String str_Para = dirDestino;
        String str_Asunto = asunto;
        String str_Mensaje = mensaje;
        
        //Obtenemos los destinatarios
        String destinos[] = str_Para.split(",");
        // Construimos el mensaje
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(str_De));
        Address[] receptores = new Address[destinos.length];
        int j = 0;
        while (j < destinos.length) {
            receptores[j] = new InternetAddress(destinos[j]);
            j++;
        }
        //receptores.
        message.addRecipients(Message.RecipientType.TO, receptores);
        message.setSubject(str_Asunto);
        message.setText(str_Mensaje);
        // Lo enviamos.
        Transport t = session.getTransport("smtp");
        t.connect(str_De, str_PwRemitente);
        t.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
        // Cierre de la conexion.
        t.close();
        System.out.println("Mensaje Enviado");
        return true;
    }

    public void auth(String cad) {
      passw=cad;
    }

    public void para(String cad) {
       dirDestino=cad;
    }
}
