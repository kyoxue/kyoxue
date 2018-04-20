package com.ilib.other.websocket;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.StatusCode;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

@WebSocket(maxTextMessageSize = 64 * 1024,maxIdleTime = 30 * 60 * 1000)
public class JettyWebSocket {
	private static ThreadLocal<Integer> onlineCount = new ThreadLocal<>();
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
    private static CopyOnWriteArraySet<JettyWebSocket> webSocketSet = new CopyOnWriteArraySet<JettyWebSocket>();
    private final CountDownLatch closeLatch;
    private Session session;
 
    public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public JettyWebSocket() {
        this.closeLatch = new CountDownLatch(1);
    }
 
    public boolean awaitClose(int duration, TimeUnit unit) throws InterruptedException {
        return this.closeLatch.await(duration, unit);
    }
 
    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
    	System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
        System.out.printf("Connection closed: %d - %s%n", statusCode, reason);
        webSocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1    
        setSession(null);
        this.closeLatch.countDown();
    }
 
    @OnWebSocketConnect
    public void onConnect(Session session) {
    	setSession(session);
        webSocketSet.add(this);     //加入set中
        addOnlineCount();           //在线数加1
        System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());
        System.out.printf("Got connect: %s%n", session);
        try {
            Future<Void> fut;
            fut = session.getRemote().sendStringByFuture("Hello");
            fut.get(2, TimeUnit.SECONDS);
            fut = session.getRemote().sendStringByFuture("Thanks for the conversation.");
            fut.get(2, TimeUnit.SECONDS);
            //session.close(StatusCode.NORMAL, "I'm done");
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
 
    @OnWebSocketMessage
    public void onMessage(String msg) {
        System.out.printf("Got msg: %s%n", msg);
        System.out.println("来自客户端的消息:" + msg);
      //群发消息
        for(JettyWebSocket item: webSocketSet){             
        	boolean open = item.getSession().isOpen();
        	System.out.println(open);
        	if (open) {
        		try {
					item.sendMessage(msg);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else{
				webSocketSet.remove(item);
			}
        }
    }
    @OnWebSocketError
    public void onError(Session session, Throwable error){
    	System.out.println("发生错误");
        error.printStackTrace();
    }
    public void sendMessage(String message) throws IOException{
    	getSession().getRemote().sendString(message);
    }
    public static synchronized int getOnlineCount() {
        return onlineCount.get().intValue();
    }
 
    public static synchronized void addOnlineCount() {
    	Integer currentCount = onlineCount.get();
    	currentCount = (currentCount == null?new Integer(0):currentCount);
    	int currentCountInt = currentCount.intValue();
    	currentCountInt = currentCountInt+1;
    	onlineCount.set(new Integer(currentCountInt));
        //MyWebSocket.onlineCount++;
    }
     
    public static synchronized void subOnlineCount() {
    	Integer currentCount = onlineCount.get();
    	currentCount = (currentCount == null?new Integer(0):currentCount);
    	int currentCountInt = currentCount.intValue();
    	if (currentCountInt>0) {
    		currentCountInt = currentCountInt-1;
    		onlineCount.set(new Integer(currentCountInt));
		}
        //MyWebSocket.onlineCount--;
    }
}
