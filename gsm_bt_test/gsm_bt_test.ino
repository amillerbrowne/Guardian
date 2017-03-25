#include <SoftwareSerial.h>

#define BT_TX_PIN 11
#define BT_RX_PIN 12
byte input = 0;
char phone_no[] = "16179357165";
//hardcoded for now, will replace with an actual location
char location[] = "BU";
//later will be replaced with the actual number
char text-to-911[] = "123";
//button mode 1
const int button1 = 1;
//button mode 2 - emergency
const int button2 = 2;

int button1State = 0;
bool check1 = 1; //for canceling logic
int button2State = 0;
bool check2 = 1; //foe canceling logic

const int led1 = 12;
const int led2 = 13;

SoftwareSerial bluetoothModule = SoftwareSerial(BT_RX_PIN, BT_TX_PIN);

void setup() {
  // put your setup code here, to run once:
  pinMode(BT_RX_PIN, INPUT);
  pinMode(BT_TX_PIN, OUTPUT);
  pinMode(button1, INPUT);
  pinMode(button2, INPUT);
  pinMode(led1, OUTPUT);
  pinMode(led2, OUTPUT);

  Serial.begin(9600);
  bluetoothModule.begin(9600);
}

void loop() {

  //Get the phone number and address from the phone
  
  while(bluetoothModule.available() > 0) {
    input = bluetoothModule.read();
    Serial.println(input);
  }
  if (sizeof(input) == 11)
  {
    phone_num = input; //latest phone number from the DB
  }
  else if (sizeof(input) > 11)
  {
    location = input; //latest location from the DB
  }

  button1State = sigitalRead(button1);
  button2State = sigitalRead(button2);
  
  //check if button1 is pressed
  if(button1State == HIGH)
  {
    if (check1)
    {
      //turn led1 on
      digitalWrite(led1, HIGH); 
      check1 != check1; //toggle state
      //Send a message to an emergency contact
      sendTextMessage(phone_num, "Hi, I am not feeling safe. My location is: " + location);
      //Also call the emergency contact to get attention
      sendVoiceCall(phone_num); 
    }
    else //canceling
    {
      //turn led1 off
      digitalWrite(led1, LOW); 
      check1 != check1;
      //Send a message to an emergency contact
      sendTextMessage(phone_num, "Sorry, that was a false alarm.");
      //hang up the call
      Serial.println("ATH");
    }
  }

  //check if button2 is pressed
  if(button2State == HIGH)
  {
    if (check2)
    {
      //turn led2 on
      digitalWrite(led2, HIGH); 
      check2 != check2;
      //Send a text to 911
      sendTextMessage(text-to-911, "Hi,I am in trouble. Please, help. My location is: " + location);
      //Send a message to an emergency contact
      sendTextMessage(phone_num, "Hi,I am in trouble. Please, call 911. My location is: " + location);
      //Also call the emergency contact to get attention
      sendVoiceCall(phone_num); 
    }
    else //canceling
    {
      //turn led2 off
      digitalWrite(led2, LOW); 
      check2 != check2;
      //Send a text to 911
      sendTextMessage(text-to-911, "I am sorry, this was a false alarm");
      //Send a message to an emergency contact
      sendTextMessage(phone_num, "Sorry, that was a false alarm.");
      //hang up the call
      Serial.println("ATH");
    }
  }

}

void sendTextMessage(char number[], char message[]) {
  delay(300);
  Serial.println("AT+CMGF=1");    
  delay(2000);
  Serial.print("AT+CMGS=\"");
  Serial.print(number); 
  Serial.write(0x22);
  Serial.write(0x0D);  // hex equivalent of Carraige return    
  Serial.write(0x0A);  // hex equivalent of newline
  delay(2000);
  Serial.print(message);
  delay(500);
  Serial.println (char(26));//the ASCII code of the ctrl+z is 26
}

void sendVoiceCall(char number[]) {  
  delay(200);
  Serial.println("AT");
  delay(1000);
  Serial.print("ATD");
  Serial.println(number);
  //Serial.println(";");
  delay(20000);
  //Serial.println("ATH");
}

