PlayVertX Demo Application
=====================================

In order to launch this sample app go to the app root folder

Open 1 term

In the first one :
	
	> play run

open your browser on [http://localhost:9000](http://localhost:9000)

First click on the connect button

Register to "someaddress" => you will receive the notifications sent by play's akka timer (also visible in the console)


	
Open a second term in the same directory : 

	> play run 9001

go to your browser at the following URL :

	
[http://localhost:9001](http://localhost:9001)

You will see the same result **but now you are clustered**

Register an other address in one browser
send a message to this address in the other one

you will receive a notification => That's Magic 

in fact each browser is connected to it's own server instance but both instance are clustered through the eventbus

	BROWSER 1 <--vertexbus.js--> SERVER 1 <--Hazelcast--> SERVER 2 <--vertexbus.js--> BROWSER 2

That's all folk's
Have FUN !
