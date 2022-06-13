package com.example.websocketservice1;

import com.alibaba.fastjson.JSONObject;
import com.example.websocketservice1.model.Message;
import org.glassfish.tyrus.client.ClientManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.websocket.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


@SpringBootApplication
public class WebsocketService1Application  {

    public static void main(String[] args) throws URISyntaxException, IOException, DeploymentException {

        String mess = "";

        ClientEndpointConfig.Builder configBuilder = ClientEndpointConfig.Builder.create();

        configBuilder.configurator(new ClientEndpointConfig.Configurator() {
            public void beforeRequest(Map<String, List<String>> headers) {
                headers.put("Connection", Arrays.asList("Upgrade"));
            }
        });
        ClientEndpointConfig clientConfig = configBuilder.build();
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();

        Session session = container.connectToServer(ClientEndpoint.class, clientConfig, new URI("ws://localhost:8080/websocket/ws/chat"));


        // connect to server
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Tiny Chat!");
        System.out.println("What's your name?");
        String user = scanner.nextLine();
        // repeatedly read a message and send it to the server (until quit)
        do {
            System.out.println("What's message?");
            mess = scanner.nextLine();
            session.getAsyncRemote().sendText(JSONObject.toJSONString(new Message(user, mess)));
        } while (!mess.equalsIgnoreCase("quit"));
        SpringApplication.run(WebsocketService1Application.class, args);

    }
}
