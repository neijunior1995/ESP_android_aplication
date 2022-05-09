package br.com.eletronicapy.eletronicapysmarthome.model.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Ouvinte implements IMqttMessageListener {
	
	private String msg;

    public Ouvinte(ClienteMQTT clienteMQTT, String topico, int qos) {
        clienteMQTT.subscribe(qos, this, topico);
    }

    @Override
    public void messageArrived(String topico, MqttMessage mm) throws Exception {
        this.msg = new String(mm.getPayload());
        if (this.msg == null) {
        	this.msg = "0";
        }
    }
    
    public String getMsg() {
		return msg;
	}

}
