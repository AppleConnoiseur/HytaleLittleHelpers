package chjees.tools.npc.data;

import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.server.core.entity.UUIDComponent;
import java.util.UUID;

/**
 * Represents a stored message that hasn't been handled by the NPC yet.
 */
public class SimpleEntityMessage {
    public static final BuilderCodec<SimpleEntityMessage> CODEC = BuilderCodec.builder(SimpleEntityMessage.class, SimpleEntityMessage::new)
            .append(new KeyedCodec<>("Message", BuilderCodec.STRING),
                    SimpleEntityMessage::setMessage,
                    SimpleEntityMessage::getMessage)
            .add()
            .append(new KeyedCodec<>("Reference", BuilderCodec.UUID_BINARY),
                    SimpleEntityMessage::setRefUUID,
                    SimpleEntityMessage::getRefUUID)
            .add()
            .build();
    /// Message for the NPC.
    private String message;
    /// (Optional) Entity reference from the caller.
    private UUID refUUID;

    public SimpleEntityMessage()
    {
        this.setMessage("");
        this.setRefUUID(null);
    }

    public SimpleEntityMessage(String message)
    {
        this.setMessage(message);
        this.setRefUUID(null);
    }

    public SimpleEntityMessage(String message, UUID refUUID)
    {
        this.setMessage(message);
        this.setRefUUID(refUUID);
    }

    public SimpleEntityMessage(String message, UUIDComponent refUUIDComponent)
    {
        this.setMessage(message);
        this.setRefUUID(refUUIDComponent.getUuid());
    }

    /// Message for the NPC.
    public String getMessage() {
        return message;
    }

    protected void setMessage(String message) {
        this.message = message;
    }

    public UUID getRefUUID() {
        return refUUID;
    }

    protected void setRefUUID(UUID refUUID) {
        this.refUUID = refUUID;
    }
}
