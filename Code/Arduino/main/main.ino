
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

int stepper_speed = 800;
int max_stepper_speed = 2000;
int stepper_accel = 400;
long init_pos = 0;
long current_pos = 0;
long steps = 2000;
bool accel_enabled = false;
bool isStop = false;

String s = "HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n<!DOCTYPE HTML>\r\n<html>\r\n</html>\n" ;

WiFiServer server(80);

/* Just a little test message.  Go to http://192.168.4.1 in a web browser
 * connected to this access point to see it.
 */

 void setup() {
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
   stepper.setMaxSpeed(3000);
   stepper.setCurrentPosition(stepper.currentPosition ());
   Serial.println("current_position");
   Serial.println(stepper.currentPosition());
 }
 void moveSlider(String direction , int speed){
    stepper.setSpeed(speed); 
  
  if (direction.equals("left")){
    Serial.println("moving left");
    //stepper.setSpeed(speed); 
    stepper.move(steps);
      
  }      
  else if (direction.equals("right")) {
    long start_pos = stepper.currentPosition();
    Serial.println("moving right");
    //stepper.setSpeed(speed);
    stepper.move(-steps);
  }

  else if (direction.equals("reset")){
    Serial.println("reset to 0");
    stepper.moveTo(0);
  }
}
void changeSpeed(int speed){
  stepper_speed = speed;
  stepper.setAcceleration(speed/2);
  Serial.println(stepper_speed);
}

void processGetRequest(String url){
  char * req = strdup(url.c_str());
  strtok(req, "?");
  String name = strtok(NULL, "=");
  int value = atoi(strtok(NULL , "="));
  Serial.println(name);
  Serial.println(value);

  if (name.equals("left")){
    moveSlider("left" , stepper_speed );
    isStop = false;
  }
  else if (name.equals("right")){
    moveSlider("right" , stepper_speed );
    isStop = false;
  }
  else if (name.equals("speed"))
    changeSpeed(value);
  else if (name.equals("stop")){
    Serial.println("stopping");
    isStop = true;
  }
  else if( name.equals("reset_pos")){
    moveSlider("reset" , max_stepper_speed );
  }
}

void loop() {
  // Check if a client has connected
  if (isStop){
    stepper.stop();
    //Serial.println("stopped");
  }
  else if (!isStop && stepper.distanceToGo() != 0){
    //Serial.println("running");
    stepper.runSpeed();
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






