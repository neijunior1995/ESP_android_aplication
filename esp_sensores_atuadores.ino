#include <ESP8266WiFi.h>
#include <PubSubClient.h>
#include <ArduinoJson.h>
#include "DHT.h";
#include <Servo.h>
Servo myservo;
// Configuração da rede wifi.

const char* ssid = "FAMILIACAMPOS2G";
const char* password = "b77qfdp27w4h68r72b48";
WiFiClient espClient;

// Configuração da conexão com servidor mqtt

const char* mqtt_server = "mqtt.eclipseprojects.io";
PubSubClient client(espClient);
const char* topicled = "labnei/ledRGB";
const char* topicporta = "labnei/portaeletronica";
const char* outtopic = "labnei/sensor_de_presenca";
const char* sensor_temperatura = "labnei/sensorHumidade";
const char* sensor_caixa = "labnei/sensorcaixadeagua";
unsigned long lastMsg = 0;
#define MSG_BUFFER_SIZE  (100)
char msg[MSG_BUFFER_SIZE];
int value = 0;
String mensagemRecebida = "";
// Configurar portas do sensor de temperatura e humidade
#define DHTPIN 3
#define DHTTYPE DHT11
DHT dht(DHTPIN, DHTTYPE);
const char* jsondht = "";
String temp = "";
String humi = "";
String ctemp = "";
int flagteste = 0;
// Configuração das portas pwm

int D5 = 14;
int D6 =  12;
int D7 =  13;
int D4 =  2;

// Configurações do pwm

int red = 0;
int green = 0;
int blue = 0;
void setup_wifi() {

  delay(10);
  // Inicilaização da conexão
  Serial.println();
  Serial.print("Conectando na rede");
  Serial.println(ssid);

  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  randomSeed(micros());
  Serial.println("");
  Serial.println("Conectado ao wifi");
  Serial.println("Endereço de IP: ");
  Serial.println(WiFi.localIP());
}

void callback(char* topic, byte* payload, unsigned int length) {
  mensagemRecebida = "";
  for (int i = 0; i < length; i++) {
    mensagemRecebida = mensagemRecebida + (char)payload[i];
  }
  if (String(topic) == String(topicled)){
    DynamicJsonDocument doc(1024);
    Serial.println(mensagemRecebida);
    deserializeJson(doc, mensagemRecebida);
    JsonObject obj = doc.as<JsonObject>();
  
   if (mensagemRecebida.indexOf("Red")!= -1){
      red = obj[String("Red")];
    }
    if (mensagemRecebida.indexOf("Red")!= -1){
     green = obj[String("Green")];
    }
    if (mensagemRecebida.indexOf("Red")!= -1){
     blue = obj[String("Blue")];
    }

    Serial.print("Vermelho: ");
    Serial.println(red);

    Serial.print("Verde: ");
    Serial.println(green);

    Serial.print("Azul: ");
    Serial.println(blue);
    analogWrite(D5,red);
    analogWrite(D6,green);
    analogWrite(D7,blue);
  }else if(String(topic) == String(topicporta)){
    Serial.print("A porta está trancada: ");
    if (String(mensagemRecebida) == "true")
    {
      Serial.println("true");
      myservo.write(10);
    }
    else{
      Serial.println("false");
      myservo.write(150);
      }
    }

}

void reconnect() {
  // Laço para conectar!
  while (!client.connected()) {
    Serial.print("Tentando conectar com o MQTT...");
    // Criação de um ID aleatório
    String clientId = "ESP8266Client-";
    clientId += String(random(0xffff), HEX);
    // Tentando conexão
    if (client.connect(clientId.c_str())) {
      Serial.println("connected");
      client.subscribe(topicled);
      client.subscribe(topicporta);
    } else {
      Serial.print("failed, rc=");
      Serial.print(client.state());
      Serial.println(" try again in 5 seconds");
      // Wait 5 seconds before retrying
      delay(5000);
    }
  }
}

void setup() {
  pinMode(14, OUTPUT);     // Initialize the BUILTIN_LED pin as an output
  Serial.begin(115200);
  setup_wifi();
  client.setServer(mqtt_server, 1883);
  client.setCallback(callback);
  pinMode(D5,OUTPUT);
  pinMode(D6,OUTPUT);
  pinMode(D7,OUTPUT);
  myservo.attach(2);
  analogWriteFreq(10000);
  dht.begin();
}

void loop() {

  if (!client.connected()) {
    reconnect();
  }
  client.loop();

  unsigned long now = millis();
  if (now - lastMsg > 1000) {
    lastMsg = millis();
    /*if (digitalRead(D4)){
      Serial.println("Movimento Detectado");
      client.publish(outtopic, "1");
      }else{
        client.publish(outtopic, "0");
        Serial.println("Sem Movimento Detectado");
        }*/
    float h = dht.readHumidity();
    float t = dht.readTemperature();
    float hic = dht.computeHeatIndex(t, h, false);
    String jsondht = String(h,2);
    String temp = String(t,2);
    String humi = String(h,2);
    String ctemp = String(hic,2);
    jsondht = "{\"temperatura\":10,\"humidade\":10,\"coeficiente\":10}";
    snprintf (msg, MSG_BUFFER_SIZE, "{\"temperatura\":%.2f,\"humidade\":%.2f,\"coeficiente\":%.2f}",t,h,hic);
    client.publish(sensor_temperatura,msg);
    
    snprintf (msg, MSG_BUFFER_SIZE, "75");
    client.publish(sensor_caixa,msg);

  
    Serial.println(msg);
    flagteste = flagteste + 10;
    analogWrite(D4,flagteste);
  }
}
