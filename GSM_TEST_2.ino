#include <SoftwareSerial.h>

#define BT_TX_PIN 7                // pin 7 on processor
#define BT_RX_PIN 6                // pin 6 on processor
byte input = 0;                    // data read from bluetooth module

//variables for dynamic phone number collection
char inData[200];                    // buffer of data in bytes; allocated for string
byte index = 0;                     // index of the data being read
String allData = "";                // buffer for data in string format
String number = "";                 // buffer for number to return

// kept as global - last known number
String allDataString = "";          // string of data
String lastNumber = "";          // assigned in the loop
String lastLatitude = "";               // current latitude value
String lastLongitude = "";              // current longitude value
String lastAccuracy = ""; 
String lastEmergency = "";
String correctNumber = "";

String message = "";
String beginText = "EMERGENCY! Please, SEND HELP! I am at location: ";
String location = "";
String correctLatitude = "";
String correctLongitude = "";
String urlMessage = "You can find the runner at this link: ";
String urlStart = "https://www.google.com/maps?q=";

SoftwareSerial bluetoothModule = SoftwareSerial(BT_RX_PIN, BT_TX_PIN);

const int  button1 = 2;    // the pin that the first button is attached to
const int led1 = 12;       // the pin that the first LED is attached to

const int  button2 = 4;    // the pin that the second button is attached to
const int led2 = 13;       // the pin that the second LED is attached to

int button1Counter = 1;   // counter for the number of button presses
int button1State = 0;         // current state of the button
int button1LastState = 0;     // previous state of the button

int button2Counter = 1;   // counter for the number of button presses
int button2State = 0;         // current state of the button
int button2LastState = 0;     // previous state of the button

void setup() {
  // initialize the first button pin as a input:
  pinMode(button1, INPUT);
  // initialize the first LED as an output:
  pinMode(led1, OUTPUT);
  // initialize the second button pin as a input:
  pinMode(button2, INPUT);
  // initialize the second LED as an output:
  pinMode(led2, OUTPUT);
  pinMode(BT_RX_PIN, INPUT);        // tell Arduino the BT_RX_PIN is an input
  pinMode(BT_TX_PIN, OUTPUT);       // tell Arduino the BT_TX_PIN is an output

  // initialize serial communication:
  Serial.begin(9600);
  bluetoothModule.begin(9600); 
}

