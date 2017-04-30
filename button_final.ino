int LED1 = 12;
int LED2 = 13;
int button = 2;

boolean LED1State = true;
boolean LED2State = false;

long buttonTimer = 0;
long longPressTime = 1700;

boolean buttonActive = false;
boolean longPressActive = false;

//variables to calculate time between button presses
//in case the user nervously presses the button too frequently
long tempTimer1 = 0;
long tempTimer2 = 0;

void setup() {

  pinMode(LED1, OUTPUT);
  pinMode(LED2, OUTPUT);
  pinMode(button, INPUT);
  Serial.begin(9600);

}

void loop()
{
  if (digitalRead(button) == HIGH) 
  {
    if (buttonActive == false) 
    {
      buttonActive = true;
      buttonTimer = millis();
    }

    if ((millis() - buttonTimer > longPressTime) && (longPressActive == false)) 
    {
      //Logic for long press - contact an emergency contact
      longPressActive = true;
      LED2State = !LED2State;
      //Long press on - on a different LED
      if(LED2State == true)
      {
        Serial.println("CONTACT ON");  
        //here would send the text
        digitalWrite(LED2, LED2State);
      }
      else //long press off
      {
        Serial.println("CONTACT OFF");  
        //here would send the text
        digitalWrite(LED2, LED2State);
      }
    }

  } 
  else 
  {
    if (buttonActive == true) 
    {
      if (longPressActive == true) 
      {
        longPressActive = false;
      } 
      else 
      {
        if(LED1State == true)
        {
          //Short press
          //time since last click
          tempTimer2 = millis();
          //see if the clicks are not too close to each other
          if (tempTimer2 - tempTimer1 > 4000) //about 5 seconds
          {
            Serial.println("EMERGENCY ON");
            //here would send the text
            digitalWrite(LED1, LED1State);
            LED1State = !LED1State;
          }
          //set timer2 to timer1 for next click
          //then see difference with following click
          tempTimer1 = tempTimer2;
        }
        else
        {
          //time since last click
          tempTimer2 = millis();
          //see if the clicks are not too close to each other
          if (tempTimer2 - tempTimer1 > 4000) //about 5 seconds
          {
            Serial.println("EMERGENCY OFF");
            //here would send the text
            digitalWrite(LED1, LED1State);
            LED1State = !LED1State;
          }
          //set timer2 to timer1 for next click
          //then see difference with following click
          tempTimer1 = tempTimer2;
        }
      }

      buttonActive = false;

    }

  }

}
