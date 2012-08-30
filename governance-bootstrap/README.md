# Pubsub Bootstrap module

## Description

This module gets topics from a WSN service, creates all the required EventClouds according to the topic and subscribes to all the topics on behalf of the DSB. It can be exposed as a Web service (WAR packaging)

## Tests

> mvn jetty:run

Open [http://localhost:8080/play-platformservices-pubsubbootstrap/](http://localhost:8080/play-platformservices-pubsubbootstrap/)

## Use
One can call the BoostrapService with all the required parameters or call the InitService REST service directly. This last one will get the default platform configuration from a github repository and calls the BootstrapService with this data as input parameter -> One shot!

The global REST operation to initialize all is a GET at http://localhost:8080/play-platformservices-pubsubbootstrap/rest/init/
