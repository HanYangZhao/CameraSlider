# CameraSlider

Smartphone controlled camera slider using 3D Printed parts and Esp8266

This project started with me wanting to take timelapse photos and videos where the camera is sliding on a set of rails. Because my primary application was timelapse photos, I wanted something that was motorized and automated so that I don't have to manually adjust the slider and cause inconsistency the the photos. Additionally, I wanted to be able to control the rig with a smartphone. Commercial products costs over 500$ and none of them are wireless so I thought I'd make something for much cheaper using 3D printed parts and off the shelf components. The final total cost was under 100$. It is still an ongoing project.

#TODO: 

-Put all the electronics in a box

-iOS app

![alt text](https://github.com/HanYangZhao/CameraSlider/blob/master/cad/render/1.JPG)

Designed using SolidWorks CAD

![alt text](https://github.com/HanYangZhao/CameraSlider/blob/master/Photos/DSC_7650.JPG)

![alt text](https://github.com/HanYangZhao/CameraSlider/blob/master/Photos/DSC_7649.JPG)




#Materials

2 x  1/2 inch copper tubing as rails. I used a meter for each rail.

8 x 608 bearings for the rollers

NEMA 17 Stepper motor with 2x20 Tooth GT2 gears to drive the plate.

1x ESP8266 Adafruit Huzzah  + FTDI cable

1x A4988 Stepper Motor Driver Carrier, Black Edition

5V power bank as power source with 2 output. 2A min on one the output

4 x 8mm bolts and nuts for the skateboard bearing

4 x 4mm bolts to and nuts attached the stepper motor plate to the chassis

2 x 3mm bolts to attached the stepper motor to the stepper motor plate

2 x 4 mm bolts and nuts to attached the belt to the bottom plate

1 x  mm bolt and nuts + washer to hold the free spinning gear to one of the plates

1 x Small box to put all the electronics.

1 x 470uF Cap

1 x 5V to 12V voltage converter

1 x 1/4 inch or 3/8 inch bolt to attach the camera

2 x 3.5mm power jack

2 x usb cables

#Hardware assembly

Hardware assembly is pretty self-explanatory. You can use the photos in the photo folder to get the idea. The top plate to support the camera includes a hole for 1/4 inch bolts and 3/8 inch bolts. You can either attach you camera directly to the plate or use a micro sliding plate to better control the balance of camera on the plate. This is especially useful for big and heavy camera like the d800.

<img src="https://github.com/HanYangZhao/CameraSlider/blob/master/Photos/DSC_7224_3000.jpg" alt="plate" style="width: 200px;"/>
<img src="https://github.com/HanYangZhao/CameraSlider/blob/master/Photos/DSC_7653.JPG" alt="plate" style="width: 200px;"/>
<img src="https://github.com/HanYangZhao/CameraSlider/blob/master/Photos/DSC_7654.JPG" alt="plate" style="width: 200px;"/>
<img src="https://github.com/HanYangZhao/CameraSlider/blob/master/Photos/DSC_7225_3000.jpg" alt="plate" style="width: 200px;"/>
<img src="https://github.com/HanYangZhao/CameraSlider/blob/master/Photos/DSC_7220_3000.jpg" alt="plate" style="width: 200px;"/>
<img src="https://github.com/HanYangZhao/CameraSlider/blob/master/Photos/DSC_7223_3000.jpg" alt="plate" style="width: 200px;"/>


To connect the electronics. Use [this guide] (https://www.pololu.com/product/2128)

![alt-text](https://a.pololu-files.com/picture/0J4069.600.png)

The MCU will be the esp8266 and the 8-35V input will be the output of the 5 to 12V converter (the 2A one)

The logic power supply will be from 2nd output of the power bank.

#Software

There are 2 folders. One for the esp8266 code and one for the Android app. The esp8266 acts as a WiFi AP. The Android phone connects to the Esp8266 and controls is by sending html messages.

You'll need AccelStepper library for the stepper motor (http://www.airspayce.com/mikem/arduino/AccelStepper)





