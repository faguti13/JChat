import javax.swing.*;

import java.awt.event.*;

import java.io.*;
import java.net.*;

/**
 * Clase principal que contiene el método main del cliente
 * @author Adrián Muñoz Alvarado y Fabián Gutiérrez Jiménez con ayuda del canal de YouYube pildorasinformaticas
 */

public class Cliente {

	/**
	 * Método principal que se ejecuta al iniciar el programa
	 * @param args Método principal que se ejecuta al iniciar el programa
	 * @author Adrián Muñoz Alvarado y Fabián Gutiérrez Jiménez con ayuda del canal de YouYube pildorasinformaticas
	 */

	public static void main(String[] args) { // Método principal que se ejecuta al iniciar el programa

		MarcoCliente mimarco=new MarcoCliente(); // Crea un objeto de tipo MarcoCliente y lo muestra en pantalla

		mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Establece la operación por defecto al cerrar el marco

	}

}

/**
 *         Instituto Tecnológico de Costa Rica
 *             Ingenería en Computadores
 * Lenguaje: Java 20.0.2
 * Descripción: Clase que hereda de JPanel y representa el panel del cliente
 * @author Adrián Muñoz Alvarado y Fabián Gutiérrez Jiménez con ayuda del canal de YouYube pildorasinformaticas
 * Profesor: Luis Alonso Barboza Artavia
 * Versión: 1
 * Fecha última modificación: Agosto 25 de 2023
 */

class MarcoCliente extends JFrame{

	/**
	 *         Instituto Tecnológico de Costa Rica
	 *             Ingenería en Computadores
	 * Lenguaje: Java 20.0.2
	 * Descripción: Constructor de la clase que inicializa el marco
	 * @author Adrián Muñoz Alvarado y Fabián Gutiérrez Jiménez con ayuda del canal de YouYube pildorasinformaticas
	 * Profesor: Luis Alonso Barboza Artavia
	 * Versión: 1
	 * Fecha última modificación: Agosto 25 de 2023
	 */

	public MarcoCliente(){// Constructor de la clase que inicializa el marco
		
		setBounds(600,300,280,350); // Establece la posición y el tamaño del marco

		LaminaMarcoCliente milamina=new LaminaMarcoCliente(); // Crea un objeto de tipo LaminaMarcoCliente y lo añade al marco

		add(milamina);
		
		setVisible(true);
		}	
	
}

/**
 *         Instituto Tecnológico de Costa Rica
 *             Ingenería en Computadores
 * Lenguaje: Java 20.0.2
 * Descripción: clase agrega distintos elementos a la ventana cliente junto con sus funciones
 * @author Adrián Muñoz Alvarado y Fabián Gutiérrez Jiménez con ayuda del canal de YouYube pildorasinformaticas
 * Profesor: Luis Alonso Barboza Artavia
 * Versión: 1
 * Fecha última modificación: Agosto 25 de 2023
 */

class LaminaMarcoCliente extends JPanel implements Runnable{
	
	public LaminaMarcoCliente(){

		nick=new JTextField(5);

		// Añade los componentes gráficos al panel en las posiciones y tamaños deseados
		add(nick);

		JLabel texto=new JLabel("-JCHAT-");
		
		add(texto);

		ip=new JTextField(8);

		add(ip);

		campochat=new JTextArea(12,20);

		add(campochat);
	
		campo1=new JTextField(20);
	
		add(campo1);

		miboton=new JButton("Enviar");

		EnviaTexto mievento=new EnviaTexto();

		miboton.addActionListener(mievento); // Crea un objeto de tipo EnviaTexto y lo asigna como oyente del botón de enviar


		add(miboton);

		Thread mihilo=new Thread(this); // Crea e inicia un hilo de ejecución


		mihilo.start();
		
	}

	/**
	 *
	 */

	private class EnviaTexto implements ActionListener{

		/**
		 *         Instituto Tecnológico de Costa Rica
		 *             Ingenería en Computadores
		 * Lenguaje: Java 20.0.2
		 * Descripción: Clase que envia el texto escrito por el cliente al servidor
		 * @author Adrián Muñoz Alvarado y Fabián Gutiérrez Jiménez con ayuda del canal de YouYube pildorasinformaticas
		 * Profesor: Luis Alonso Barboza Artavia
		 * Versión: 1
		 * Fecha última modificación: Agosto 25 de 2023
		 * @param e el mensaje escrito para ser enviado al servidor
		 */

		@Override
		public void actionPerformed(ActionEvent e) {

			//System.out.println(campo1.getText());

			campochat.append("\n" + campo1.getText());

			try {
				Socket misocket=new Socket("192.168.164.73",9999); // Crea un socket para conectarse al servidor con la dirección IP y el puerto dados

				PaqueteEnvio datos=new PaqueteEnvio(); // Crea un objeto de tipo PaqueteEnvio con los datos del nick, la IP y el mensaje

				datos.setNick(nick.getText());

				datos.setIp(ip.getText());

				datos.setMensaje(campo1.getText());

				ObjectOutputStream paquete_datos=new ObjectOutputStream(misocket.getOutputStream());

				paquete_datos.writeObject(datos);

				misocket.close(); // Cierra el socket

				/*DataOutputStream flujo_salida=new DataOutputStream(misocket.getOutputStream());

				flujo_salida.writeUTF(campo1.getText());

				flujo_salida.close();*/



			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			} catch (IOException e1){
				System.out.println(e1.getMessage());
			}

		}

	}
		
		
	private JTextField campo1, nick, ip;

	private JTextArea campochat; // Este área de texto muestra el historial de mensajes del chat

	private JButton miboton; // Este botón envía el mensaje escrito en el campo de texto al servidor


	@Override
	public void run() {

		try{

			ServerSocket servidor_cliente=new ServerSocket(9090); // Crea un socket de servidor para escuchar peticiones de los clientes en el puerto 9090

			Socket cliente;

			PaqueteEnvio paqueteRecibido;

			while (true){ // Bucle infinito para atender a los clientes

				cliente=servidor_cliente.accept(); // Acepta una conexión de un cliente y asigna el socket correspondiente

				ObjectInputStream flujo_entrada=new ObjectInputStream(cliente.getInputStream()); // Crea un flujo de entrada de objetos para recibir el paquete de datos del cliente

				paqueteRecibido= (PaqueteEnvio) flujo_entrada.readObject();

				campochat.append("\n" + paqueteRecibido.getNick() + ": " + paqueteRecibido.getMensaje()); // Añade al campochat el nick y el mensaje del paquete recibido con un salto de línea



			}

		} catch (Exception e){

			System.out.println(e.getMessage());
		}

	}
}

/**
 *         Instituto Tecnológico de Costa Rica
 *             Ingenería en Computadores
 * Lenguaje: Java 20.0.2
 * Descripción: Clase que implementa la interfaz Serializable y representa un paquete de datos para el chat
 * @author Adrián Muñoz Alvarado y Fabián Gutiérrez Jiménez con ayuda del canal de YouYube pildorasinformaticas
 * Profesor: Luis Alonso Barboza Artavia
 * Versión: 1
 * Fecha última modificación: Agosto 25 de 2023
 */

class PaqueteEnvio implements Serializable{

	private String nick, ip, mensaje; // Atributos privados que almacenan el nick, la ip y el mensaje del usuario

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
}