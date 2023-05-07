package amymialee.piercingpaxels.compat.techreborn;

import amymialee.piercingpaxels.items.PaxelItem;
import amymialee.piercingpaxels.registry.PiercingItems;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import reborncore.common.powerSystem.RcEnergyTier;
import reborncore.common.util.WorldUtils;
import techreborn.blocks.misc.BlockRubberLog;
import techreborn.config.TechRebornConfig;
import techreborn.events.TRRecipeHandler;
import techreborn.init.ModSounds;
import techreborn.init.TRContent;
import techreborn.init.TRToolMaterials;
import techreborn.init.TRToolTier;

@SuppressWarnings("unused")
public class TechRebornPaxels {
    public static final Item BRONZE_PAXEL = PiercingItems.registerItem("tr_bronze_paxel", new PaxelItem(TRToolTier.BRONZE, 5.0f, -2.8f, new FabricItemSettings().rarity(Rarity.UNCOMMON)));
    public static final Item RUBY_PAXEL = PiercingItems.registerItem("tr_ruby_paxel", new PaxelItem(TRToolTier.RUBY, 5.0f, -2.8f, new FabricItemSettings().rarity(Rarity.UNCOMMON)));
    public static final Item SAPPHIRE_PAXEL = PiercingItems.registerItem("tr_sapphire_paxel", new PaxelItem(TRToolTier.SAPPHIRE, 5.0f, -2.8f, new FabricItemSettings().rarity(Rarity.RARE)));
    public static final Item PERIDOT_PAXEL = PiercingItems.registerItem("tr_peridot_paxel", new PaxelItem(TRToolTier.PERIDOT, 5.0f, -2.8f, new FabricItemSettings().rarity(Rarity.UNCOMMON)));

    public static final DrackTier BASIC = DrackTier.of(TRToolMaterials.BASIC_DRILL, TechRebornConfig.basicDrillCharge, RcEnergyTier.MEDIUM, TechRebornConfig.basicDrillCost, 6F, -.5F);
    public static final DrackTier ADVANCED = DrackTier.of(TRToolMaterials.ADVANCED_DRILL, TechRebornConfig.advancedDrillCharge, RcEnergyTier.EXTREME, TechRebornConfig.advancedDrillCost, 12F, 0.2F);
    public static final DrackTier INDUSTRIAL = DrackTier.of(TRToolMaterials.INDUSTRIAL_DRILL, TechRebornConfig.industrialDrillCharge, RcEnergyTier.INSANE, TechRebornConfig.industrialDrillCost, 20.0F, 1.0F);

    public static final Item BASIC_DRACK = PiercingItems.registerItem("tr_basic_drack", new DrackItem(BASIC, 5.0f, -2.8f, new FabricItemSettings().rarity(Rarity.UNCOMMON).maxCount(1).maxDamage(-1)));
    public static final Item ADVANCED_DRACK = PiercingItems.registerItem("tr_advanced_drack", new DrackItem(ADVANCED, 5.0f, -2.8f, new FabricItemSettings().rarity(Rarity.RARE).maxCount(1).maxDamage(-1)));
    public static final Item INDUSTRIAL_DRACK = PiercingItems.registerItem("tr_industrial_drack", new DrackItem(INDUSTRIAL, 6.0f, -2.8f, new FabricItemSettings().rarity(Rarity.EPIC).maxCount(1).maxDamage(-1)));

    public static final Item ADVANCED_UPGRADE_KIT = PiercingItems.registerItem("tr_advanced_upgrade_kit", new Item(new FabricItemSettings().rarity(Rarity.RARE)));
    public static final Item INDUSTRIAL_UPGRADE_KIT = PiercingItems.registerItem("tr_industrial_upgrade_kit", new Item(new FabricItemSettings().rarity(Rarity.EPIC)));

