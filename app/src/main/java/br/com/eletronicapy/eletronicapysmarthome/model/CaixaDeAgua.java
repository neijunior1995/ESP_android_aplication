package br.com.eletronicapy.eletronicapysmarthome.model;


import br.com.eletronicapy.eletronicapysmarthome.model.mqtt.ClienteMQTT;
import br.com.eletronicapy.eletronicapysmarthome.model.mqtt.Ouvinte;

public class CaixaDeAgua extends DispositivoEletronico{
	
	private Ouvinte ouvinte;
	private String msg = "0";

	public CaixaDeAgua(int id, String topico, ClienteMQTT cliente) {
		super.setId(id);
		super.setTopicoMqtt(topico);
		super.setCliente(cliente);
		ouvinte = new Ouvinte(cliente, topico, 0);
	}
	 
	public void carregaMenssagem(){
		if (ouvinte.getMsg()!=null)
		{
		this.msg=ouvinte.getMsg();
		}
		else {
			this.msg="0";
		}
	}
	public String getMsg() {
		return this.msg;
	}
	
	public void desativarConexao() {
		super.getCliente().unsubscribe(super.getTopicoMqtt());
	}
}
