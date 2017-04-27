int LED1 = 12;
int LED2 = 13;
int button = 2;

boolean LED1State = false;
boolean LED2State = false;

long buttonTimer = 0;
long longPressTime = 1700;

boolean buttonActive = false;
boolean longPressActive = false;

void setup() {

  pinMode(LED1, OUTPUT);
  pinMode(LED2, OUTPUT);
  pinMode(button, INPUT);
  Serial.begin(9600);

}

void loop() {

  if (digitalRead(button) == HIGH) {

    if (buttonActive == false) {

      buttonActive = true;
      buttonTimer = millis();

    }

    if ((millis() - buttonTimer > longPressTime) && (longPressActive == false)) {

      longPressActive = true;
      LED1State = !LED1State;
      if(LED1State == true)
      {
        Serial.println("EMERGENCY ON");  
      }
      else
      {
        Serial.println("EMERGENCY OFF");  
      }
      digitalWrite(LED1, LED1State);

    }

  } else {

    if (buttonActive == true) {

      if (longPressActive == true) {

        longPressActive = false;

      } else {

        LED2State = !LED2State;
        if(LED2State == true)
        {
          Serial.println("CONTACT ON");  
        }
        else
        {
          Serial.println("CONTACT OFF");  
        }
        digitalWrite(LED2, LED2State);

      }

      buttonActive = false;

    }

  }

}
