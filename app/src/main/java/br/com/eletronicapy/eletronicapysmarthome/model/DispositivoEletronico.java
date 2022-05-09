package br.com.eletronicapy.eletronicapysmarthome.model;

import br.com.eletronicapy.eletronicapysmarthome.model.mqtt.ClienteMQTT;

public class DispositivoEletronico {
	protected boolean ligado;
	protected int id;
	protected String topicoMqtt;
	protected ClienteMQTT cliente;
	public boolean isLigado() {
		return ligado;
	}
	public void setLigado(boolean ligado) {
		this.ligado = ligado;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTopicoMqtt() {
		return topicoMqtt;
	}
	public void setTopicoMqtt(String topicoMqtt) {
		this.topicoMqtt = topicoMqtt;
	}
	
	public String ligarDispositivo(){
		this.ligado = true;
		return "1";
	}
	
	public String desligarDispositov() {
		this.ligado = false;
		return "0";
	}
	public void setCliente(ClienteMQTT cliente) {
		this.cliente = cliente;
	}
	public ClienteMQTT getCliente() {
		return cliente;
	}
	
	public void enviarComando(String comando) {
		this.cliente.publicar(this.topicoMqtt, comando.getBytes(), 1);
	}
	public void desativarConexao() {
		this.cliente.unsubscribe(this.topicoMqtt);
	}
}
