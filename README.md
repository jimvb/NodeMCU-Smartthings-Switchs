SmartThings Controlled Switch using a NodeMCU ESP8266

This will allow you to control the outputs of a NodeMCU ESP8266 0-8 from a virtual switch inside SmartThings.

Contains:
•	A SmartThings Device Handler for the virtual switch that sends a HTTP GET to the ESP8266
•	A SmartThings SmartApp that creates the virtual switch and assigns the Device Handler
•	Lua code for the NodeMCU device
•	Firmware for the NodeMCU device (newer firmware has an issue with HTTP)

Materials
•	A NodeMCU development board
•	A basic breadboard
•	Some extra wires of various male/female combinations.
•	A microUSB power supply
•	Something to turn on or off (LEDs, Relays)

Steps:
•	Flash the firmware onto the NodeMCU device
o	I personally use NodeMCU-PyFlasher for Windows

•	Edit the Lua File and save
o	Add your Wi-Fi name, password,   Static IP, Subnet, and Gateway

•	Load the Lua file onto your NodeMCU device
o	I use ESPlorer

•	Open SmartThings on your desktop and add in the SmartApp and Device Handler

•	In the SmartThings app, add SmartThings App
o	Select HTTP Button Maker
o	Type in a Label for the button, choose your hub, and assign a name
o	Click Done. You now have created the virtual switch.

•	Go to the Virtual switch you just created.
o	go to the configuration screen
o	Type in the IP address you assigned in the LUA code
o	Device port defaults at 80
o	Part of the url path for on and off are filled in already for you, you just have to add the pin number to the end. exp(/?pin=On1)
o	Should always use the GET method which is already filled in.
o	If you setup a user and password in the LUA file, enter them also
  
That’s it, enjoy
