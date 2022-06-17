package com.example.demows;

import org.glassfish.tyrus.server.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.websocket.DeploymentException;

import javax.websocket.server.ServerEndpointConfig;
import java.io.IOException;
import java.net.*;
import java.util.Scanner;

@SpringBootApplication
public class DemoWsApplication {

    public static void main(String[] args) throws DeploymentException, URISyntaxException, IOException {
        Server ser = new Server("localhost", 8087, "/ws", ServerEndpoint.class);
        ser.start();
        ServerEndpointConfig.Builder configBuilder = ServerEndpointConfig.Builder.create(ServerEndpoint.class, "/ws");
        configBuilder.configurator(new ServerEndpointConfig.Configurator());
        configBuilder.build();

        System.out.println("Press any key to stop the server..");
        new Scanner(System.in).nextLine();
        SpringApplication.run(DemoWsApplication.class, args);
    }
}
