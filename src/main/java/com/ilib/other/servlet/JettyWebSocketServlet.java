package com.ilib.other.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import com.ilib.other.websocket.JettyWebSocket;
@WebServlet("/jettyTest.ws")
public class JettyWebSocketServlet extends WebSocketServlet {

	@Override
	public void configure(WebSocketServletFactory factory) {
		// TODO Auto-generated method stub
		// register a socket class as default
        factory.register(JettyWebSocket.class);
	}

}
