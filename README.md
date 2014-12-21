Tesseract
=========
A 3D rendering of a 4D object called a tesseract using Java OpenGL.

Navigation
-----------
[Tesseract](#tesseract) |
[Purpose](#purpose) |
[Screenshot](#screenshot) |
[Team](#team) |
[Configuration](#configuration) | 
[JAR and natives Configuration](#jar and natives configuration) |
[Running the program](#running the program) |
[Liscence](#liscence) | 
[TODO](#todo) 

Purpose
-------
This project aims to create an animated 3D rendering of a 4D cube known as a tesseract. We are using Java's LWJGL library and OpenGL to make the animation, and Gradle/Groovy as a build tools to make the JAR, automate testing, and to link required resources and natives. So, we're kind of doing 4 things here. 

Screenshot
----------
This is what the tesseract should look like. 
![Picture](http://rabbitfighter.net/wp-content/uploads/2014/12/tesseract.jpg)


Team
----------------
<ul>
<li>@redragonx</li>
<li>@rabbitfighter81</li>
</ul>


Configuration
==============================
This program requires the lwjgl vers 3.0.0a JAR file as well as natives for Windows, Linux, and OSX. As per our Gradle build program, these files must be obtained and put into the correct folders in the project structure or you will be an unhappy camper, and the program will fail. So the following steps are necessary:

JAR and natives Configuration
-----------------------------
<ol>
<li>Obtain the LWJGL zip file from: http://www.lwjgl.org/download and extract the files somewhere.</li>
<li>Create a folder in the main directory of the project called <code>libs/</code></li>
<li>Move the native folder from wherever you extracted the lwjgl.zip folder and rename it to <code>natives</code>, and then move the whole folder into the <code>libs/</code> folder in the project.</li>
<li>Then copy the lwjgl.jar from the exttracted files and move it into the <code>libs</code> folder in the project. You must then rename it <code>lwjgl3.jar</code>.</li>
</ol>

Running the program
-------------------
<ol>
<li>If gradle is not installed, install it. Then from the project directory, run <code>gradle build</code>.</li>
<li>Run the program by typing <code>gradle runJar</code></li>
</ol>

The program should run now. Yay!

Liscence
---------
???

TODO
----
This is a work in progress... 




