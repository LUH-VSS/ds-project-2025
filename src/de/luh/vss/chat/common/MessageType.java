package de.luh.vss.chat.common;

import java.io.DataInputStream;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public enum MessageType {
	SERVICE_ERROR_RESPONSE(0, Message.ServiceErrorResponse.class), SERVICE_REGISTRATION_REQUEST(1, Message.ServiceRegistrationRequest.class),
	SERVICE_REGISTRATION_RESPONSE(2, Message.ServiceRegistrationResponse.class), CHAT_MESSAGE_PAYLOAD(4, Message.ChatMessagePayload.class);

	private final int msgType;

	private Constructor<? extends Message> constr;

	private static final Map<Integer, MessageType> lookup = new HashMap<Integer, MessageType>();
	static {
		for (final MessageType mt : MessageType.values()) {
			lookup.put(mt.msgType, mt);
		}
	}

	private MessageType(int msgType, final Class<? extends Message> cls) {
		this.msgType = msgType;
		try {
			this.constr = cls.getConstructor(DataInputStream.class);
		} catch (NoSuchMethodException | SecurityException e) {
			System.err.println("Error while registering message type. "
					+ "Constructor from DataInputStream not present or accessible");
			e.printStackTrace();
			System.exit(-1);
		}
	}

	int msgType() {
		return msgType;
	}

	public static MessageType fromInt(final int val) {
		return lookup.get(val);
	}

	public static Message fromInt(final int val, final DataInputStream in) throws ReflectiveOperationException {
		final MessageType mt = fromInt(val);
		if (mt == null) {
			throw new IllegalStateException("Unknown message type " + val);
		}
		return mt.constr.newInstance(in);
	}

}