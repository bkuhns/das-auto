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


// Globals (eww)
#define  DEBUG
#define  xAxis  0
#define  yAxis  1
#define  zAxis  2
#define  statusLED  13

#define BUFFER_SIZE  64
static char buffer[BUFFER_SIZE];


void setup() {
  Serial.begin(9600);
  
  // Set up the buffer with blank spaces.
  for(int i = 0; i < BUFFER_SIZE; i++) {
    buffer[i] = ' ';
  }
  
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
#ifndef  DEBUG
  int prevMessage = (int)Serial.read();
  while(prevMessage != 49) { // Busy wait until we recieve a '1'.
    prevMessage = (int)Serial.read();
  }
  if((int)Serial.read() == 50) { // Received '2'.
    if((int)Serial.read() == 60) { // Received '<'.
      for(int i = 0; i < 5; i++) {
        // Perform a new blink pattern to indicate we're ready to start logging.
        digitalWrite(statusLED, HIGH);
        delay(500);
        digitalWrite(statusLED, LOW);
        delay(500);
      }
    }
  }
#endif
  
  delay(2000); // Pause again for good measure before logging.
  
  // We should probably specify what version/build of the code we're running here and
  // print that to the file so that we can keep track of differences in formatting across
  // versions.
}


void loop() {
  digitalWrite(statusLED, LOW); // Turn off the activity LED.
  
  // Get the current milliseconds timestamp and all 3-axis of acceleration data.
  unsigned long timestamp = millis();
  int xAxisVal = analogRead(xAxis);
  int yAxisVal = analogRead(yAxis);
  int zAxisVal = analogRead(zAxis);
  
  // Combine all the values into the string buffer.
  sprintf(buffer, "%u,%d,%d,%d", timestamp, xAxisVal, yAxisVal, zAxisVal);
  
  digitalWrite(statusLED, HIGH); // Indicate we are writing data.
  Serial.println(buffer); // Print the buffer to the card.
  
  delay(1); // Let the microSD card catch it's breath for a moment.
}

