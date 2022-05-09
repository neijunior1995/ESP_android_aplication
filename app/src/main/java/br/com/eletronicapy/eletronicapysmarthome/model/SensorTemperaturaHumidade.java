package br.com.eletronicapy.eletronicapysmarthome.model;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import br.com.eletronicapy.eletronicapysmarthome.model.mqtt.ClienteMQTT;
import br.com.eletronicapy.eletronicapysmarthome.model.mqtt.Ouvinte;

public class SensorTemperaturaHumidade extends DispositivoEletronico {
	
	private Ouvinte ouvinte;
	private String msg = "oi";
	private ObjectMapper mapper = new ObjectMapper();

	public SensorTemperaturaHumidade(int id, String topico, ClienteMQTT cliente) {
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
	
	public String getjson(String chaveJson) {
		try {
			JsonNode node = mapper.readTree(this.msg);
			return node.get(chaveJson).asText();
		} catch (Exception e) {
			return "sem dados do sensor";
		} 
		}
	public String getTemperatura(){
		return getjson("temperatura") + " °C";
	}
	
	public String getHumidade(){
		return getjson("humidade") + "%";
	}
	
	public String getCoeficiente(){
		return getjson("coeficiente")  + " °C";
	}

	}
