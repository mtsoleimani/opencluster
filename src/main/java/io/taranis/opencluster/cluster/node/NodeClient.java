package io.taranis.opencluster.cluster.node;

import java.util.List;

import io.taranis.opencluster.messages.Message;
import io.taranis.opencluster.server.transport.Transport;

public interface NodeClient extends Transport {
	
	public void connect(String host, int port) throws Exception;
	
	public void leave() throws Exception;
	
	public void disconnect() throws Exception;
	
	public void heartbeat(List<String> hosts) throws Exception;
	
	public void setListener(NodeListener listener);
	
	public void onMessage(Message message, Transport transport);
	
}
