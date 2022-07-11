package io.taranis.opencluster.client;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.WebSocket;

public class WebSocketClient extends AbstractVerticle {

	private final String host;
	
	private final int port;
	
	private HttpClient client;
	
	private WebSocket webSocket;
	
	private final ConnectionListener connectionListener;
	
	public WebSocketClient(Vertx vertx, String host, int port, ConnectionListener connectionListener) {
		this.vertx = vertx;
		this.host = host;
		this.port = port;
		this.connectionListener = connectionListener;
	}
	
	
	@Override
    public void start() {
        startClient();
    }
	
	
	private void startClient() {
        HttpClient client = vertx.createHttpClient();

        client.webSocket(port, host, "/", ctx ->  {
			
        	if(ctx.failed()) {
        		connectionListener.onFailure(ctx.cause(), host, port);
        	} else {
        		webSocket = ctx.result();
        		connectionListener.onConnected(host, port);
        	}
		});
        
    }
	
	public void writeTextMessage(String text) throws Exception {
		webSocket.writeTextMessage(text);
	}
	
	
	public void writeTextMessage(byte[] bytes) throws Exception {
		webSocket.writeBinaryMessage(Buffer.buffer(bytes));
	}
	
	
	 public void stop() throws Exception {
		 client.close();
	 }
}