void loop() {
  // read the first pushbutton input pin:
  button1State = digitalRead(button1);
  // read the second pushbutton input pin:
  button2State = digitalRead(button2);
   while(bluetoothModule.available() > 0) {
        input = bluetoothModule.read();
        Serial.println("testing bt");
        allDataString = allThem();                      // 32 chars total
  
        String currentEmergency = ""; //false means triigger emergency, true means false alarm
        String currentLatitude = "";               // current latitude value
        String currentLongitude = "";              // current longitude value
        String currentAccuracy = "";
        String currentNumber = "";          // assigned in the loop
        
        int delimiter1 = allDataString.indexOf(':');
        int delimiter2 = allDataString.indexOf(':', delimiter1 + 1);
        int delimiter3 = allDataString.indexOf(':', delimiter2 + 1);
        int delimiter4 = allDataString.indexOf(':', delimiter3 + 1);
        int endDelimiter = allDataString.indexOf(';'); // end of the road
         
        currentEmergency = allDataString.substring(0, delimiter1);
        currentLatitude = allDataString.substring(delimiter1 + 1, delimiter2);
        currentLongitude = allDataString.substring(delimiter2 + 1, delimiter3);
        currentAccuracy = allDataString.substring(delimiter3 + 1, delimiter4);
        currentNumber = allDataString.substring(delimiter4 + 1, endDelimiter);

        
         
         if((lastNumber.charAt(0) == '1' && lastNumber.length() == 11) || (lastNumber == "119"))
         {
          correctNumber = lastNumber;
         }
         if (lastLatitude.length() == 9)
         {
          correctLatitude = lastLatitude;
         }
         if (lastLongitude.length() == 10)
         {
          correctLongitude = lastLongitude;
         }
         message = beginText + correctLatitude + ", " + correctLongitude + "." + urlMessage + urlStart + correctLatitude + "," + correctLongitude;
         
        for (int i = 0; i < 52; i++) {
          if(allDataString.substring(i, i+1) == ";"){
            allDataString = "";          // string of data
            currentEmergency = "";
            currentNumber = "";          // assigned in the loop
            currentLatitude = "";               // current latitude value
            currentLongitude = "";              // current longitude value
            currentAccuracy = "";
            index = 0;
          }
          
        }
        
        lastEmergency = currentEmergency;
        lastNumber = currentNumber;          // assigned in the loop
        lastLatitude = currentLatitude;               // current latitude value
        lastLongitude = currentLongitude;              // current longitude value
        lastAccuracy = currentAccuracy;
        
        Serial.println("latitude2: " + lastLatitude);
        Serial.println("longitude2: " + lastLongitude);
        Serial.println("phone #: " + lastNumber);
            
         if (currentEmergency.equals("true")) {
            boolean sendPhone = checkSend(currentNumber);
            if(sendPhone == true)
            {
              //trigger text to be sent
              Serial.println("send emergency text here now");
              //sendTextMessage(lastNumber,"Hi, I am not feeling safe, my location is ...");
              sendPhone = false;
            }
          
        }
   }

       
  // compare the button1State to its previous state
  if (button1State != button1LastState) {
    // if the state has changed, increment the counter
    if (button1State == HIGH) {
      // if the current state is HIGH then the button
      // went from off to on:
      button1Counter++;
      if (button1Counter % 2 == 0)
      {
        //sending a text to the emergency contact
         Serial.println("text to contact");
         sendTextMessage(correctNumber, message);
      }
      else
      {
        //cancel the emergency contact text
        Serial.println("cancel");
        sendTextMessage(correctNumber,"Sorry, false alarm.");
      }
    } 
    // Delay a little bit to avoid bouncing
    delay(50);
  }
  // save the current state as the last state,
  //for next time through the loop
  button1LastState = button1State;

  // turns on the LED every four button pushes by
  // checking the modulo of the button push counter.
  // the modulo function gives you the remainder of
  // the division of two numbers:
  if (button1Counter % 2 == 0) {
    digitalWrite(led1, HIGH);
  } else {
    digitalWrite(led1, LOW);
  }

  // compare the button2State to its previous state
  if (button2State != button2LastState) {
    // if the state has changed, increment the counter
    if (button2State == HIGH) {
      // if the current state is HIGH then the button
      // wend from off to on:
      button2Counter++;
      if (button2Counter % 2 == 0)
      {
        //texting to 911
        Serial.println("emergency text here");
        sendTextMessage("119",message);
      } 
      else
      {
        //canceling text to 911
        Serial.println("emergency cancel");
        sendTextMessage("119","Sorry, false alarm.");
      }
    } 
    // Delay a little bit to avoid bouncing
    delay(50);
  }
  // save the current state as the last state,
  //for next time through the loop
  button2LastState = button2State;

  // turns on the LED every four button pushes by
  // checking the modulo of the button push counter.
  // the modulo function gives you the remainder of
  // the division of two numbers:
  if (button2Counter % 2 == 0) {
    digitalWrite(led2, HIGH);
  } else {
    digitalWrite(led2, LOW);
  }

}


/* sends text message with the activation of a button */
void sendTextMessage(String number, String message) {
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


/* sends voice call with the activation of a button */ 
void sendVoiceCall(String number) {  
  delay(200);
  Serial.println("AT");
  delay(1000);
  Serial.print("ATD");
  Serial.println(number);
  //Serial.println(";");
  delay(15000);                   // delays by 20,000 millisecond
  Serial.println("ATH");
}

// checks that number has the right parameters
boolean checkSend(String currentNo)
{
  if(currentNo == "119" || currentNo.length() == 11)
  {
    return true;
  }
  else
  {
    return false;
  }
  
}

String allThem()
{
  if(index < 200) // large enough buffer
    {
      inData[index] = input;        // Store it
      index++;                      // Increment where to write next
      inData[index] = '\0';         // Null terminate the string
      number = String(inData);      // convert byte array to string
   }
    return number;
  
}
