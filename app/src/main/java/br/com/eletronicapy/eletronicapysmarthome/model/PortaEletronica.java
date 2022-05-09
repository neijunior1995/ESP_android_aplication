package br.com.eletronicapy.eletronicapysmarthome.model;

import br.com.eletronicapy.eletronicapysmarthome.model.mqtt.ClienteMQTT;

public class PortaEletronica extends DispositivoEletronico {
	
	public boolean trancado = true;

	public PortaEletronica(int id, String topicoMqtt, ClienteMQTT cliente) {
		this.id = id;
		this.topicoMqtt = topicoMqtt;
		this.cliente = cliente;
	}
	
	public void trancar() {
		this.trancado = true;		
	}
	
	public void destrancar() {
		this.trancado = false;
	}
	
	public boolean getTrancado() {
		return this.trancado;
	}
	
	
	public void enviarComando()
	{
		super.enviarComando(String.valueOf(trancado));
	}
	

}
