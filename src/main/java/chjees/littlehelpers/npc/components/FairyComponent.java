package chjees.littlehelpers.npc.components;

import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.validation.Validators;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.util.UUID;

/**
 * Data container for all values relevant for maintaining recruited fairies.
 */
public class FairyComponent  implements Component<EntityStore> {
    /// The player which employs the fairy.
    private UUID playerRecruiter;
    /// The food need for the fairy. If it drops to 0 it is unhappy.
    private double foodNeed;
    /// The essence need for the fairy. If it drops to 0 it is unhappy.
    private double essenceNeed;
    /// The happiness need for the fairy. If it drops to 0 it will become wild again.
    private double happinessNeed;
    /// The home co-ordinates for the fairy. Will stick around there if set. (X, Y, Z)
    private int[] homeCoordinates;
    /// If true home co-ordinates are set.
    private boolean homeCoordinatesSet;

    public  FairyComponent()
    {
        //Defaults
        setPlayerRecruiter(null);
        setFoodNeed(0);
        setEssenceNeed(0);
        setHappinessNeed(0);
        setHomeCoordinates(0,0, 0);
        setHomeCoordinatesSet(false);
    }

    public  FairyComponent(FairyComponent other)
    {
        //Defaults
        setPlayerRecruiter(other.getPlayerRecruiter());
        setFoodNeed(other.getFoodNeed());
        setEssenceNeed(other.getEssenceNeed());
        setHappinessNeed(other.getHappinessNeed());
        setHomeCoordinates(other.getHomeCoordinates());
        setHomeCoordinatesSet(other.isHomeCoordinatesSet());
    }

    public static final BuilderCodec<FairyComponent> CODEC =
            BuilderCodec.builder(FairyComponent.class, FairyComponent::new).
            append(new KeyedCodec<>("PlayerRecruiter", BuilderCodec.UUID_BINARY),
                    FairyComponent::setPlayerRecruiter,
                    FairyComponent::getPlayerRecruiter)
            .addValidator(Validators.nonNull())
            .add()
            .append(new KeyedCodec<>("FoodNeed", BuilderCodec.DOUBLE),
                    FairyComponent::setFoodNeed,
                    FairyComponent::getFoodNeed)
            .add()
            .append(new KeyedCodec<>("EssenceNeed", BuilderCodec.DOUBLE),
                    FairyComponent::setEssenceNeed,
                    FairyComponent::getEssenceNeed)
            .add()
            .append(new KeyedCodec<>("HappinessNeed", BuilderCodec.DOUBLE),
                    FairyComponent::setHappinessNeed,
                    FairyComponent::getHappinessNeed)
            .add()
            .append(new KeyedCodec<>("HomeCoordinates", BuilderCodec.INT_ARRAY),
                    FairyComponent::setHomeCoordinates,
                    FairyComponent::getHomeCoordinates)
            .add()
            .append(new KeyedCodec<>("HomeCoordinatesSet", BuilderCodec.BOOLEAN),
                    FairyComponent::setHomeCoordinatesSet,
                    FairyComponent::isHomeCoordinatesSet)
            .add()
            .build();

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @NullableDecl
    @Override
    public Component<EntityStore> clone() {
        return new FairyComponent(this);
    }

    public UUID getPlayerRecruiter() {
        return playerRecruiter;
    }

    public void setPlayerRecruiter(UUID playerRecruiter) {
        this.playerRecruiter = playerRecruiter;
    }

    public double getFoodNeed() {
        return foodNeed;
    }

    public void setFoodNeed(double foodNeed) {
        this.foodNeed = foodNeed;

        //Can't be below zero,
        if(this.foodNeed < 0d)
            this.foodNeed = 0d;
    }

    public double getEssenceNeed() {
        return essenceNeed;
    }

    public void setEssenceNeed(double essenceNeed) {
        this.essenceNeed = essenceNeed;

        //Can't be below zero,
        if(this.essenceNeed < 0d)
            this.essenceNeed = 0d;
    }

    /// The food need for the fairy. If it drops to 0 it is unhappy.
    public double getHappinessNeed() {
        return happinessNeed;
    }

    public void setHappinessNeed(double happinessNeed) {
        this.happinessNeed = happinessNeed;

        //Can't be below zero,
        if(this.happinessNeed < 0d)
            this.happinessNeed = 0d;
    }

    public int[] getHomeCoordinates() {
        return homeCoordinates;
    }

    public Vector3d getHomeCoordinatesAsPoint()
    {
        int[] coords = getHomeCoordinates();
        return new Vector3d(coords[0], coords[1], coords[2]);
    }

    public void setHomeCoordinates(int[] homeCoordinates) {
        this.homeCoordinates = homeCoordinates;
    }

    public void setHomeCoordinates(int x, int y, int z) {
        this.homeCoordinates = new int[]{x, y, z};
    }

    /// If true then homeCoordinates is set.
    public boolean isHomeCoordinatesSet() {
        return homeCoordinatesSet;
    }

    public void setHomeCoordinatesSet(boolean homeCoordinatesSet) {
        this.homeCoordinatesSet = homeCoordinatesSet;
    }
}
