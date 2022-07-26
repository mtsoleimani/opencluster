package io.taranis.opencluster.messages.parser;

import java.util.Arrays;
import java.util.stream.Collectors;

import io.taranis.opencluster.exception.InvalidMessageException;
import io.taranis.opencluster.messages.AckMessage;
import io.taranis.opencluster.messages.DataMessage;
import io.taranis.opencluster.messages.HeartBeatMessage;
import io.taranis.opencluster.messages.LeaveMessage;
import io.taranis.opencluster.messages.Message;
import io.taranis.opencluster.messages.MessageType;
import io.taranis.opencluster.messages.Metadata;
import io.vertx.core.json.DecodeException;
import io.vertx.core.json.JsonObject;

public class JsonMessageParser implements Metadata {

	public static Message parse(String text) throws InvalidMessageException {

		try {
			return parse(new JsonObject(text));
		} catch (NullPointerException | DecodeException | InvalidMessageException e) {
			throw new InvalidMessageException();
		}
	}

	public static Message parse(JsonObject json) throws InvalidMessageException {

		try {

			final MessageType type = MessageType.valueOf(json.getInteger(TYPE));
			if (type == null)
				throw new InvalidMessageException();

			switch (type) {
			case HEARTBEAT:
				return parseHeartBeatMessage(json);

			case DATA:
				return parseDataMessage(json);
				
			case LEAVE:
				return parseLeaveMessage(json);

				
			case ACK:
				return parseAckMessage(json);
				
			default:
				return null;
			}

		} catch (InvalidMessageException e) {
			throw e;
		} catch (ClassCastException e) {
			throw new InvalidMessageException();
		}
	}

	private static HeartBeatMessage parseHeartBeatMessage(JsonObject json) {
		if(!json.containsKey(VALUE))
			return new HeartBeatMessage();
		
		String[] hosts = json.getString(VALUE).trim().split(",");
		if(hosts == null || hosts.length == 0)
			return new HeartBeatMessage();
		
		return new HeartBeatMessage(Arrays.stream(hosts).map(i -> i.toString()).collect(Collectors.toList()));
	}

	private static DataMessage parseDataMessage(JsonObject json) throws InvalidMessageException {
		if(!json.containsKey(KEY) && !json.containsKey(VALUE))
			throw new InvalidMessageException();
		
		return new DataMessage(json.getString(KEY), json.getString(VALUE));
	}
	
	private static LeaveMessage parseLeaveMessage(JsonObject json) throws InvalidMessageException {
		return new LeaveMessage();
	}

	private static AckMessage parseAckMessage(JsonObject json) throws InvalidMessageException {
		if(!json.containsKey(VALUE))
			throw new InvalidMessageException();
		
		return new AckMessage(json.getString(VALUE));
	}

}
