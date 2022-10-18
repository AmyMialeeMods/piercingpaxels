package amymialee.piercingpaxels.compat.techreborn;

import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import reborncore.common.powerSystem.RcEnergyTier;
import techreborn.items.tool.MiningLevel;

public class DrackTier {
    private ToolMaterial material = ToolMaterials.WOOD;
    private int maxCharge = 1;
    private int cost = 1;
    private float poweredSpeed = 1;
    private float unpoweredSpeed = 1;
    private RcEnergyTier tier = RcEnergyTier.LOW;

    public static DrackTier of() {
        return new DrackTier();
    }

    public static DrackTier of(ToolMaterial material, int maxCharge, RcEnergyTier tier, int cost, float poweredSpeed, float unpoweredSpeed) {
        return of()
                .setMaterial(material)
                .setMaxCharge(maxCharge)
                .setCost(cost)
                .setPoweredSpeed(poweredSpeed)
                .setUnpoweredSpeed(unpoweredSpeed)
                .setTier(tier);
    }

    public DrackTier setMaterial(ToolMaterial material) {
        this.material = material;
        return this;
    }

    public DrackTier setMaxCharge(int maxCharge) {
        this.maxCharge = maxCharge;
        return this;
    }

    public DrackTier setCost(int cost) {
        this.cost = cost;
        return this;
    }

    public DrackTier setPoweredSpeed(float poweredSpeed) {
        this.poweredSpeed = poweredSpeed;
        return this;
    }

    public DrackTier setUnpoweredSpeed(float unpoweredSpeed) {
        this.unpoweredSpeed = unpoweredSpeed;
        return this;
    }

    public DrackTier setTier(RcEnergyTier tier) {
        this.tier = tier;
        return this;
    }

    public ToolMaterial getMaterial() {
        return material;
    }

    public int getMaxCharge() {
        return maxCharge;
    }

    public int getCost() {
        return cost;
    }

    public float getPoweredSpeed() {
        return poweredSpeed;
    }

    public float getUnpoweredSpeed() {
        return unpoweredSpeed;
    }

    public RcEnergyTier getTier() {
        return tier;
    }
}