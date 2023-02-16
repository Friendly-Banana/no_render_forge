package me.banana.no_render;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;

public class NoRenderConfig {
    public static NoRenderConfig CONFIG;
    public static final ForgeConfigSpec GENERAL_SPEC;

    public static final ArrayList<Class<? extends Entity>> hiddenTypes = new ArrayList<>();
    public final ForgeConfigSpec.BooleanValue hideAllEntities;
    public final ForgeConfigSpec.BooleanValue hideItems;
    public final ForgeConfigSpec.BooleanValue hidePlayer;

    NoRenderConfig(ForgeConfigSpec.Builder builder) {
        builder.push("Entities");
        hideAllEntities = builder.comment(" hide all entities").define("entities", false);
        hideItems = builder.comment(" hide items").define("items", true);
        hidePlayer = builder.comment(" hide player").define("player", false);
        builder.pop();
    }

    public static void updateHiddenTypes() {
        hiddenTypes.clear();
        if (NoRenderConfig.CONFIG.hideItems.get()) {
            hiddenTypes.add(ItemEntity.class);
        }
        if (NoRenderConfig.CONFIG.hidePlayer.get()) {
            hiddenTypes.add(Player.class);
        }
    }

    static {
        Pair<NoRenderConfig, ForgeConfigSpec> pair = new ForgeConfigSpec.Builder().configure(NoRenderConfig::new);
        CONFIG = pair.getLeft();
        GENERAL_SPEC = pair.getRight();
    }
}
