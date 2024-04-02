package io.taranis.opencluster.server.http;

import java.util.function.Consumer;
import java.util.function.Predicate;

import io.vertx.ext.web.RoutingContext;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true, fluent = true)
public class VertxCallable {
	
	public VertxCallable(AppHttpMethod httpMethod, String url, Consumer<RoutingContext> fucntion, Predicate<RoutingContext> authHandler) {
		this.httpMethod = httpMethod;
		this.url = url;
		this.fucntion = fucntion;
		this.authHandler = authHandler;
	}
	
	protected AppHttpMethod httpMethod;
	
	protected String url;
	
	protected Consumer<RoutingContext> fucntion;
	
	protected Predicate<RoutingContext> authHandler;
}
