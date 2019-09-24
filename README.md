# rcp-java

rcp model and transporter for tcp, udp and websocket

use it with:

maven:
```
<dependency>
  <groupId>io.github.rabbitcontrol</groupId>
  <artifactId>rcp</artifactId>
  <version>0.1.0</version>
</dependency>
```

gradle:
```
implementation 'io.github.rabbitcontrol:rcp:0.1.0'
```


example:
``` java
RCPServer rabbitServer = new RCPServer();

final WebsocketServerTransporterNetty transporter = new WebsocketServerTransporterNetty();

rabbitServer.addTransporter(transporter);

transporter.bind(10000);

// expose parameter
Int32Parameter floatParameter = rabbitServer.createInt32Parameter("Int 32");

// update server (push to clients)
rabbitServer.update();
```
