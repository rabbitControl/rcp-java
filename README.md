# rcp-java

rcp model and transporter for tcp, udp and websocket

use it with:

maven:
```
<dependency>
  <groupId>cc.rabbitcontrol</groupId>
  <artifactId>rcp</artifactId>
  <version>0.3.23</version>
</dependency>
```

gradle:
```
implementation 'cc.rabbitcontrol:rcp:0.3.23'
```

## rcp-example-repository
see: https://github.com/rabbitControl/rcp-java-example

## example:
``` java
RCPServer rabbitServer = new RCPServer();

final WebsocketServerTransporterNetty transporter = new WebsocketServerTransporterNetty();

rabbitServer.addTransporter(transporter);

transporter.bind(10000);

// expose parameter
Int32Parameter parameter = rabbitServer.createInt32Parameter("Int 32");

// update server (push to clients)
rabbitServer.update();
```
