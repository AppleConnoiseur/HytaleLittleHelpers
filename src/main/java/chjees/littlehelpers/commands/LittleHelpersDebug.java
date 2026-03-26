package chjees.littlehelpers.commands;

import chjees.littlehelpers.LittleHelpersPlugin;
import chjees.littlehelpers.npc.components.FairyComponent;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.matrix.Matrix4d;
import com.hypixel.hytale.protocol.DebugShape;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractTargetEntityCommand;
import com.hypixel.hytale.server.core.modules.debug.DebugUtils;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.joml.Vector3d;
import org.joml.Vector3f;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Debugging commands for developing this mod.
 */
public class LittleHelpersDebug extends AbstractTargetEntityCommand {
    public LittleHelpersDebug(String name, String description) {
        super(name, description);
    }

    @Override
    protected void execute(@NonNullDecl CommandContext commandContext, @NonNullDecl List<Ref<EntityStore>> objectList, @NonNullDecl World world, @NonNullDecl Store<EntityStore> store) {
        commandContext.sendMessage(Message.raw("Hello from LittleHelpersDebug."));
        if(!objectList.isEmpty())
        {
            Ref<EntityStore> firstObject = objectList.getFirst();
            FairyComponent fairyComp = store.getComponent(firstObject, LittleHelpersPlugin.Instance().getFairyComponent());
            if(fairyComp != null)
            {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Recruiter=").append(fairyComp.getPlayerRecruiter()).append('\n');
                stringBuilder.append("Need:Food=").append(fairyComp.getFoodNeed()).append('\n');
                stringBuilder.append("Need:Essence=").append(fairyComp.getEssenceNeed()).append('\n');
                stringBuilder.append("Need:Happiness=").append(fairyComp.getHappinessNeed()).append('\n');
                stringBuilder.append("Home=").append(fairyComp.getHomeCoordinatesAsPoint()).append('\n');
                commandContext.sendMessage(Message.raw(stringBuilder.toString()));

                if(fairyComp.isHomeCoordinatesSet())
                {
                    //Debug information
                    TransformComponent transformComponent = store.getComponent(firstObject, TransformComponent.getComponentType());
                    assert transformComponent != null;

                    Vector3d position = fairyComp.getHomeCoordinatesAsPoint();

                    //Home center
                    {
                        Vector3f color = new Vector3f(1f, 201 / 255f, 14 / 255f);
                        int flags = DebugUtils.FLAG_FADE;
                        DebugUtils.add(world, DebugShape.Cube, makeMatrix(position, 1.0), color, 10.0F, flags);
                    }

                    //Home area
                    {
                        Vector3f color = DebugUtils.COLOR_LIME;
                        int flags = DebugUtils.FLAG_FADE;
                        DebugUtils.add(world, DebugShape.Cube, makeMatrix(position, 60.0), color, 10.0F, flags);
                    }
                }
            }
        }
    }

    @Nonnull
    public static Matrix4d makeMatrix(@Nonnull Vector3d pos, double scale) {
        Matrix4d matrix = new Matrix4d();
        matrix.identity();
        matrix.translate(pos);
        matrix.scale(scale, scale, scale);
        return matrix;
    }
}
