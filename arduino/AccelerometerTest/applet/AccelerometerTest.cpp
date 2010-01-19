/*
  Analog Input
 Demonstrates analog input by reading an analog sensor on analog pin 0 and
 turning on and off a light emitting diode(LED)  connected to digital pin 13. 
 The amount of time the LED will be on and off depends on
 the value obtained by analogRead(). 
 
 The circuit:
 * Potentiometer attached to analog input 0
 * center pin of the potentiometer to the analog pin
 * one side pin (either one) to ground
 * the other side pin to +5V
 * LED anode (long leg) attached to digital output 13
 * LED cathode (short leg) attached to ground
 
 * Note: because most Arduinos have a built-in LED attached 
 to pin 13 on the board, the LED is optional.
 
 
 Created by David Cuartielles
 Modified 16 Jun 2009
 By Tom Igoe
 
 http://arduino.cc/en/Tutorial/AnalogInput
 
 */

#include "WProgram.h"
void setup();
void loop();
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

int main(void)
{
	init();

	setup();
    
	for (;;)
		loop();
        
	return 0;
}

