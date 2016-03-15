
/* Create a WiFi access point and provide a web server on it. */

#include <ESP8266WiFi.h>
#include <WiFiClient.h> 
#include <ESP8266WebServer.h>
#include <AccelStepper.h>
#include "Timer.h";

AccelStepper stepper(1,5,4);
//pin 5 step, pin 4 dir

/* Set these to your desired credentials. */
const char *ssid = "CameraSlider";
const char *password = "thereisnospoon";

float stepper_speed = 10000;
int max_stepper_speed = 25000;
int stepper_accel = 10000;
int timelapse_mins = 60;
long init_pos = 0;
long current_pos = 0;
long steps = 29000;
bool accel_enabled = false;
bool isStop = false;
int MS1 = 12;
int MS2 = 13;
int MS3 = 14;

String s = "HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n<!DOCTYPE HTML>\r\n<html>\r\n</html>\n" ;

WiFiServer server(80);

/* Just a little test message.  Go to http://192.168.4.1 in a web browser
 * connected to this access point to see it.
 */

 void setup() {
   pinMode(MS1, OUTPUT);
   pinMode(MS2, OUTPUT);
   pinMode(MS3, OUTPUT);
   //Microstepping is set to 1/4
   digitalWrite(MS1, LOW);
   digitalWrite(MS2, HIGH);
   digitalWrite(MS3, LOW);
   delay(1000);
   Serial.begin(115200);
   Serial.println();
   Serial.print("Configuring access point...");
  /* You can remove the password parameter if you want the AP to be open. */
   WiFi.softAP(ssid, password);

   IPAddress myIP = WiFi.softAPIP();
   Serial.print("AP IP address: ");
   Serial.println(myIP);
   server.begin();
   Serial.println("server started");
   stepper.setMaxSpeed(max_stepper_speed);
   stepper.setCurrentPosition(0);
   Serial.println("current_position");
   Serial.println(stepper.currentPosition());
 }
 void moveSlider(String direction){ 
  if (direction.equals("left")){
    Serial.println("moving left");
    //stepper.setSpeed(speed); 
    stepper.move(steps);
    stepper.setSpeed(stepper_speed); 
  }      
  else if (direction.equals("right")) {
    long start_pos = stepper.currentPosition();
    Serial.println("moving right");
    stepper.move(-steps);
    stepper.setSpeed(-stepper_speed);
  }

  else if (direction.equals("reset")){
    Serial.println("reset to 0");
    stepper.move(stepper.currentPosition()); 
    stepper.setSpeed(stepper_speed); 
  }
}
void changeSpeed(float mot_speed){
  stepper_speed = mot_speed;
  stepper.setAcceleration(mot_speed/4);
  Serial.println(stepper_speed);
}

void timelapse(String direction){
  int timelapse_speed = (int) 30287 * pow(2.7183, (-0.013 * timelapse_mins));
  if (timelapse_speed > 25000)
    timelapse_speed = 25000;
  if (direction.equals("left")){
     stepper.move(steps);
     stepper.setSpeed(timelapse_speed);     
  }

  else if (direction.equals("right")){
    stepper.move(-steps);
    stepper.setSpeed(-timelapse_speed);
  }
}

void processGetRequest(String url){
  char * req = strdup(url.c_str());
  float rev_speed = 0.0f;
  int rev_data = 0;
  strtok(req, "?");
  String name = strtok(NULL, "=");
  if (name.equals("speed"))
    rev_speed = atof(strtok(NULL , "="));
  else {
     rev_data = atoi(strtok(NULL , "="));
  }
  
  Serial.println(name);

  if (name.equals("left")){
    moveSlider("left");
    isStop = false;
  }
  else if (name.equals("right")){
    moveSlider("right");
    isStop = false;
  }
  else if (name.equals("speed"))
    changeSpeed(rev_speed);
  else if (name.equals("stop")){
    Serial.println("stopping");
    isStop = true;
  }
  else if (name.equals("reset_pos")){
    moveSlider("reset");
    isStop = false;
  }

  else if (name.equals("steps")){
    steps = rev_data;
  }
  else if (name.equals("recalibrate")){
    stepper.setCurrentPosition(0);
    Serial.println(stepper.currentPosition());
  }
  else if (name.equals("timelapse_right")){
    timelapse("right");
  }
  else if (name.equals("timelaspe_left")){
    timelapse("left");
  }
  else if (name.equals("mins")){
    timelapse_mins = rev_data;
  }

  else if (name.equals("accel_enabled")){
    accel_enabled = rev_data;
  }

  else if (name.equals("set_accel")){
    stepper.setAcceleration(rev_data);
  }
}
void loop() {
  // Check if a client has connected
  if (isStop){
    stepper.stop();
    //Serial.println("stopped");
  }
  else if (!isStop && stepper.distanceToGo() != 0){
    if (!accel_enabled)
      stepper.runSpeed();
    else if (accel_enabled)
      stepper.run();
    Serial.println(stepper.currentPosition());
  }

  
  WiFiClient client = server.available();
  if (!client) {
   return;
 }
    //Wait until the client sends some data
 Serial.println("new client");
 while (!client.available()){
  delay(1);
}
  // Read the first line of the request   
  processGetRequest(client.readStringUntil('\r'));
  client.print(s);
  client.stop();
  
}






