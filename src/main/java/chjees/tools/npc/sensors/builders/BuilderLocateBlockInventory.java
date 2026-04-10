package chjees.tools.npc.sensors.builders;

import chjees.tools.npc.sensors.LocateBlockInventory;
import com.google.gson.JsonElement;
import com.hypixel.hytale.server.npc.asset.builder.Builder;
import com.hypixel.hytale.server.npc.asset.builder.BuilderDescriptorState;
import com.hypixel.hytale.server.npc.asset.builder.BuilderSupport;
import com.hypixel.hytale.server.npc.asset.builder.Feature;
import com.hypixel.hytale.server.npc.asset.builder.holder.EnumHolder;
import com.hypixel.hytale.server.npc.asset.builder.holder.IntHolder;
import com.hypixel.hytale.server.npc.asset.builder.validators.IntSingleValidator;
import com.hypixel.hytale.server.npc.corecomponents.builders.BuilderSensorBase;
import com.hypixel.hytale.server.npc.instructions.Sensor;

/**
 * <p>Builder for {@link LocateBlockInventory}.</p>
 * <p>Locates a nearby inventory.</p>
 */
public class BuilderLocateBlockInventory extends BuilderSensorBase {
    private final IntHolder scanRange = new IntHolder();
    private final EnumHolder<LocateBlockInventory.InventoryStatus> filter = new EnumHolder<>();

    public BuilderLocateBlockInventory() {
    }

    @Override
    public String getShortDescription() {
        return "Short description for LocateBlockInventory.";
    }

    @Override
    public String getLongDescription() {
        return "Long description for LocateBlockInventory.";
    }

    @Override
    public Builder<Sensor> readConfig(JsonElement data) {
        this.getInt(
                data,
                "Range",
                this.getScanRange(),
                3,
                IntSingleValidator.greater0(),
                BuilderDescriptorState.Stable,
                "Maximum range of blocks to scan.",
                null);
        this.getEnum(
                data,
                "Filter",
                this.getFilter(),
                LocateBlockInventory.InventoryStatus.class,
                LocateBlockInventory.InventoryStatus.Any,
                BuilderDescriptorState.Stable,
                "Which block inventories to look for. Default is `Any`.",
                "");
        this.provideFeature(Feature.Position);
        return this;
    }

    @Override
    public Sensor build(BuilderSupport builderSupport) {
        return new LocateBlockInventory(this, builderSupport);
    }

    @Override
    public BuilderDescriptorState getBuilderDescriptorState() {
        return BuilderDescriptorState.Stable;
    }

    public IntHolder getScanRange() {
        return scanRange;
    }

    public EnumHolder<LocateBlockInventory.InventoryStatus> getFilter() {
        return filter;
    }
}