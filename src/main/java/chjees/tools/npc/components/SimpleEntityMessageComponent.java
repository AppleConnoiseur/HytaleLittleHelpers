package chjees.tools.npc.components;

import chjees.tools.npc.data.SimpleEntityMessage;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.map.MapCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.server.core.entity.UUIDComponent;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
import java.util.Map;

/**
 * <p>Storage of messages sent from various sources.</p>
 */
public class SimpleEntityMessageComponent implements Component<EntityStore> {
    public final static MapCodec<SimpleEntityMessage, Map<String,SimpleEntityMessage>> MESSAGES_CODEC =
            new MapCodec<>(SimpleEntityMessage.CODEC, Object2ObjectOpenHashMap::new, false);

    private Map<String, SimpleEntityMessage> messages = new Object2ObjectOpenHashMap<>();

    public SimpleEntityMessageComponent() { }

    public SimpleEntityMessageComponent(SimpleEntityMessageComponent other) {
        this.messages.putAll(other.getMessages());
    }

    public static final BuilderCodec<SimpleEntityMessageComponent> CODEC =
            BuilderCodec.builder(SimpleEntityMessageComponent.class, SimpleEntityMessageComponent::new)
                    .append(new KeyedCodec<>("Messages", MESSAGES_CODEC),
                            (messagesComponent,
                             messagesMap) -> messagesComponent.messages = messagesMap,
                            messagesComponent -> messagesComponent.messages)
                    .add()
                    .build();

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @NullableDecl
    @Override
    public Component<EntityStore> clone() {
        return new SimpleEntityMessageComponent(this);
    }

    public Map<String, SimpleEntityMessage> getMessages() {
        return messages;
    }

    public SimpleEntityMessage getMessage(String messageKey)
    {
        return this.messages.getOrDefault(messageKey,null);
    }

    public void putMessage(String messageKey)
    {
        this.messages.put(messageKey, new SimpleEntityMessage(messageKey));
    }

    public void putMessage(String messageKey, UUIDComponent uuid)
    {
        this.messages.put(messageKey, new SimpleEntityMessage(messageKey, uuid));
    }

    public void removeMessage(String messageKey)
    {
        this.messages.remove(messageKey);
    }
}