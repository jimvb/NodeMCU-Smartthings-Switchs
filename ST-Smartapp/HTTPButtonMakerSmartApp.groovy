/**
 *  HTTP Button Maker
 *  Category: Smart App
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
 *  Credit: Eric Roberts (baldeagle072) - Virtual switch creator
 *  Credit: tguerena and surge919 - URI Switch
 *  Credit: Soon Chye
 *  Fix addChildDevice problem - https://community.smartthings.com/t/use-addchilddevice-with-manual-ip-entry/4594/23
 */
definition(
    name: "HTTP Button Maker",
    namespace: "jimvb",
    author: "JamesV",
    description: "Creates HTTP button",
    iconUrl: "https://github.com/chancsc/icon/raw/master/standard-tile%401x.png",
    iconX2Url: "https://github.com/chancsc/icon/raw/master/standard-tile@2x.png",
    iconX3Url: "https://github.com/chancsc/icon/raw/master/standard-tile@3x.png")


preferences {
	section("Create HTTP Button") {
		input "switchLabel", "text", title: "Button Label", required: true
	}
    section("on this hub...") {
        input "theHub", "hub", multiple: false, required: true
    }
}
def installed() {
	log.debug "Installed with settings: ${settings}"
	initialize()
}
def updated() {
	log.debug "Updated with settings: ${settings}"
	unsubscribe()
	initialize()
}
def initialize() {
    def deviceId = app.id
    log.debug(deviceId)
    def existing = getChildDevice(deviceId)
    if (!existing) {
        def childDevice = addChildDevice("jimvb", "HTTP Button", deviceId, theHub.id, [label: switchLabel])
    }
}