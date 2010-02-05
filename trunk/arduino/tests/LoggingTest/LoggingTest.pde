/**
 * DAS Auto
 * 
 * Bret Kuhns, Chip Fursdon, Piper Wakefield
 * 
 * == LoggingTest ==
 *
 * This is a basic test of logging the 3-axis accelerometer analog values to the microSD
 * "OpenLog" microcontroller from Sparkfun Electronics. Data is output in a basic Comma-
 * separated values (CSV) format in standard ASCII on a FAT16-formatted microSD card.
 */
 
#include <WString.h>


// Globals (eww)
#define  xAxis  0;
#define  yAxis  1;
#define  zAxis  2;
#define  statusLED  13;

String buffer = "";


void setup() {
  Serial.begin(9600);
  
  pinMode(statusLED, OUTPUT);
  
  // Generic blink pattern to indicate the board is pausing for a few seconds.
  for(int i = 0; i < 5; i++) {
    digitalWrite(statusLED, HIGH);
    delay(100);
    digitalWrite(statusLED, LOW);
    delay(900); 
  }
  
  // Now start reading from OpenLog over serial. We want to verify we recieved
  // the "12<" prompt before continuing.
  int prevMessage = (int)Serial.read();
  while(prevMessage != 49) { // Busy wait until we recieve a '1'.
    prevMessage = (int)Serial.read();
  }
  if((int)Serial.read() == 50) { // Received '2'.
    if((int)Serial.read() == 60) { // Received '<'.
      for(int i = 0; i < 5; i++) {
        // Perform a new blink pattern to indicate we're ready to start logging.
        digitalWrite(statusLED, HIGH);
        delay(25);
        digitalWrite(statusLED, LOW);
        delay(475);
      }
    }
  }
  
  delay(2000); // Pause again for good measure before logging.
}


void loop() {
  digitalWrite(statusLED, LOW); // Turn off the activity LED.
  
  // Get the current milliseconds timestamp and all 3-axis of acceleration data.
  unsigned long timestamp = millis();
  int xAxisVal = analogRead(xAxis);
  int yAxisVal = analogRead(yAxis);
  int zAxisVal = analogRead(zAxis);
  
  // Construct a String buffer in CSV format to print to the card.
  buffer = "";
  buffer.append((long)timestamp);
  buffer.append(',');
  buffer.append(xAxisVal);
  buffer.append(',');
  buffer.append(yAxisVal);
  buffer.append(',');
  buffer.append(zAxisVal);  
  
  digitalWrite(statusLED, HIGH); // Indicate we are writing data.
  Serial.println(buffer); // Print the buffer to the card.
  
  delay(1); // Let the microSD card catch it's breath for a moment.
}

