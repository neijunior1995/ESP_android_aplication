package br.com.eletronicapy.eletronicapysmarthome.ui;

import androidx.appcompat.app.AppCompatActivity;
import static org.apache.commons.lang3.StringUtils.isNumeric;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import br.com.eletronicapy.eletronicapysmarthome.R;
import br.com.eletronicapy.eletronicapysmarthome.model.CaixaDeAgua;
import br.com.eletronicapy.eletronicapysmarthome.model.LedRGB;
import br.com.eletronicapy.eletronicapysmarthome.model.PortaEletronica;
import br.com.eletronicapy.eletronicapysmarthome.model.SensorTemperaturaHumidade;
import br.com.eletronicapy.eletronicapysmarthome.model.mqtt.ClienteMQTT;

public class SmartHomeActivity extends AppCompatActivity {
    TextView nivelCaixa;
    TextView humidade;
    TextView temperatura;
    TextView coeficiente;
    PortaEletronica portaEletronica;
    LedRGB ledRGB;
    ClienteMQTT clienteMQTT = new ClienteMQTT("tcp://mqtt.eclipseprojects.io:1883", null, null);
    SensorTemperaturaHumidade sensor;
    CaixaDeAgua caixa;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ledRGB = new LedRGB();
        ledRGB.setCliente(clienteMQTT);
        ledRGB.setTopicoMqtt("labnei/ledRGB");
        clienteMQTT.iniciar();
        sensor = new SensorTemperaturaHumidade(1, "labnei/sensorHumidade", clienteMQTT);
        caixa = new CaixaDeAgua(1, "labnei/sensorcaixadeagua", clienteMQTT);
        portaEletronica = new PortaEletronica(2,"labnei/portaeletronica",clienteMQTT);
        setContentView(R.layout.activity_smart_home);

        humidade    = findViewById(R.id.smarthome_mostra_humidade);
        temperatura = findViewById(R.id.smarthome_mostra_temperatura);
        coeficiente = findViewById(R.id.smarthome_mostra_indice_temperatura);
        nivelCaixa  = findViewById(R.id.smarthome_mostra_nivel);

        content();

        Button destrancaPorta = findViewById(R.id.smarthome_button_destrancar);
        destrancaPorta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView estadoDaPorta = findViewById(R.id.smarthome_mostra_porta_da_frente);
                if (portaEletronica.getTrancado()) {
                    portaEletronica.destrancar();
                    portaEletronica.enviarComando();
                    estadoDaPorta.setText("Destrancada");
                }else {
                    portaEletronica.trancar();
                    portaEletronica.enviarComando();
                    estadoDaPorta.setText("Trancada");
                }

            }
        });

        Button enviaComandoLed = findViewById(R.id.button_smathome_configura_led);
        enviaComandoLed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText ledAzul     = findViewById(R.id.smarthome_configura_azul);
                EditText ledVerde    = findViewById(R.id.smarthome_configura_verde);
                EditText ledVermelho = findViewById(R.id.smarthome_configura_vermelho);

                String sred  = ledVermelho.getText().toString();
                String sblue  = ledAzul.getText().toString();
                String sgreen = ledVerde.getText().toString();

                if (isNumeric(sred.replace('.','1')) &&
                        isNumeric(sgreen.replace('.','1'))&&
                        isNumeric(sblue.replace('.',','))){
                    int red = Integer.parseInt(sred);
                    int blue = Integer.parseInt(sblue);
                    int green = Integer.parseInt(sgreen);

                    ledRGB.configuraLed(red,green,blue);
                    ledRGB.enviarComando();
                }
            }
        });
    }

    public void content(){
        count++;
        sensor.carregaMenssagem();
        caixa.carregaMenssagem();
        humidade.setText(sensor.getHumidade());
        temperatura.setText(sensor.getTemperatura());
        coeficiente.setText(sensor.getCoeficiente());
        nivelCaixa.setText(caixa.getMsg()+"%");
        atualiza(1000);
    }

    private void atualiza(int milisegundo){
        final Handler handler = new Handler();

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                content();
            }
        };
        handler.postDelayed(runnable,milisegundo);
    }
}