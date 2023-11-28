package com.example.passpringrest.codecs;

import com.example.passpringrest.entities.AbstractAccount;
import com.example.passpringrest.entities.AdminAccount;
import com.example.passpringrest.entities.ClientAccount;
import com.example.passpringrest.entities.ResourceManagerAccount;
import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;



public class AccountCodec implements Codec<AbstractAccount> {
    private final CodecRegistry registry;
    private final Codec<MongoUUID> uuidCodec;

    public AccountCodec(CodecRegistry registry) {
        this.registry = registry;
        this.uuidCodec = registry.get(MongoUUID.class);
    }

    @Override
    public AbstractAccount decode(BsonReader reader, DecoderContext decoderContext) {
        AbstractAccount abstractAccount = null;
        Document document = new Document();

        reader.readStartDocument();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            String fieldName = reader.readName();
            if (fieldName.equals("_id")) {
                MongoUUID mongoUUID = uuidCodec.decode(reader, decoderContext);
                document.put("_id", mongoUUID);
            } else {
                document.put(fieldName, readValue(reader, decoderContext, fieldName));
            }
        }
        reader.readEndDocument();

        String clazz = document.getString("_clazz");
        if (clazz != null) {
            if (clazz.equals("AdminAccount")) {
                abstractAccount = new AdminAccount();
//TODO admin account props
            } else if (clazz.equals("ResourceManagerAccount")) {
                abstractAccount = new ResourceManagerAccount();
//TODO resource manager account props
            } else if (clazz.equals("ClientAccount")) {
                abstractAccount = new ClientAccount();
            } else {
                throw new UnsupportedOperationException("Unsupported client type: " + clazz);
            }
        }

        abstractAccount.setLogin(document.getString("login"));
        abstractAccount.setPassword(document.getString("password"));
        abstractAccount.setPersonalId(document.getString("personalId"));
        abstractAccount.setActive(document.getBoolean("active"));
        abstractAccount.setId(document.get("_id", MongoUUID.class));

        return abstractAccount;
    }

    @Override
    public void encode(BsonWriter writer, AbstractAccount value, EncoderContext encoderContext) {
        writer.writeStartDocument();
        writer.writeString("_clazz", value.getClass().getSimpleName()); // Add the _clazz field
        writer.writeString("login", value.getLogin());
        writer.writeString("password", value.getPassword());
        writer.writeString("personalId", value.getPersonalId());
        writer.writeBoolean("active", value.getActive());


        if (value instanceof AdminAccount) {
            //TODO add admin account props
//            writer.writeDouble("discount", (value).getDiscount());
//            writer.writeInt32("membershipLevel", (value).getMembershipLevel());
        } else if (value instanceof ResourceManagerAccount) {
            //TODO add resource manager account props
//            writer.writeDouble("discount", (value).getDiscount());
        }


        writer.writeName("_id");
        uuidCodec.encode(writer, value.getId(), encoderContext);

        writer.writeEndDocument();
    }

    @Override
    public Class<AbstractAccount> getEncoderClass() {
        return AbstractAccount.class;
    }

    private void writeValue(BsonWriter writer, EncoderContext encoderContext, Object value) {
        if (value instanceof String) {
            writer.writeString((String) value);
        } else if (value instanceof Integer) {
            writer.writeInt32((int) value);
        } else if (value instanceof Double) {
            writer.writeDouble((double) value);
        }
    }

    private Object readValue(BsonReader reader, DecoderContext decoderContext, String fieldName) {
        BsonType type = reader.getCurrentBsonType();
        switch (type) {
            case STRING:
                return reader.readString();
            case INT32:
                return reader.readInt32();
            case DOUBLE:
                return reader.readDouble();
            case BOOLEAN:
                return reader.readBoolean();
            default:
                throw new UnsupportedOperationException("Unsupported BSON type for field: " + fieldName);
        }
    }
}
