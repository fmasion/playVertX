#PlayVertX

### Playframework 2.1 vertX's EventBus integration
=========


# Forword about vertx.io

Vert.x is an amazing polyglot event driven framework based on the JVM. A kind of mix of the actor and reactor patterns.
It's a quite similar programing model as Node.js with some interesting feature for clustering and thread pools for workers… I recommand you to take a look by yourself [http://vertx.io](http://vertx.io).

Vert.x has a core library available as a single jar file (vertx-core.jar). This core library doesn't provide you the hole framework (verticles deployment, polygot api…) but let you access many of the underlying APIs :

* the netty http server
* the SockJs implementation
* Hazelcast (used for SharedData and cluster management)
* #####the Event Bus

## The event bus

The event bus is system wide (from the machines in the cluster to each browsers). This bus enables machines in the cluster to send an receive messages based on the pub/sub pattern.
But what amazing is that browsers can also suscribe to channels or publish messages to channels.

Each client can register as many channel as it wants.

####This bus relies on SockJS 

take a look at the [vert.x documentation](http://vertx.io/core_manual_java.html#event-bus-api)

## This Play Plugin

This plugin is a `quick and dirty` integration of vert.x core library in Play Framework > 2.1 to expose the vert.x event bus to play.
It enables play app to integrate a cluster.
Publish and subscribe to messages on the cluster.
And it enables to use the event bus in the browser. 

As usual it is provided without warranty. So try it, fork it, improve it, create issues, feel free to ask question but don't expect me to be very available.

####What does it mean ? 

Imagine you have N machines in your cluster. 2 users are connected with sockJS each to a different server…
 
They both are sharing the same channel (aka a chat room / a game table…) 

When one user publish a message on the channel the other one will receive it in real time.

Ok, want to give it a try ? 

## How to install it

In your application, add this configuration to the `project/Build.scala` file :

add this dependency :

`"playvertx"  % "playvertx_2.10" % "0.1"`

add this resolver :

`resolvers += Resolver.url("Fred's GitHub Play Repository", url("http://fmasion.github.com/releases/"))(Resolver.ivyStylePatterns)`

  
## Try the sample app

In the project directory you have a sample app check it out

## License

This project is published under the Apache License v2.0.

You may obtain a copy of the License at [http://www.apache.org/licenses/LICENSE-2.0] (http://www.apache.org/licenses/LICENSE-2.0).