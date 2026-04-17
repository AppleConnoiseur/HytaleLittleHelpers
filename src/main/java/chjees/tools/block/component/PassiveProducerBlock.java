package chjees.tools.block.component;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.asset.type.item.config.Item;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.thread.TickingThread;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.Instant;

public class PassiveProducerBlock  implements Component<ChunkStore> {
    //Builder fields
    protected String produceItem;
    protected int produceAmount;
    protected int productionTime;

    //Worker fields
    protected float productionProgress;
    @Nullable
    private Instant lastTickGameTime;

    public static final BuilderCodec<PassiveProducerBlock> CODEC =BuilderCodec.builder(PassiveProducerBlock.class, PassiveProducerBlock::new)
            .appendInherited(new KeyedCodec<>("Item", Codec.STRING),
                    (state, s) -> state.produceItem = s,
                    state -> state.produceItem,
                    (o, p) -> o.produceItem = p.produceItem)
            .addValidator(Item.VALIDATOR_CACHE.getValidator())
            .add()
            .appendInherited(new KeyedCodec<>("Amount", Codec.INTEGER),
                    (state, s) -> state.produceAmount = s,
                    state -> state.produceAmount,
                    (o, p) -> o.produceAmount = p.produceAmount)
            .add()
            .appendInherited(new KeyedCodec<>("ProductionTime", Codec.INTEGER),
                    (state, s) -> state.productionTime = s,
                    state -> state.productionTime,
                    (o, p) -> o.productionTime = p.productionTime)
            .add()
            .append(new KeyedCodec<>("ProductionProgress", Codec.FLOAT),
                    (state, s) -> state.productionProgress = s,
                    state -> state.productionProgress)
            .add()
            .append(new KeyedCodec<>("LastTickGameTime", Codec.INSTANT),
                    (state, t) -> state.lastTickGameTime = t,
                    state -> state.lastTickGameTime)
            .add()
            .build();
    private PassiveProducerBlock() {
    }

    public PassiveProducerBlock(PassiveProducerBlock other) {
        this.produceItem = other.produceItem;
        this.produceAmount = other.produceAmount;
        this.productionTime = other.productionTime;
        this.productionProgress = other.productionProgress;
        this.lastTickGameTime = other.lastTickGameTime;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @NullableDecl
    @Override
    public Component<ChunkStore> clone() {
        return new PassiveProducerBlock(this);
    }

    public void advanceProcessing(
        float delta,
        @Nonnull Store<EntityStore> entityStore,
        int blockX,
        int blockY,
        int blockZ,
        @Nonnull BlockType blockType,
        int rotationIndex
    ) {
        //Progress time.
        //Math: Progress per tick = (1 second / Ticks Per Second) * Delta Time
        float progress = (1.0f / (float)TickingThread.TPS) * delta;
        productionProgress += progress;

        //Progress is finished.
        if(productionProgress > productionTime)
        {
            //Clamp progress value.
            productionProgress = productionTime;

            //Try to output an item stack to our `ItemContainerBlock` component.
            ChunkStore chunkStore = entityStore.getExternalData().getWorld().getChunkStore();
        }
    }
}
