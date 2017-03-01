void setup() {
 Serial.begin(9600);

}
long deltaTime = 1000;
long nextTargetTime = 0;
int minutes;
int seconds;
void loop() {
   if (millis()>=nextTargetTime){
    minutes = nextTargetTime / 60000;
    seconds = (nextTargetTime % 60000) / 1000;
     Serial.print(minutes);
     Serial.print(":");
     Serial.println(seconds);
     nextTargetTime = nextTargetTime + deltaTime;
   }
  
}
