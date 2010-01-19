/*
 * This is just a simple test of the 3-axis accelerometer. The X axis is linked to the
 * blue pin of the RGB LED and the Y axis is linked to the red pin. The two fade based
 * on the tilt of the device.
 *
 * Last modified: 01.19.2010
 */

int accelXPin = 0;
int accelYPin = 1;
int ledRedPin = 9; 
int ledBluePin = 11;


void setup() {
  pinMode(ledRedPin, OUTPUT);  
  pinMode(ledBluePin, OUTPUT);  
}


void loop() {
  analogWrite(ledBluePin, analogRead(accelXPin));
  analogWrite(ledRedPin, analogRead(accelYPin));
}
