
#include <Arduino.h>
#include <Wire.h>


int enable = 2;
int a0 = 3;
int a1 = 4;
int a2 = 5;
int a3 = 6;
int a4 = 7;
int a5 = 8;
int a6 = 9;
int a7 = 10;
int a8 = 11;
int a9 = 12;


void setup() {
  Serial.begin(9600);
  pinMode(enable, OUTPUT);
  pinMode(a0, OUTPUT);
  pinMode(a1, OUTPUT);
  pinMode(a2, OUTPUT);
  pinMode(a3, OUTPUT);
  pinMode(a4, OUTPUT);
  pinMode(a5, OUTPUT);
  pinMode(a6, OUTPUT);
  pinMode(a7, OUTPUT);
  pinMode(a8, OUTPUT);
  pinMode(a9, OUTPUT);
  digitalWrite(enable, LOW);
  digitalWrite(a0, LOW);
  digitalWrite(a1, LOW);
  digitalWrite(a2, LOW);
  digitalWrite(a3, LOW);
  digitalWrite(a4, LOW);
  digitalWrite(a5, LOW);
  digitalWrite(a6, LOW);
  digitalWrite(a7, LOW);
  digitalWrite(a8, LOW);
  digitalWrite(a9, LOW);
  //lcd.print("lol");

  boolean handShake = false;

  while (!handShake) {
    //wait
    if (Serial.available() > 0) {
      if (Serial.read() == 33) {
        handShake = true;
        Serial.write(33);
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
    //  nullSignal();
    byte current = Serial.read();
    if (current == 64) {
      signalMaker();
    }
  }
}
void signalMaker() {
  int readBytes = 0;
  byte firstByte;
  while (readBytes < 2) {
    if (Serial.available() > 0) {
      if (readBytes == 0) {
        firstByte =  Serial.read();
        readBytes++;
      }
      else if (readBytes == 1) {
        byte secondByte = Serial.read();

        readBytes++;
        for (int i = 0; i < 8; i++) {
          if (firstByte & simpleExp(7 - i)) {
            digitalWrite(a0 + i, HIGH);
          }
        }
        for (int i = 0; i < 2; i++) {
          if (secondByte & simpleExp(7 - i)) {
            digitalWrite(a8 + i, HIGH);
          }
        }
      }
    }
  }
  /*
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
  */
}
void nullSignal() {
  digitalWrite(enable, LOW);
  digitalWrite(a0, LOW);
  digitalWrite(a1, LOW);
  digitalWrite(a2, LOW);
  digitalWrite(a3, LOW);
  digitalWrite(a4, LOW);
  digitalWrite(a5, LOW);
  digitalWrite(a6, LOW);
  digitalWrite(a7, LOW);
}
byte simpleExp(int power) {
  switch (power) {
    case 0:
      return 1;
      break;
    case 1:
      return 2;
      break;
    case 2:
      return 4;
      break;
    case 3:
      return 8;
      break;
    case 4:
      return 16;
      break;
    case 5:
      return 32;
      break;
    case 6:
      return 64;
      break;
    case 7:
      return 128;
      break;
    case 8:
      return 256;
      break;
    default:
      return 0;
      break;
  }
}

