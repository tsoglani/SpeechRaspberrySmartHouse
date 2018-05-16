INFORMATION

This is a smart house application. It allows the user to remotely interact with everything that uses electricity within the house. This includes lights, electric blinds, boilers, AC units and many more. The application supports multiple input methods. The Android application(link), network [PCs](https://github.com/tsoglani/Java_SmartHouseClient)  and terminals within the house.

INSTRUCTIONS

The application supports Rasberry 2b and Rasberry 3. The source code can be modified by changing the gpio pins libraries in order to run on any device using input or output pins.

In order to use the application:
1.	Clone or copy this project on raspberry pi side. Paste it in /home/pi/Desktop/   
2.	Go to the following folder: SpeechRasberrySmartHouse (should be on your desktop)
3.	Modify the following files:
•	 [Commands.txt](https://github.com/tsoglani/SpeechRaspberrySmartHouse/blob/master/commands.txt)  assign your devices to the respective output, devices after the double comma can be controlled by voice e.g. “kitchen_light,, kitchen_lamp@@0” after double @@ is the output, the    
      file is bind with gpio_input_output.txt, etc if we combine our examples, the kitchen light will assign to output 15/gpio15, 0->15 if it was after @@ the value 1 would go compare the 2 examples to 16.

•	[DeviceName.txt](https://github.com/tsoglani/SpeechRaspberrySmartHouse/blob/master/deviceName.txt)  (assign the user/s allowed to interact with the application)

•	[gpio_input_output.txt](https://github.com/tsoglani/SpeechRaspberrySmartHouse/blob/master/gpio_input_output.txt)   (assign pins to input and output e.g. "INPUT:0,1,2,3" means gpio 0-3 are assigned to input, respectively "Output:15,16,17" means gpio 15-17 are assigned to output, additionally you can assign pca9685 to external output by typing "External true")

•	  exclusiveOutputs.txt (type 
"
   exclusiveOutputs:1, 2, 3
   exclusiveOutputs:5, 6
"
 in order to to have only one of the aforementioned outputs run at a time. The input format must be line by line for every output combination will be assigned to your first selected output. In the aforementioned examples it will be assigned to output 15, respectively 1 will be assigned to 15, respectively 5, 6 will be assigned to the 5th and 6th output from the current file)

•	[port.txt](https://github.com/tsoglani/SpeechRaspberrySmartHouse/blob/master/port.txt) (lets you choose your preferred port. It must be in the following format "port:____". Default port is 2222)

•      [DeviceID.txt](https://github.com/tsoglani/SpeechRaspberrySmartHouse/blob/master/DeviceID.txt)  set the id of device, example "DeviceID:0" the device id equals with "0" integer(must be an integer), you modify this file if you want to connect more than one raspberry device at the same network.

  In order to load the libraries into Raspberry pi use the following commands (Command line) [link](https://github.com/tsoglani/SpeechRaspberrySmartHouse/blob/master/Command_Line.md)

  You can set the application to run on startup by following these instructions  [link](https://github.com/tsoglani/SpeechRaspberrySmartHouse/blob/master/RunOnStartup.md)

  In order to run the application you must create a .jar file named "SmartHouseServer.jar" and put it inside the folder named "SmartHouseServer" then place the folder on the desktop of Rasberry pi.
  
  You can create a .jar file from within Raspberry with BlueJ by using the application inside the SpeechRasberrySmartHouse/Rasberry_2b-3/ folder, named Rasberry_2a,b_InAndOut_40gpio_pin. You can also use NetBeans or IntelliJ IDEA to create the .jar file by following this link: SpeechRaspberrySmartHouse_IDEA_VERSION

if you want to use pca9685, download and run on raspberry side the [pi4j-1.2-SNAPSHOT.deb](https://github.com/tsoglani/SpeechRaspberrySmartHouse/blob/master/pi4j-1.2-SNAPSHOT.deb) file to istall the new libraries (old one might have some confligs with pca9685).

ENJOY AND THANK YOU FOR DOWNLOADING!
