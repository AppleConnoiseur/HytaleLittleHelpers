package chjees.tools.npc.actions;

import chjees.littlehelpers.LittleHelpersPlugin;
import chjees.tools.npc.actions.builders.BuilderClearMessage;
import chjees.tools.npc.components.SimpleEntityMessageComponent;
import chjees.tools.npc.data.SimpleEntityMessage;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.asset.builder.BuilderSupport;
import com.hypixel.hytale.server.npc.corecomponents.ActionBase;
import com.hypixel.hytale.server.npc.role.Role;
import com.hypixel.hytale.server.npc.sensorinfo.InfoProvider;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import javax.annotation.Nonnull;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * <p>Explicitly clear a message from the {@link SimpleEntityMessageComponent}.</p>
 */
public class ClearMessage extends ActionBase {
    private final String messageKey;
    public ClearMessage(@NonNullDecl BuilderClearMessage builder, BuilderSupport builderSupport) {
        super(builder);

        this.messageKey = builder.getMessageKey().get(builderSupport.getExecutionContext());
    }

    @Override
    public boolean canExecute(@Nonnull Ref<EntityStore> ref, @Nonnull Role role, InfoProvider sensorInfo, double dt, @Nonnull Store<EntityStore> store) {
        return super.canExecute(ref, role, sensorInfo, dt, store);
    }

    @Override
    public boolean execute(@NonNullDecl Ref<EntityStore> ref, @NonNullDecl Role role, InfoProvider sensorInfo, double dt, @NonNullDecl Store<EntityStore> store) {
        if(!super.execute(ref, role, sensorInfo, dt, store))
            return false;

        SimpleEntityMessageComponent messageComp = store.getComponent(ref, LittleHelpersPlugin.Instance().getMessagesComponent());
        //Fail if we have no component to work with.
        if(messageComp == null)
        {
            HytaleLogger.getLogger().at(Level.WARNING).atMostEvery(1, TimeUnit.MINUTES).log("SimpleEntityMessageSensor is trying to do a operation with a `null` SimpleEntityMessageComponent: %s", getBreadCrumbs());
            return false;
        }

        SimpleEntityMessage message = messageComp.getMessage(this.messageKey);
        if(message == null || !message.getMessage().equals(this.messageKey))
            return false;

        messageComp.removeMessage(this.messageKey);

        return true;
    }
}