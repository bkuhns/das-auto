#include <WString.h>

int xAxis = 0;
int yAxis = 1;
int zAxis = 2;
int statusLED = 13;

int button = 8;
int lastButton = HIGH;
String buffer = "";


void setup() {
  Serial.begin(9600);
  
  pinMode(statusLED, OUTPUT);
  
  for(int i = 0; i < 5; i++) {
    digitalWrite(13, HIGH);
    delay(100);
    digitalWrite(13, LOW);
    delay(900); 
  }
  
  int prevMessage = (int)Serial.read();
  while(prevMessage != 49) {
    prevMessage = (int)Serial.read();
  }
  if((int)Serial.read() == 50) { // Received '2'
    if((int)Serial.read() == 60) { // Received '<'
      for(int i = 0; i < 5; i++) {
        digitalWrite(statusLED, HIGH);
        delay(25);
        digitalWrite(statusLED, LOW);
        delay(500);
      }
    }
  }
  
  delay(2000);
}


void loop() {
  digitalWrite(statusLED, LOW);
  /*if(digitalRead(button) != lastButton) {
    Serial.println("Done logging.");
    while(true); 
  }*/
  /*if(Serial.available() > 0) {
    BYTE received = Serial.read();
    Serial.print(received);
    ser.print(received);
  }*/
  int timestamp = millis();
  int xAxisVal = analogRead(xAxis);
  int yAxisVal = analogRead(yAxis);
  int zAxisVal = analogRead(zAxis);
  
  buffer = "";
  buffer.append(timestamp);
  buffer.append(',');
  buffer.append(xAxisVal);
  buffer.append(',');
  buffer.append(yAxisVal);
  buffer.append(',');
  buffer.append(zAxisVal);  
  
  digitalWrite(statusLED, HIGH);
  Serial.println(buffer);
  Serial.flush();
  delay(10);
  
  lastButton = digitalRead(button);
}

