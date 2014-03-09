#PlayVertX

## Playframework 2.2.x / vert.x 2.1RC1 integration
=========


# Foreword about vert.x

Vert.x is an amazing polyglot event driven framework that runs on the JVM. A kind of mix of the actor and reactor patterns.
It's a quite similar programing model as Node.js with some interesting features for clustering and threads pools for workers… I recommand you to take a look by yourself [http://vertx.io](http://vertx.io).

Vert.x has an embeddable library that gives you in version > 2 access to all the plateform's fonctionality. So you can now deploy Verticle and Modules programaticly. 
You can now take advantage of the polyglot nature of vert.x in an embeded environement.

You can also access directly the core library in java and access many of the underlying APIs :

* the netty http server
* the SockJs implementation
* Hazelcast (used for SharedData and cluster management)
* **the Event Bus**

# Cluster Management

Vert.x uses Hazelcast as it's default ClusterManager implementation. But vert.x doesn't expose hazelcast… (so sad)

**So this second version of the playVertX plugin now provides it's own ClusterManagerFactory based on the "famous, incredible and wonderful" [PlayHazelcast plugin](https://github.com/fmasion/playHazelcast) ;-) **

This gives you full control on the underlying Hazelcast instance : 

* play app can now register listener on machines joining and living the cluster
* you have full access to the distributed Maps, Lists, MultiMaps, Queues, Sets, Locks, Topics…
* Topics can be used for clusterWide pubSub
* You can acces and write directly in vert.x's shared Data
* All this primitives are observable => you can register listeners on add, update, remove ops
* Want more ? take a look to ExecutorService in the Hazelcast documentation


As a second benefits you can use the embeded vert.x **with or without cluster management** (useful in dev mode if you dont want to join your coworkers cluster…)


# The event bus

The event bus is application wide (from the machines in the cluster to each browsers). This bus enables machines in the cluster to send and receive messages based on the pub/sub pattern.
But what amazing is that browsers can also suscribe to channels and/or publish messages these channels.

Each client can register as many channel as it wants (channel is only a String identifier).

In this second version you can deploy the event bus as a simple Verticle in the PlatformManager (see sample App)

###This bus relies on [SockJS](https://github.com/sockjs/sockjs-client) 
This makes older browser seamlessly fallback from websocket to a websocket emulation.

Take a look at the [vert.x documentation](http://vertx.io/core_manual_java.html#event-bus-api)

# This Play Plugin

This plugin is an integration of vert.x core library in Play Framework 2.2.x to expose the vert.x event bus to play.
It enables play app to integrate a cluster.
Publish and subscribe to messages on the cluster.
And it enables to use all the amazing goodness of vert.x like sockJs or the event bus in the browser. 


###What does it mean ? 

Imagine you have N machines in your cluster. 2 users are connected with sockJS each to a different server…
 
They both are sharing the same channel (aka a chat room / a game table…) 

When one user publish a message on the channel the other one will receive it in real time.

Ok, want to give it a try ? 

# How to install it

In your application, add this configuration to the `project/Build.scala` file :

add this dependency for play 2.2.x :

	"playvertx"  % "playvertx_2.10" % "2.1RC1"


if you want cluster support add also :

	"playhazelcast"  % "playhazelcast_2.10" % "2.6.7"



add this resolver :

	resolvers += Resolver.url("Fred's GitHub Play Repository", url("http://fmasion.github.com/releases/"))(Resolver.ivyStylePatterns)

In your application, add to `conf/play.plugins` (or create the file if it dosn't exist) this configuration :

	1500:playvertx.VertxPlugin

if you want cluster support add also **with an higher priority** :

	500:playHazelcast.api.HazelcastPlugin

The first number is a priority you can adjust it if you have other plugins which depend on the one

Finally in the `conf/application.conf` you can configure some elements of vert.x  and for clustering. The cleanest way is to add this conf in additional files so add in `conf/application.conf` these lines :
	
	include "playvertx.conf"
	include "hazelcast.conf"

Create `conf/playvertx.conf` you can configure some elements of vert.x :
	
	playVertX {
	
		# isClustered true if needs hazelcast clusterManager => false will disable clustered eventBus en sharedData
		# Hazelcast has to be provided using playHazelcast plugin an the plugin must be defined
		# in /conf/play.plugin with an higher priority (smaller number)
		# even if you provide an hazelcast instance it won't be used if set to false
		isClustered=true
	
		# thread pool size is by default nb of cpu cores
		threadPoolSize=3


	}

**As you may notice you can now fix the thread pool size you give to our embeded vertx instance**

Create `conf/hazelcast.conf` you can configure some elements of Hazelcast that will overide cluster.xml :

	
	hz.port=5705

	hz.groupname="demo"
	hz.grouppassword="demo-pass"


	# the hazelcast conf file
	# entry in hazelcast.conf file will OVERRIDE params in this file (port, adresses, groupname...) 
	# hz.configfile="conf/config.xml"
	
	# No licenceKey is required for community edition
	# hz.licenceKey="XXXXXXXXX"

	# for hazelcastClient you configure a list of seeds (some of the member to contact if present)
	# the first seed that respond enables the connection
	# by default addMembershipListener will keep members up to date 
	# so connection to the cluster won't go down if the connected member fails 
	hz.addresses = ["127.0.0.1:5705", "127.0.0.1:5701"]

  
# Try the sample app

In the project directory you have a sample app check it out :

*NB : this is a port of the vert.x event bus sample app*

**This app demonstrate how to deploy Verticle to the PlatformManager programaticly from a play App. The choosen exemple is the EventBus**

you can launch it open diferent browser on the same url : 

* you first have to connect
* register to one or more channels
* publish messages to these channels
* there's a special channel called **someaddress**. Play sends periodic messages to this channel and listen to it too. all messages sent to this channel are sent to the play console


As usual it's provided without warranty. So try it, fork it, improve it, create issues, feel free to ask question but don't expect me to be very available.

Have Fun !

# License

© F.Masion

This project is published under the Apache License v2.0.

You may obtain a copy of the License at [http://www.apache.org/licenses/LICENSE-2.0] (http://www.apache.org/licenses/LICENSE-2.0).