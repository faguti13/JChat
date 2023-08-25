package JChat;

import javax.swing.*;

import java.awt.*;

import java.io.*;
import java.net.ServerSocket;

import java.net.*;

/**
 * Clase principal que contiene el método main del servidor
 * @author Adrián Muñoz Alvarado y Fabián Gutiérrez Jiménez con ayuda del canal de YouYube pildorasinformaticas
 */
public class Servidor  {
	/**
	 * Clase que hereda de JPanel y representa el panel del cliente
	 * @author Adrián Muñoz Alvarado y Fabián Gutiérrez Jiménez con ayuda del canal de YouYube pildorasinformaticas
	 * @param args método principal que crea una instancia de la clase MarcoServidor
	 */
	public static void main(String[] args) { // Este método principal crea una instancia de la clase MarcoServidor

		MarcoServidor mimarco=new MarcoServidor();

		mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
	}
}

/**
 *         Instituto Tecnológico de Costa Rica
 *             Ingenería en Computadores
 * Lenguaje: Java 20.0.2
 * Descripción: Esta clase representa un marco que puede recibir y reenviar mensajes de chat, y que puede ejecutarse en un hilo
 * @author Adrián Muñoz Alvarado y Fabián Gutiérrez Jiménez con ayuda del canal de YouYube pildorasinformaticas
 * Profesor: Luis Alonso Barboza Artavia
 * Versión: 1
 * Fecha última modificación: Agosto 25 de 2023
 */
class MarcoServidor extends JFrame implements Runnable {

	public MarcoServidor(){ // Este constructor inicializa el marco y el área de texto

		setBounds(1200,300,280,350);

		// Se crea un panel y se le asigna un diseño de borde
		JPanel milamina= new JPanel();
		
		milamina.setLayout(new BorderLayout());
		
		areatexto=new JTextArea();// Se crea un área de texto
		
		milamina.add(areatexto,BorderLayout.CENTER);// Se añade el área de texto al panel en la posición central
		
		add(milamina);// Se añade el panel al marco
		
		setVisible(true);

		Thread mihilo=new Thread(this); // Se crea un hilo que ejecuta el método run() de esta clase

		mihilo.start();// Se inicia el hilo
		
		}
	

	@Override
	public void run() {

		try {
			ServerSocket servidor=new ServerSocket(9999); // Se crea un socket servidor en el puerto 9999

			String nick, ip, mensaje;

			PaqueteEnvio paquete_recibido;

			while(true) {

				Socket misocket = servidor.accept();// Se acepta una conexión entrante

				ObjectInputStream paquete_datos=new ObjectInputStream(misocket.getInputStream());// Se crea un flujo de entrada para leer el paquete de datos

				paquete_recibido=(PaqueteEnvio) paquete_datos.readObject();// Se lee el paquete de datos y se guarda en una variable

				// Se obtienen los datos del paquete: nick, ip y mensaje
				nick=paquete_recibido.getNick();

				ip=paquete_recibido.getIp();

				mensaje=paquete_recibido.getMensaje();



				areatexto.append("\n" + nick + ": " + mensaje + " para " + ip);// Se muestra el mensaje en el área de texto

				Socket enviaDestinatario=new Socket(ip,9090);// Se crea un socket para enviar el paquete al destinatario

				ObjectOutputStream paqueteReenvio=new ObjectOutputStream(enviaDestinatario.getOutputStream());// Se crea un flujo de salida para escribir el paquete de datos

				paqueteReenvio.writeObject(paquete_recibido); // Se escribe el paquete de datos en el flujo de salida

				paqueteReenvio.close();

				enviaDestinatario.close();

				misocket.close();
			}

		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	private	JTextArea areatexto;
}