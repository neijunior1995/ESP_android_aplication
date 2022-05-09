package br.com.eletronicapy.eletronicapysmarthome.model;

public class LedRGB extends DispositivoEletronico{
	int red;   //Intensidade da luz vermelha; 
	int green; //Intensidade da luz verde;
	int blue;  //Intensidade da luz azul;
	
	public void configuraLed(int red, int green, int blue) {
		this.red = red;
		this.blue = blue;
		this.green = green;
	}
	
	public String comandoLed() {
		return String.format("{\"Red\": %d, \"Green\": %d, \"Blue\": %d}", this.red,this.green,this.blue);
	}
	
	public void enviarComando() {
		super.getCliente().publicar(super.getTopicoMqtt(), this.comandoLed().getBytes(), 0);
	}
	
}
