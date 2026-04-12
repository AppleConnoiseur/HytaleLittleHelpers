package chjees.littlehelpers.commands;

import chjees.littlehelpers.LittleHelpersPlugin;
import chjees.littlehelpers.npc.components.FairyComponent;
import chjees.tools.algorithm.ChunkTraverser;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.math.vector.Vector3dUtil;
import com.hypixel.hytale.protocol.DebugShape;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.*;
import com.hypixel.hytale.server.core.modules.block.components.ItemContainerBlock;
import com.hypixel.hytale.server.core.modules.debug.DebugUtils;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.chunk.BlockComponentChunk;
import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import it.unimi.dsi.fastutil.ints.IntObjectImmutablePair;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.joml.Matrix4d;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector3i;
import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * Debugging commands for developing this mod.
 */
public class LittleHelpersDebug extends AbstractCommandCollection {
    public LittleHelpersDebug(String name, String description) {
        super(name, description);
        this.addSubCommand(new Inspect("inspect", "commands.Little_Helpers.commands.debug.component.inspect.desc"));
        this.addSubCommand(new Home("home", "commands.Little_Helpers.commands.debug.component.home.desc"));
        this.addSubCommand(new Needs("needs", "commands.Little_Helpers.commands.debug.component.needs.desc"));
        this.addSubCommand(new TestInventory("test", "commands.Little_Helpers.commands.debug.component.test.desc"));
    }


    @Nonnull
    public static Matrix4d makeMatrix(@Nonnull Vector3d pos, double scale) {
        Matrix4d matrix = new Matrix4d();
        matrix.identity();
        matrix.translate(pos);
        matrix.scale(scale, scale, scale);
        return matrix;
    }

    private static class TestInventory extends AbstractPlayerCommand {

        public TestInventory(@NonNullDecl String name, @NonNullDecl String description) {
            super(name, description);
        }

        @Override
        protected void execute(
                @NonNullDecl CommandContext commandContext,
                @NonNullDecl Store<EntityStore> store,
                @NonNullDecl Ref<EntityStore> ref,
                @NonNullDecl PlayerRef playerRef,
                @NonNullDecl World world) {
            if(commandContext.isPlayer())
            {
                TransformComponent transform = store.getComponent(ref, TransformComponent.getComponentType());
                assert transform != null;
                Vector3i originPosition = Vector3dUtil.toVector3i(transform.getPosition());

                Store<ChunkStore> chunkStore = world.getChunkStore().getStore();

                //Block index + ItemContainerBlock
                ArrayList<ObjectObjectImmutablePair<Vector3i,ItemContainerBlock>> blockInventories = new ArrayList<>();

                //Check chunks for inventories.
                ChunkTraverser.generate(30, originPosition, chunk -> {
                    long chunkIndex = ChunkUtil.indexChunk(chunk.x, chunk.y);
                    HytaleLogger.getLogger().at(Level.INFO).log("Chunk: %d :: %d, %d", chunkIndex, chunk.x, chunk.y);
                    Ref<ChunkStore> chunkReference = chunkStore.getExternalData().getChunkReference(chunkIndex);
                    assert chunkReference != null;

                    BlockComponentChunk blockComponentChunk = chunkStore.getComponent(chunkReference, BlockComponentChunk.getComponentType());
                    assert blockComponentChunk != null;

                    var entityRefs = blockComponentChunk.getEntityReferences();

                    entityRefs.forEach((blockEntityIndex, _) -> {
                        ItemContainerBlock component = blockComponentChunk.getComponent(blockEntityIndex, ItemContainerBlock.getComponentType());
                        if(component != null)
                        {
                            //Add if its within distance.
                            int x1 = ChunkUtil.xFromIndex(blockEntityIndex) + (chunk.x * ChunkUtil.SIZE);
                            int y1 = ChunkUtil.yFromIndex(blockEntityIndex);
                            int z1 = ChunkUtil.zFromIndex(blockEntityIndex) + (chunk.y * ChunkUtil.SIZE);

                            HytaleLogger.getLogger().at(Level.INFO).log("ItemContainerBlock at: %d, %d, %d :: distance: %d", x1, y1, z1, Vector3d.distanceSquared(x1, y1, z1, originPosition.x, originPosition.y, originPosition.z));

                            if(Vector3d.distanceSquared(x1, y1, z1, originPosition.x, originPosition.y, originPosition.z) <= 30)
                                blockInventories.add(new ObjectObjectImmutablePair<>(new Vector3i(x1, y1, z1), component));
                        }
                    });
                    //We want to search all chunks, that is why we return false.
                    return false;
                });

                commandContext.sendMessage(Message.raw(String.format("Inventories: %s", blockInventories)));
            }
        }
    }

    /**
     * Inspects the fairies component information.
     */
    private static class Inspect extends AbstractTargetEntityCommand {
        public Inspect(@NonNullDecl String name, @NonNullDecl String description) {
            super(name, description);
        }

