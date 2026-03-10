package chjees.tools.npc.sensors;

import chjees.littlehelpers.LittleHelpersPlugin;
import chjees.tools.npc.components.SimpleEntityMessageComponent;
import chjees.tools.npc.data.SimpleEntityMessage;
import chjees.tools.npc.sensors.builders.BuilderSimpleEntityMessageSensor;
import com.hypixel.hytale.component.ComponentAccessor;import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.entity.UUIDComponent;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.asset.builder.BuilderSupport;
import com.hypixel.hytale.server.npc.corecomponents.SensorBase;
import com.hypixel.hytale.server.npc.entities.NPCEntity;import com.hypixel.hytale.server.npc.movement.controllers.MotionController;import com.hypixel.hytale.server.npc.role.Role;
import com.hypixel.hytale.server.npc.sensorinfo.EntityPositionProvider;
import com.hypixel.hytale.server.npc.sensorinfo.InfoProvider;
import javax.annotation.Nonnull;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class SimpleEntityMessageSensor extends SensorBase {
    private final String messageKey;
    private final boolean useEntity;
    private final boolean clearMessage;

    private final EntityPositionProvider entityProvider = new EntityPositionProvider();

    public SimpleEntityMessageSensor(@Nonnull BuilderSimpleEntityMessageSensor builder, @Nonnull BuilderSupport builderSupport) {
        super(builder);

        messageKey = builder.getMessageKey().get(builderSupport.getExecutionContext());
        useEntity = builder.getUseEntity().get(builderSupport.getExecutionContext());
        clearMessage = builder.getClearMessage().get(builderSupport.getExecutionContext());
    }

    @Override
    public boolean matches(@NonNullDecl Ref<EntityStore> ref, @NonNullDecl Role role, double dt, @NonNullDecl Store<EntityStore> store) {
        if(!super.matches(ref, role, dt, store))
        {
            entityProvider.clear();
            return false;
        }

        SimpleEntityMessageComponent messageComp = store.getComponent(ref, LittleHelpersPlugin.Instance().getMessagesComponent());
        //Fail if we have no component to work with.
        if(messageComp == null)
        {
            HytaleLogger.getLogger().at(Level.WARNING).atMostEvery(1, TimeUnit.MINUTES).log("SimpleEntityMessageSensor is trying to do a operation with a `null` SimpleEntityMessageComponent: %s", getBreadCrumbs());
            return false;
        }

        //Message handling
        SimpleEntityMessage message = messageComp.getMessage(this.messageKey);
        if(message == null)
        {
            entityProvider.clear();
            return false;
        }

        if(message.getMessage().equals(this.messageKey))
        {
            //See if we got an entity to deal with.
            if(this.useEntity)
            {
                //Set the target.
                Ref<EntityStore> otherRef = store.getExternalData().getRefFromUUID(message.getRefUUID());

                if(otherRef != null)
                {
                    UUIDComponent otherUUID = store.getComponent(otherRef, UUIDComponent.getComponentType());
                    if (otherUUID != null)
                    {
                        entityProvider.setTarget(otherRef, store);
                    }
                } else {
                    entityProvider.clear();
                    return false;
                }
            }

            //Remove message once done.
            if(this.clearMessage)
                messageComp.removeMessage(this.messageKey);
            return  true;
        }else {
            return  false;
        }
    }

    @Override
    public InfoProvider getSensorInfo() {
        if(this.useEntity)
            return entityProvider;

        return null;
    }

    @Override
    public void motionControllerChanged(@NullableDecl Ref<EntityStore> ref, @NonNullDecl NPCEntity npcComponent, MotionController motionController, @NullableDecl ComponentAccessor<EntityStore> componentAccessor) {
        super.motionControllerChanged(ref, npcComponent, motionController, componentAccessor);
    }
}