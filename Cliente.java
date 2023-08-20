import javax.swing.*;

import java.awt.event.*;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;

public class Cliente {

	public static void main(String[] args) {
		
		MarcoCliente mimarco=new MarcoCliente();
		
		mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

}


class MarcoCliente extends JFrame{
	
	public MarcoCliente(){
		
		setBounds(600,300,280,350);
				
		LaminaMarcoCliente milamina=new LaminaMarcoCliente();
		
		add(milamina);
		
		setVisible(true);
		}	
	
}

class LaminaMarcoCliente extends JPanel{
	
	public LaminaMarcoCliente(){
	
		JLabel texto=new JLabel("CLIENTE");
		
		add(texto);
	
		campo1=new JTextField(20);
	
		add(campo1);

		miboton=new JButton("Enviar");

		EnviaTexto mievento=new EnviaTexto();

		miboton.addActionListener(mievento);

		add(miboton);	
		
	}
	
	
	
	private class EnviaTexto implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {


			//System.out.println(campo1.getText());

			try {
				Socket misocket=new Socket("192.168.56.1",9999);

				DataOutputStream flujo_salida=new DataOutputStream(misocket.getOutputStream());

				flujo_salida.writeUTF(campo1.getText());

				flujo_salida.close();

			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			} catch (IOException e1){
				System.out.println(e1.getMessage());
			}

		}

	}
		
		
	private JTextField campo1;
	
	private JButton miboton;
	
}