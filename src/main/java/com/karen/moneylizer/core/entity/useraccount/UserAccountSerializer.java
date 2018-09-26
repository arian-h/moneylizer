package com.karen.moneylizer.core.entity.useraccount;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class UserAccountSerializer extends StdSerializer<UserAccountEntity> {

	private static final long serialVersionUID = 6260059370136673787L;

	public UserAccountSerializer() {
		this(null);
	}

	protected UserAccountSerializer(Class<UserAccountEntity> t) {
		super(t);
	}

	@Override
	public void serialize(UserAccountEntity value, JsonGenerator gen,
			SerializerProvider provider) throws IOException {
		gen.writeStartObject();
		gen.writeStringField("id", value.getId());
		gen.writeStringField("username", value.getUsername());
		gen.writeEndObject();
	}

}
