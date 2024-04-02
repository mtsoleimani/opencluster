package io.taranis.opencluster.common.configs;


public class Metadata {
	
	private Metadata() {
		
	}

	public static final String LOCALHOST = "127.0.0.1";
	public static final String ALL_HOSTS = "0.0.0.0";


	public static final String TLS = "tls";
	public static final String PEM_CERT = "pem_cert";
	public static final String PEM_KEY = "pem_key";

	public static final String ROUTE = "route";

	public static final String TCP_OPTIONS = "tcp_options";
	public static final String BACKLOG = "backlog";
	public static final String TX_BUFFER = "tx_buffer";
	public static final String RX_BUFFER = "rx_buffer";
	public static final String FAST_OPEN = "fast_open";
	public static final String NO_DELAY = "no_delay";
	public static final String QUICK_ACK = "quick_ack";

	
	public static final String HTTP_HOST = "http_host";
	public static final String HTTP_PORT = "http_port";
	public static final String HTTP_TLS = "http_tls";
	public static final String HTTP_PEM_CERT = "http_pem_cert";
	public static final String HTTP_PEM_KEY = "http_pem_key";
	
	public static final String AUTH_TOKEN = "auth_token";
	

}
