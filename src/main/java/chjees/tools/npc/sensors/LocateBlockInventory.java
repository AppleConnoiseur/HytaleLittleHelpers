package chjees.tools.npc.sensors;

import chjees.tools.algorithm.ChunkTraverser;
import chjees.tools.npc.sensors.builders.BuilderLocateBlockInventory;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.math.vector.Vector3dUtil;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.chunk.BlockComponentChunk;
import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.asset.builder.BuilderSupport;
import com.hypixel.hytale.server.npc.corecomponents.SensorBase;
import com.hypixel.hytale.server.npc.role.Role;
import com.hypixel.hytale.server.npc.sensorinfo.InfoProvider;

import javax.annotation.Nonnull;

import com.hypixel.hytale.server.npc.sensorinfo.PositionProvider;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.joml.Vector3i;

import java.util.function.Supplier;

/**
 * <p>Locates a nearby inventory.</p>
 */
public class LocateBlockInventory extends SensorBase {
    /// The maximum range radius in cells to find block inventories in.
    private final int scanRange;
    private final InventoryStatus inventoryFilter;
    /// Position provider for the sensor.
    private final PositionProvider positionProvider = new PositionProvider();

    public LocateBlockInventory(@Nonnull BuilderLocateBlockInventory builder, @Nonnull BuilderSupport builderSupport) {
        super(builder);

        scanRange = builder.getScanRange().get(builderSupport.getExecutionContext());
        inventoryFilter = builder.getFilter().get(builderSupport.getExecutionContext());
    }

    @Override
    public boolean matches(@NonNullDecl Ref<EntityStore> ref, @NonNullDecl Role role, double dt, @NonNullDecl Store<EntityStore> store) {
        //Ensure we will only try to match as long as our parent class matches.
        if (!super.matches(ref, role, dt, store))
            return false;

        //Relevant data to work with.
        TransformComponent transform = store.getComponent(ref, TransformComponent.getComponentType());
        assert transform != null;

        Vector3i originPosition = Vector3dUtil.toVector3i(transform.getPosition());

        World world = store.getExternalData().getWorld();
        Store<ChunkStore> chunkStore = world.getChunkStore().getStore();

        ChunkTraverser.generate(scanRange, originPosition, chunk -> {
            long chunkIndex = ChunkUtil.indexChunk(chunk.x, chunk.y);
            Ref<ChunkStore> chunkReference = chunkStore.getExternalData().getChunkReference(chunkIndex);
            assert chunkReference != null;

            WorldChunk worldChunk = chunkStore.getComponent(chunkReference, WorldChunk.getComponentType());
            assert worldChunk != null;

            BlockComponentChunk blockComponentChunk = worldChunk.getBlockComponentChunk();
            assert blockComponentChunk != null;

            return true;
        });

        return true;
    }

    // Note: Will only be used if you call `provideFeature` in `readConfig` in the BuilderLocateBlockInventory class.
    @Override
    public InfoProvider getSensorInfo() {
        return positionProvider;
    }

    /**
     * What status of inventories to look for. From anything to empty inventories.
     */
    public enum InventoryStatus  implements Supplier<String> {
        Any("Look for any block inventories."),
        SpaceLeft("Look for block inventories with any space left."),
        Empty("Look for block inventories that are empty.");

        private final String description;

        InventoryStatus(String description){this.description = description;}

        public String get() {return description;}
    }
}