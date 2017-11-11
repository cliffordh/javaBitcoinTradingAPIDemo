package com.bitso.example;

import java.io.IOException;
import java.net.URI;
import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

@ClientEndpoint
public class WSClient {
    private final String uri="wss://ws.bitso.com";
    private Session session;

    private Controller appController;

    public WSClient(Controller appController) {
        this.appController=appController;
    }

    public void connect() {

        try{
            WebSocketContainer container=ContainerProvider.
                    getWebSocketContainer();
            container.connectToServer(this, new URI(uri));
            this.sendMessage("");

        }catch(Exception ex){
            System.out.println(ex);

        }

    }

    @OnOpen
    public void onOpen(Session session){
      System.out.println("Session opened");
       this.session=session;
        this.sendMessage("{\"action\":\"subscribe\", \"book\":\"btc_mxn\",\"type\":\"diff-orders\"}");
    }

    @OnMessage
    public void onMessage(String message, Session session){
        System.out.println(message);

        // determine type of message and dispatch to controller if a "diff-order"
        if(message.contains("diff-orders") && !message.contains("subscribe")) {
            appController.diffOrderReceived(message);
        }
    }

    public void sendMessage(String message){
        try {
            session.getBasicRemote().sendText(message);
            System.out.println(message);
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
}

