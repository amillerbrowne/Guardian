#include <SoftwareSerial.h>

#define led 5
#define BT_TX_PIN 11
#define BT_RX_PIN 12
byte input = 0;
char phone_no[];

SoftwareSerial bluetoothModule = SoftwareSerial(BT_RX_PIN, BT_TX_PIN);

void setup() {
  // put your setup code here, to run once:
  pinMode(BT_RX_PIN, INPUT);
  pinMode(BT_TX_PIN, OUTPUT);
  pinMode(led, OUTPUT);

  Serial.begin(9600);
  bluetoothModule.begin(9600);
}

void loop() {
  // put your main code here, to run repeatedly:

  while(bluetoothModule.available() > 0) {
    input = bluetoothModule.read();
    Serial.println(input);
  }

    if(input == 255)
    {
      sendTextMessage();
      input = 0;
    } 
    else if (input == 128)
    {
      sendVoiceCall();
      input = 0;
    }
    else if (input == 1)
    {
      Serial.println("ATH");
      input = 0;
    }
}

void sendTextMessage() {
  delay(300);
  Serial.println("AT+CMGF=1");    
  delay(2000);
  Serial.print("AT+CMGS=\"");
  Serial.print(phone_no); 
  Serial.write(0x22);
  Serial.write(0x0D);  // hex equivalent of Carraige return    
  Serial.write(0x0A);  // hex equivalent of newline
  delay(2000);
  Serial.print("GSM A6 test message!");
  delay(500);
  Serial.println (char(26));//the ASCII code of the ctrl+z is 26
}

void sendVoiceCall() {  
  delay(200);
  Serial.println("AT");
  delay(1000);
  Serial.print("ATD");
  Serial.println(phone_no);
  //Serial.println(";");
  delay(20000);
  //Serial.println("ATH");
}

