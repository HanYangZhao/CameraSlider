
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
int stepper_accel = 400;
long init_pos = 0;
long current_pos = 0;
bool accel_enabled = false;

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
   stepper.setMaxSpeed(2000);
 }

 void moveSlider(String direction , int speed){
  stepper.setSpeed(speed); 
  stepper.setAcceleration(stepper_speed / 2);
  if(direction.equals("left")){
    Serial.print("moving left");
    //stepper.moveTo(stepper.currentPosition() + 5);
    //stepper.setSpeed(speed); 
    stepper.runToNewPosition (stepper.currentPosition() + 5);
    
  }      
  else{
    Serial.print("moving right");
    //stepper.moveTo( stepper.currentPosition() -5);
    //stepper.setSpeed(speed); 
    stepper.runToNewPosition(stepper.currentPosition() - 5);
  }
}

void changeSpeed(int speed){
  stepper_speed = speed;
  Serial.println(stepper_speed);
}
void processGetRequest(String url){
  char * req = strdup(url.c_str());
  strtok(req, "?");
  String name = strtok(NULL, "=");
  int value = atoi(strtok(NULL , "="));
  Serial.println(name);
  Serial.println(value);

  if(name.equals("left"))
    moveSlider("left" , stepper_speed );
  else if(name.equals("right"))
    moveSlider("right" , stepper_speed );
  else if(name.equals("speed"))
    changeSpeed(value);
}

void loop() {
  //stepper.runSpeedToPosition ();
  // Check if a client has connected

  
  if(stepper.distanceToGo() == 0){
    WiFiClient client = server.available();
    if (!client) {
      return;
    }
          // Wait until the client sends some data
    Serial.println("new client");
    while(!client.available()){
      delay(1);
    }
    // Read the first line of the request   
    processGetRequest(client.readStringUntil('\r'));
    client.flush();
    client.print(s);
    client.stop();
  }
}






