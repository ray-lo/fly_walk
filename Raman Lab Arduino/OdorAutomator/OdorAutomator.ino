
#include <Arduino.h>
#include <Wire.h>
#include "ST7036.h"

ST7036 lcd = ST7036 ( 2, 16, 0x7c );

int enable = 2;
int a0 = 3;
int a1 = 4;
int a2 = 8;
int a3 = 7;


void setup() {
  Serial.begin(9600);
  lcd.init ();
  lcd.setContrast(0);
  pinMode(enable, OUTPUT);
  pinMode(a0, OUTPUT);
  pinMode(a1, OUTPUT);
  pinMode(a2, OUTPUT);
  pinMode(a3, OUTPUT);
  digitalWrite(enable, LOW);
  digitalWrite(a0, LOW);
  digitalWrite(a1, LOW);
  digitalWrite(a2, LOW);
  digitalWrite(a3, LOW);
  //lcd.print("lol");

  boolean handShake = false;

  while (!handShake) {
    //wait
    if (Serial.available() > 0) {
      if (Serial.read() == 33) {
        handShake = true;
        Serial.write(33);
        lcd.print("Shook Hands!");
      }
    }
  }

}

void loop() {
  //  for (int i = 1; i <= 16; i++) {
  //    signalMaker(i);
  //    //Serial.println(i);
  //    delay(500);
  //    digitalWrite(a3, HIGH);
  //  }
  if (Serial.available() > 0) {
    lcd.clear();
    //  nullSignal();
    byte current = Serial.read();
    signalMaker(current);
    lcd.print("Channel: ");
    lcd.print(current);
  }
}
void signalMaker(byte channelNumber) {
  if (channelNumber == 0) {
    digitalWrite(enable, LOW);
    digitalWrite(a0, LOW);
    digitalWrite(a1, LOW);
    digitalWrite(a2, LOW);
    digitalWrite(a3, LOW);
  }
  else if (channelNumber >= 1 && channelNumber < 17) {
    // Serial.println("I am here");
    digitalWrite(enable, HIGH);
    for (int i = 0; i < 4; i ++) {
      if (channelNumber - 1 & simpleExp(3 - i)) { //bitMasking
        digitalWrite(a3 - i, HIGH);//this line seems problematic
        //  lcd.print("1");
      }
      else {
        digitalWrite(a3 - i , LOW);
        //    lcd.print("0");
      }
    }
    //Serial.println("");
  }
}
void nullSignal() {
  digitalWrite(enable, LOW);
  digitalWrite(a0, LOW);
  digitalWrite(a1, LOW);
  digitalWrite(a2, LOW);
  digitalWrite(a3, LOW);
}
byte simpleExp(int power) {
  if (power == 0) {
    return 1;
  }
  if (power == 1) {
    return 2;
  }
  if (power == 2) {
    return 4;
  }
  if (power == 3) {
    return 8;
  }
}

