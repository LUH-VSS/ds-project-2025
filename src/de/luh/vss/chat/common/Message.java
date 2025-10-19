package de.luh.vss.chat.common;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;

import de.luh.vss.chat.common.User.UserIdentifier;

public abstract class Message {

	private static final String VERSION = "1.1";

	public static class ServiceRegistrationRequest extends Message {

		private final UserIdentifier id;
		private final InetAddress address;
		private final int port;

		public ServiceRegistrationRequest(final UserIdentifier id, final InetAddress address, final int port) {
			this.id = id;
			this.address = address;
			this.port = port;
		}

		public ServiceRegistrationRequest(final DataInputStream in) throws IOException {
			this.id = new UserIdentifier(in.readInt());
			this.address = InetAddress.getByName(in.readUTF());
			this.port = in.readInt();
		}

		@Override
		public MessageType getMessageType() {
			return MessageType.SERVICE_REGISTRATION_REQUEST;
		}

		@Override
		public void toStream(final DataOutputStream out) throws IOException {
			out.writeInt(MessageType.SERVICE_REGISTRATION_REQUEST.msgType());
			out.writeInt(id.id());
			out.writeUTF(address.getCanonicalHostName());
			out.writeInt(port);
		}

		public UserIdentifier getUserIdentifier() {
			return id;
		}

		@Override
		public String toString() {
			return "SERVICE_REGISTRATION_REQUEST (" + id + ", " + address.getCanonicalHostName() + ":" + port + ")";
		}

	}

	public static class ServiceRegistrationResponse extends Message {

		public ServiceRegistrationResponse() {

		}

		public ServiceRegistrationResponse(final DataInputStream in) {

		}

		@Override
		public MessageType getMessageType() {
			return MessageType.SERVICE_REGISTRATION_RESPONSE;
		}

		@Override
		public void toStream(final DataOutputStream out) throws IOException {
			out.writeInt(MessageType.SERVICE_REGISTRATION_RESPONSE.msgType());
		}

		@Override
		public String toString() {
			return "SERVICE_REGISTRATION_RESPONSE ()";
		}

	}

	public static class ServiceErrorResponse extends Message {

		private final String errorMsg;

		public ServiceErrorResponse(final Exception e) {
			this.errorMsg = e.getMessage();
		}

		public ServiceErrorResponse(final DataInputStream in) throws IOException {
			errorMsg = in.readUTF();
		}

		public ServiceErrorResponse(final String e) {
			this.errorMsg = e;
		}

		public String getErrorMessage() {
			return errorMsg;
		}

		@Override
		public void toStream(final DataOutputStream out) throws IOException {
			out.writeInt(MessageType.SERVICE_ERROR_RESPONSE.msgType());
			out.writeUTF(errorMsg);
		}

		@Override
		public MessageType getMessageType() {
			return MessageType.SERVICE_ERROR_RESPONSE;
		}

		@Override
		public String toString() {
			return "SERVICE_ERROR_RESPONSE (" + errorMsg + ")";
		}

	}

	public static class ChatMessagePayload extends Message {

		private final UserIdentifier recipient;
		private final String msg;

		public ChatMessagePayload(final UserIdentifier recipient, final String msg) {
			this.recipient = recipient;
			this.msg = msg;
		}

		public ChatMessagePayload(final DataInputStream in) throws IOException {
			this.recipient = new UserIdentifier(in.readInt());
			this.msg = in.readUTF();
		}

		@Override
		public void toStream(final DataOutputStream out) throws IOException {
			out.writeInt(MessageType.CHAT_MESSAGE_PAYLOAD.msgType());
			out.writeInt(recipient.id());
			out.writeUTF(msg);
		}

		@Override
		public MessageType getMessageType() {
			return MessageType.CHAT_MESSAGE_PAYLOAD;
		}

		public UserIdentifier getRecipient() {
			return recipient;
		}

		public String getMessage() {
			return msg;
		}

		@Override
		public String toString() {
			return "CHAT_MESSAGE_PAYLOAD (to " + recipient + ": '" + msg + "')";
		}
	}

	public static Message parse(final DataInputStream in) throws IOException, ReflectiveOperationException {
		return MessageType.fromInt(in.readInt(), in);
	}

	public abstract void toStream(final DataOutputStream out) throws IOException;

	public abstract MessageType getMessageType();
}
