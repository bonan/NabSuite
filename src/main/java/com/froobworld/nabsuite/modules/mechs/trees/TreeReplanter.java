package com.froobworld.nabsuite.modules.mechs.trees;

import com.froobworld.nabsuite.modules.mechs.MechsModule;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class TreeReplanter implements Listener {
    private final MechsModule mechsModule;
    private final TreeManager treeManager;

    public TreeReplanter(MechsModule mechsModule, TreeManager treeManager) {
        this.mechsModule = mechsModule;
        this.treeManager = treeManager;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    private void onBlockBreak(BlockBreakEvent event) {
        if (Tag.LOGS.isTagged(event.getBlock().getType())) {
            if (treeManager.replantEnabled(event.getPlayer()) && treeManager.isNaturalLog(event.getBlock().getLocation())) {
                Material saplingMaterial = saplingTypeForWood(event.getBlock().getType());
                if (saplingMaterial != null) {
                    schedulePlantTask(event.getBlock().getLocation(), saplingMaterial);
                }
            }
        }
    }

    private void schedulePlantTask(Location location, Material saplingMaterial) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(mechsModule.getPlugin(), () -> {
            if (canPlant(location)) {
                location.getBlock().setType(saplingMaterial);
            }
        }, 20);
    }

    private boolean canPlant(Location location) {
        Block block = location.getBlock();
        Block blockBelow = block.getRelative(0, -1, 0);
        return block.isEmpty() && Tag.DIRT.isTagged(blockBelow.getType());
    }

    private Material saplingTypeForWood(Material woodType) {
        if (Tag.OAK_LOGS.isTagged(woodType)) {
            return Material.OAK_SAPLING;
        } else if (Tag.BIRCH_LOGS.isTagged(woodType)) {
            return Material.BIRCH_SAPLING;
        } else if (Tag.SPRUCE_LOGS.isTagged(woodType)) {
            return Material.SPRUCE_SAPLING;
        } else if (Tag.DARK_OAK_LOGS.isTagged(woodType)) {
            return Material.DARK_OAK_SAPLING;
        } else if (Tag.ACACIA_LOGS.isTagged(woodType)) {
            return Material.ACACIA_SAPLING;
        } else if (Tag.JUNGLE_LOGS.isTagged(woodType)) {
            return Material.JUNGLE_SAPLING;
        } else if (Tag.CHERRY_LOGS.isTagged(woodType)) {
            return Material.CHERRY_SAPLING;
        } else if (Tag.PALE_OAK_LOGS.isTagged(woodType)) {
            return Material.PALE_OAK_SAPLING;
        }
        return null;
    }

}
