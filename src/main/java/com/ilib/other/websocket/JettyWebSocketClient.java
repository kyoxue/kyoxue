package com.ilib.other.websocket;

import java.net.URI;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import com.ilib.utils.HttpUtil;

public class JettyWebSocketClient {


	public static void main(String[] args) {
		String destUri = "ws://127.0.0.1:8080/ilib/jettyTest.action";  
        if (args.length > 0) {  
            destUri = args[0];  
        }  
        WebSocketClient client = new WebSocketClient();  
        JettyWebSocket socket = new JettyWebSocket();  
        try {  
            client.start();  
            URI echoUri = new URI(destUri);  
            ClientUpgradeRequest request = new ClientUpgradeRequest();  
            client.connect(socket, echoUri, request);  
            System.out.printf("Connecting to : %s%n", echoUri);  
            socket.awaitClose(5, TimeUnit.SECONDS);  
        } catch (Throwable t) {  
            t.printStackTrace();  
        } finally {  
            try {  
                client.stop();  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  

	}

}