    public static final Item ADVANCED_DRACK_UPGRADE_KIT = PiercingItems.registerItem("tr_advanced_drack_upgrade_kit", new Item(new FabricItemSettings().rarity(Rarity.RARE)));
    public static final Item INDUSTRIAL_DRACK_UPGRADE_KIT = PiercingItems.registerItem("tr_industrial_drack_upgrade_kit", new Item(new FabricItemSettings().rarity(Rarity.EPIC)));

    public static final Item USAGE_TREETAP = PiercingItems.registerItem("tr_usage_treetap", new Item(new FabricItemSettings().maxCount(1).rarity(Rarity.UNCOMMON)));

    public static void init() {
        ItemGroupEvents.modifyEntriesEvent(PiercingItems.PAXELS_GROUP).register(entries -> entries.add(BRONZE_PAXEL));
        ItemGroupEvents.modifyEntriesEvent(PiercingItems.PAXELS_GROUP).register(entries -> entries.add(RUBY_PAXEL));
        ItemGroupEvents.modifyEntriesEvent(PiercingItems.PAXELS_GROUP).register(entries -> entries.add(SAPPHIRE_PAXEL));
        ItemGroupEvents.modifyEntriesEvent(PiercingItems.PAXELS_GROUP).register(entries -> entries.add(PERIDOT_PAXEL));

        ItemGroupEvents.modifyEntriesEvent(PiercingItems.PAXELS_GROUP).register(entries -> entries.add(BASIC_DRACK));
        ItemGroupEvents.modifyEntriesEvent(PiercingItems.PAXELS_GROUP).register(entries -> entries.add(ADVANCED_DRACK));
        ItemGroupEvents.modifyEntriesEvent(PiercingItems.PAXELS_GROUP).register(entries -> entries.add(INDUSTRIAL_DRACK));

        ItemGroupEvents.modifyEntriesEvent(PiercingItems.PAXELS_GROUP).register(entries -> entries.add(ADVANCED_UPGRADE_KIT));
        ItemGroupEvents.modifyEntriesEvent(PiercingItems.PAXELS_GROUP).register(entries -> entries.add(INDUSTRIAL_UPGRADE_KIT));

        ItemGroupEvents.modifyEntriesEvent(PiercingItems.PAXELS_GROUP).register(entries -> entries.add(ADVANCED_DRACK_UPGRADE_KIT));
        ItemGroupEvents.modifyEntriesEvent(PiercingItems.PAXELS_GROUP).register(entries -> entries.add(INDUSTRIAL_DRACK_UPGRADE_KIT));

        ItemGroupEvents.modifyEntriesEvent(PiercingItems.PAXELS_GROUP).register(entries -> entries.add(USAGE_TREETAP));
    }

    public static void paxelUsages(ItemStack upgradeUsage, ItemUsageContext context) {
        if (upgradeUsage.isOf(USAGE_TREETAP)) {
            World world = context.getWorld();
            BlockPos blockPos = context.getBlockPos();
            PlayerEntity player = context.getPlayer();
            BlockState state = world.getBlockState(blockPos);
            if (player != null && state.contains(BlockRubberLog.HAS_SAP) && state.get(BlockRubberLog.HAS_SAP) && state.contains(BlockRubberLog.SAP_SIDE) && state.get(BlockRubberLog.SAP_SIDE) == context.getSide()) {
                world.setBlockState(blockPos, state.with(BlockRubberLog.HAS_SAP, false).with(BlockRubberLog.SAP_SIDE, Direction.fromHorizontal(0)));
                world.playSound(player, blockPos, ModSounds.SAP_EXTRACT, SoundCategory.BLOCKS, 0.6F, 1F);
                if (!player.getInventory().insertStack(TRContent.Parts.SAP.getStack())) {
                    WorldUtils.dropItem(TRContent.Parts.SAP.getStack(), world, blockPos.offset(context.getSide()));
                }
                if (player instanceof ServerPlayerEntity serverPlayer && !TechRebornConfig.vanillaUnlockRecipes) {
                    TRRecipeHandler.unlockTRRecipes(serverPlayer);
                }
            }
        }
    }
}
