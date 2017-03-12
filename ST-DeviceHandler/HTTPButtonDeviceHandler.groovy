/**
 *  HTTP Button
 *  Category: Device Handler
 *  Copyright 2017 James VanBennekom
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 *  Fix addChildDevice problem - https://community.smartthings.com/t/use-addchilddevice-with-manual-ip-entry/4594/23
 */

import groovy.json.JsonSlurper
	preferences {
		input("DeviceIP", "string", title:"Device IP Address", description: "Please enter your device's IP Address", required: true, displayDuringSetup: true)
		input("DevicePort", "string", title:"Device Port",  defaultValue: "80", required: false, displayDuringSetup: true)
		input("DevicePathOn", "string", title:"URL Path for ON",  defaultValue: "/?pin=ON", displayDuringSetup: true)
		input("DevicePathOff", "string", title:"URL Path for OFF", defaultValue: "/?pin=OFF", displayDuringSetup: true)
		input("DevicePostGet", "string", title: "POST or GET", defaultValue: "GET", displayDuringSetup: true)
		section() {
			input("HTTPAuth", "bool", title:"Requires User Auth?", description: "Choose if the HTTP requires basic authentication", defaultValue: false, required: true, displayDuringSetup: true)
			input("HTTPUser", "string", title:"HTTP User", description: "Enter your basic username", required: false, displayDuringSetup: true)
			input("HTTPPassword", "string", title:"HTTP Password", description: "Enter your basic password", required: false, displayDuringSetup: true)
		}
}
metadata {
	definition (name: "HTTP Button", namespace: "NodeMCU", author: "JamesV") {
        capability "Switch"
		attribute "triggerswitch", "string"
		command "DeviceTrigger"
	}

	// simulator metadata
	simulator {
	}

	// UI tile definitions
	tiles {
		standardTile("button", "device.switch", width: 2, height: 2, canChangeIcon: true) {
			state "off", label: 'Off', action: "switch.on", icon: "st.switches.switch.off", backgroundColor: "#ffffff", nextState: "on"
				state "on", label: 'On', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#79b821", nextState: "off"
		}
		standardTile("offButton", "device.button", width: 1, height: 1, canChangeIcon: true) {
			state "default", label: 'Force Off', action: "switch.off", icon: "st.switches.switch.off", backgroundColor: "#ffffff"
		}
		standardTile("onButton", "device.switch", width: 1, height: 1, canChangeIcon: true) {
			state "default", label: 'Force On', action: "switch.on", icon: "st.switches.switch.on", backgroundColor: "#79b821"
		}
		main "button"
			details (["button","onButton","offButton"])
	}
}
def parse(String description) {
	log.debug(description)
}
def internal_method = internal_method ? internal_method : "GET"
def on() {
	log.debug "---ON COMMAND--- ${DevicePathOn}"
    sendEvent(name: "triggerswitch", value: "triggeron", isStateChange: true)
    sendEvent(name: "switch", value: "on") 
    	runCmd(DevicePathOn)   
}
def off() {
	log.debug "---OFF COMMAND--- ${DevicePathOff}"
    sendEvent(name: "triggerswitch", value: "triggeroff", isStateChange: true)
    sendEvent(name: "switch", value: "off")
    runCmd(DevicePathOff)
}
def runCmd(String varCommand) {
	def host = DeviceIP
	def LocalDevicePort = ''
	if (DevicePort==null) { LocalDevicePort = "80" } else { LocalDevicePort = DevicePort }
	def userpassascii = "${HTTPUser}:${HTTPPassword}"
	def userpass = "Basic " + userpassascii.encodeAsBase64().toString()
	log.debug "The device id configured is: $device.deviceNetworkId"
	def path = varCommand
	log.debug "path is: $path"
	def body = "" 
	def headers = [:] 
	headers.put("HOST", "$host:$LocalDevicePort")
	headers.put("Content-Type", "application/x-www-form-urlencoded")
	if (HTTPAuth) {
		headers.put("Authorization", userpass)
	}
	log.debug "The Header is $headers"
	def method = "POST"
	try {
		if (DevicePostGet.toUpperCase() == "GET") {
			method = "GET"
			}
		}
	catch (Exception e) {
		settings.DevicePostGet = "POST"
		log.debug e
		log.debug "You must not have set the preference for the DevicePOSTGET option"
	}
	log.debug "The method is $method"
	try {
		def hubAction = new physicalgraph.device.HubAction(
			method: method,
			path: path,
			body: body,
			headers: headers
			)
		log.debug hubAction
		return hubAction
	}
	catch (Exception e) {
		log.debug "Hit Exception $e on $hubAction"
	}
    }




