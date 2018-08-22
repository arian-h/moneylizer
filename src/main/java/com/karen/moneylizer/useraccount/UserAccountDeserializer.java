package com.karen.moneylizer.useraccount;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class UserAccountDeserializer extends JsonDeserializer<UserAccountEntity> {

	@Override
	public UserAccountEntity deserialize(JsonParser jp,
			DeserializationContext ctxt) throws IOException,
			JsonProcessingException {
		JsonNode node = jp.getCodec().readTree(jp);
		final String username = node.get("username").asText();
		final String password = node.get("password").asText();
		return new UserAccountEntity(username, password);
	}

}
