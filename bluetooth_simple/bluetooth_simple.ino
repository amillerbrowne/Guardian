#include <SoftwareSerial.h>
//10 = Rx port
//11 = Tx Port
SoftwareSerial BM(10, 11);
int led = 13; // led on D13 will show blink on / off
int bData; // the data given from Computer
char phone_no[];

void setup() {
  BM.begin(9600);
  BM.println("Bluetooth On please press 1 or 0 blink LED ..");
  pinMode(led,OUTPUT);

  //GSM - use serial port for GSM communication
  Serial.begin(9600);
  delay(300);

  Serial.println("AT+CMGF=1");
  delay(2000);
  Serial.print("AT+CMGS=\"");
  Serial.print(phone_no);
  Serial.write(0x22);
  Serial.write(0x0D);
  Serial.write(0x0A);
  delay(2000);
  Serial.print("Alex Miller-Browne's Coordinates: 42.34 Latitude, -71.10 Longitude.");
  delay(500);
  Serial.println(char(26));

}

void loop() {
  if(BM.available())
  {
    bData = BM.read();
  
  if(bData == '1')
  {
    digitalWrite(led,1);
    BM.println("LED On ");
  }
  
  if(bData == '0')
  {
    digitalWrite(led,0);
    BM.println("LED Off ");
  }
  }
  delay(100);

}
