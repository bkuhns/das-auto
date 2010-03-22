#include <NewSoftSerial.h>

NewSoftSerial gps(2, 3);
int xAxis = 0;
int yAxis = 1;
int zAxis = 2;

#define BUFFER_SIZE  64
static char buffer[BUFFER_SIZE];

#define LINE_LENGTH 256
char currentLine[LINE_LENGTH];
int currentLineIndex = 0;


int strpos(char* haystack, char* needle) {
  char *p = strstr(haystack, needle);
  if(p) {
    return p - haystack;
  }
  
  return -1; // Not found
}


void setup() {
  gps.begin(38400);
  Serial.begin(115200);
}


void loop() {
  while(gps.available()) {
    char currentChar = gps.read();
    if(currentChar != '\n') {
      currentLine[currentLineIndex] = currentChar;
      currentLineIndex++;
    } else {
      if(currentLine[0] == '$' && currentLine[1] == 'G' && currentLine[2] == 'P' && currentLine[3] == 'R' && currentLine[4] == 'M' && currentLine[5] == 'C') {
        int index = 1;
        for(int i = 7; i < LINE_LENGTH; i++) {
          if(currentLine[i] != ',') {
            if(index == 1 || (index >= 3 && index <= 9)) {
              Serial.print(currentLine[i]);
            }
          } else {
            if(index == 1 || (index >= 3 && index < 9)) {
              Serial.print(",");
            }
            index++;
          }
        }
        Serial.print('\n');
      }
      for(int i = 0; i <= currentLineIndex; i++) {
        currentLine[i] = '\0';
      }
      currentLineIndex = 0;
    }
  }
  
  // Get the current milliseconds timestamp and all 3-axis of acceleration data.
  unsigned long timestamp = millis();
  int xAxisVal = analogRead(xAxis);
  int yAxisVal = analogRead(yAxis);
  int zAxisVal = analogRead(zAxis);
  
  // Combine all the values into the string buffer.
  sprintf(buffer, "#%lu,%d,%d,%d#", timestamp, xAxisVal, yAxisVal, zAxisVal);
  Serial.println(buffer); // Print the buffer to the card.
}


