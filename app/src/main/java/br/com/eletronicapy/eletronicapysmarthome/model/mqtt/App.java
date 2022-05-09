package br.com.eletronicapy.eletronicapysmarthome.model.mqtt;

import java.text.SimpleDateFormat;

public class App {

    public static void main(String[] args) throws InterruptedException {
    	System.out.println("Começou");
        ClienteMQTT clienteMQTT = new ClienteMQTT("tcp://broker.mqttdashboard.com:1883", null, null);
        clienteMQTT.iniciar();
        System.out.println("Terminou");

        new Ouvinte(clienteMQTT, "br/com/nei", 0);

        while (true) {
            Thread.sleep(1000);
            String mensagem = "Mensagem enviada em " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(System.currentTimeMillis()) + " - Teste do nei";

            clienteMQTT.publicar("br/com/nei", mensagem.getBytes(), 0);
        }
    }
}
