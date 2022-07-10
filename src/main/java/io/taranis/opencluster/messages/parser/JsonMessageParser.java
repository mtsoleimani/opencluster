package io.taranis.opencluster.messages.parser;


import io.taranis.opencluster.Message;
import io.taranis.opencluster.MetadataMessage;
import io.taranis.opencluster.exception.InvalidMessageException;
import io.vertx.core.json.DecodeException;
import io.vertx.core.json.JsonObject;

public class JsonMessageParser implements MetadataMessage {

	public static Message parse(String text) throws InvalidMessageException {
		
		try {
			return parse(new JsonObject(text));
		} catch (NullPointerException | DecodeException | InvalidMessageException e) {
			throw new InvalidMessageException();
		}
	}

	public static Message parse(JsonObject json) throws InvalidMessageException {

		try {
			return null;
		} catch (ClassCastException e) {
			throw new InvalidMessageException();
		}
	}

}
