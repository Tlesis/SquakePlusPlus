# Squake++
Squake++ is client-side Fabric mod for Minecraft which adds bunnyhopping, strafe jumping, air control, trimping, and sharking, as well as a spedometer. It also can disable the loss of speed when taking fall damage (needs to be installed server-side). All options are configurable with an ingame config GUI.

The modified movement code is based on Quake and Half-Life's code, with the the goal to make the movement feel the same as Half-Life. Because of this, trimping and sharking, which are based off of Fortress Forever's movement (not Half-Life), are disabled by default. These are only included as they were features in the [original Squake](https://www.curseforge.com/minecraft/mc-mods/squake) by squeek502.

## Features
By default, the config GUI can be opened with `O+C`

* **Bunnyhopping** - Uncapped by default, but you can apply soft and hard speed caps.
* **Trimping** - The trimp mechanic is based off of the technique in Fortress Forever. If you're moving fast enough, pressing sneak will convert some of your horizontal speed into vertical speed. The multiplier for how much of your momentum is converted is configurable. Trimping is disabled by default.
* **Sharking** - Sharking refers to a mechanic also from Fortress Forever, which allows for gliding across the surface of water by holding jump. The surface tension and friction of the water are configurable, which affect how much momentum you lose when hitting the water, and how fast you move on the water. Sharking is also disabled by default.

## Compiling
1. Clone the repository with `git clone https://github.com/Tlesis/SquakePlusPlus.git`
2. Navigate into the repository's directory 
3. Run `./gradlew build`

The built jar file will be in `build/libs/`

Credits
-------
Original Squake: https://www.curseforge.com/minecraft/mc-mods/squake <br>
Squake ported to Fabric: https://github.com/He11crow/SquakeFabric
