#include <NewSoftSerial.h>

NewSoftSerial gps(2, 3);
int xAxis = 0;
int yAxis = 1;
int zAxis = 2;

#define SENSOR_ID_GPS    "01"
#define SENSOR_ID_ACCEL  "02"

#define GPS_LINE_LENGTH 256
char gpsCurrentLine[GPS_LINE_LENGTH];
int gpsCurrentLineIndex = 0;

unsigned long lastMillis = 0;


void setup() {
  gps.begin(38400);
  Serial.begin(115200);
}


void printGpsLine(unsigned long *lastMillis) {
  while(gps.available()) {
    char currentChar = gps.read();
    if(currentChar != '\n') {
      gpsCurrentLine[gpsCurrentLineIndex] = currentChar;
      gpsCurrentLineIndex++;
    } else {
      if(strncmp(gpsCurrentLine, "$GPRMC", 6) == 0) {  // Make sure we're only printing the RMC line.
        Serial.print(SENSOR_ID_GPS);
        Serial.print(':');

        // Tokenize the current line using comma as a delimiter and extract only the values we want.
        int index = 0;
        char *dummy;
        char *currentChunk = strtok_r(gpsCurrentLine, ",", &dummy);
        while(currentChunk != NULL) {
          if(index == 1 || (index >= 3 && index <= 9)) {
            Serial.print(currentChunk);
            if(index < 9) {
              Serial.print(','); 
            }
          }
          currentChunk = strtok_r(NULL, ",", &dummy);
          index++;
        }

        Serial.print('\n');

        *lastMillis = millis();
      }
      
      // Clear the line so it's ready to use again.
      for(int i = 0; i <= gpsCurrentLineIndex; i++) {
        gpsCurrentLine[i] = '\0';
      }
      gpsCurrentLineIndex = 0;
    }
  }
}


void printAccelerometerLine(unsigned long lastMillis) {
  unsigned long timestamp = millis();
  int xAxisVal = analogRead(xAxis);
  int yAxisVal = analogRead(yAxis);
  int zAxisVal = analogRead(zAxis);

  char buffer[64];
  sprintf(buffer, "%s:%lu,%d,%d,%d", SENSOR_ID_ACCEL, timestamp-lastMillis, xAxisVal, yAxisVal, zAxisVal);

  Serial.println(buffer);
}


void loop() {
  printGpsLine(&lastMillis);
  printAccelerometerLine(lastMillis);
}



