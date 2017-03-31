#include <SoftwareSerial.h>

#define led 5
#define BT_TX_PIN 11
#define BT_RX_PIN 12
byte input =0;

SoftwareSerial bluetoothModule = SoftwareSerial(BT_RX_PIN, BT_TX_PIN);

void setup(){
pinMode(BT_RX_PIN, INPUT);
pinMode(BT_TX_PIN,OUTPUT);
pinMode(led, OUTPUT);

Serial.begin(9600);
bluetoothModule.begin(9600);
}

void loop(){
  while(bluetoothModule.available() >0){
    input=bluetoothModule.read();
    Serial.print(input);
  }

  ledBright(input);
}
  void ledBright(int brightness){
    analogWrite(led,brightness);
}