        protected void execute(@NonNullDecl CommandContext commandContext, @NonNullDecl List<Ref<EntityStore>> objectList, @NonNullDecl World world, @NonNullDecl Store<EntityStore> store) {
            if(!objectList.isEmpty())
            {
                Ref<EntityStore> firstObject = objectList.getFirst();
                FairyComponent fairyComp = store.getComponent(firstObject, LittleHelpersPlugin.Instance().getFairyComponent());
                if(fairyComp != null)
                {
                    String debugString = "Recruiter=" + fairyComp.getPlayerRecruiter() + '\n' +
                            "Need:Food=" + fairyComp.getFoodNeed() + '\n' +
                            "Need:Essence=" + fairyComp.getEssenceNeed() + '\n' +
                            "Need:Happiness=" + fairyComp.getHappinessNeed() + '\n' +
                            "Home=" + fairyComp.getHomeCoordinatesAsPoint() + '\n';
                    commandContext.sendMessage(Message.raw(debugString));
                } else {
                    commandContext.sendMessage(Message.raw("No valid fairy with component in sight."));
                }
            } else {
                commandContext.sendMessage(Message.raw("No valid fairy in sight."));
            }
        }
    }

    /**
     * Visualizes the fairies home area.
     */
    private static class Home extends AbstractTargetEntityCommand {
        public Home(@NonNullDecl String name, @NonNullDecl String description) {
            super(name, description);
        }

        protected void execute(@NonNullDecl CommandContext commandContext, @NonNullDecl List<Ref<EntityStore>> objectList, @NonNullDecl World world, @NonNullDecl Store<EntityStore> store) {
            if(!objectList.isEmpty())
            {
                Ref<EntityStore> firstObject = objectList.getFirst();
                FairyComponent fairyComp = store.getComponent(firstObject, LittleHelpersPlugin.Instance().getFairyComponent());
                if(fairyComp != null)
                {
                    if(fairyComp.isHomeCoordinatesSet())
                    {
                        //Debug information
                        commandContext.sendMessage(Message.raw("Visualizing default home area for fairy."));

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
                    } else {
                        commandContext.sendMessage(Message.raw("No home co-ordinates set for fairy."));
                    }
                } else {
                    commandContext.sendMessage(Message.raw("No valid fairy with component in sight."));
                }
            } else {
                commandContext.sendMessage(Message.raw("No valid fairy in sight."));
            }
        }
    }

    private static class Needs extends AbstractTargetEntityCommand {
        private final RequiredArg<String> needArg = this.withRequiredArg("need", "commands.Little_Helpers.commands.debug.component.needs.need.desc", ArgTypes.STRING);
        private final RequiredArg<String> operationArg = this.withRequiredArg("operation", "commands.Little_Helpers.commands.debug.component.needs.operation.desc", ArgTypes.STRING);

        public Needs(@NonNullDecl String name, @NonNullDecl String description) {
            super(name, description);
        }

        protected void execute(@NonNullDecl CommandContext commandContext, @NonNullDecl List<Ref<EntityStore>> objectList, @NonNullDecl World world, @NonNullDecl Store<EntityStore> store) {
            if(!objectList.isEmpty())
            {
                Ref<EntityStore> firstObject = objectList.getFirst();
                FairyComponent fairyComp = store.getComponent(firstObject, LittleHelpersPlugin.Instance().getFairyComponent());
                if(fairyComp != null)
                {
                    String needType = commandContext.get(needArg);
                    String operationType = commandContext.get(operationArg);

                    boolean allNeedsAlter = false;
                    boolean foodNeedAlter = false;
                    boolean essenceNeedAlter = false;
                    boolean happinessNeedAlter = false;

                    switch (needType) {
                        case "all" -> allNeedsAlter = true;
                        case "food" -> foodNeedAlter = true;
                        case "essence" -> essenceNeedAlter = true;
                        case "happiness" -> happinessNeedAlter = true;
                        default -> {
                            commandContext.sendMessage(Message.raw("Wrong first argument, valid arguments: all, food, essence, happiness"));
                            return;
                        }
                    }

                    double operationModifier;
                    switch (operationType) {
                        case "refill" -> operationModifier = 1.0d;
                        case "zero" -> operationModifier = 0.0d;
                        case "low" -> operationModifier = 0.33d;
                        default -> {
                            commandContext.sendMessage(Message.raw("Wrong second argument, valid arguments: refill, zero, low"));
                            return;
                        }
                    }

                    if(allNeedsAlter)
                    {
                        fairyComp.setFoodNeed(operationModifier);
                        fairyComp.setEssenceNeed(operationModifier);
                        fairyComp.setHappinessNeed(operationModifier);
                        commandContext.sendMessage(Message.raw(String.format("Set all needs to: %s", operationModifier)));
                    }
                    if(foodNeedAlter)
                    {
                        fairyComp.setFoodNeed(operationModifier);
                        commandContext.sendMessage(Message.raw(String.format("Set food need to: %s", operationModifier)));
                    }
                    if(essenceNeedAlter)
                    {
                        fairyComp.setEssenceNeed(operationModifier);
                        commandContext.sendMessage(Message.raw(String.format("Set essence need to: %s", operationModifier)));
                    }
                    if(happinessNeedAlter)
                    {
                        fairyComp.setHappinessNeed(operationModifier);
                        commandContext.sendMessage(Message.raw(String.format("Set happiness need to: %s", operationModifier)));
                    }
                } else {
                    commandContext.sendMessage(Message.raw("No valid fairy with component in sight."));
                }
            } else {
                commandContext.sendMessage(Message.raw("No valid fairy in sight."));
            }
        }
    }
}
