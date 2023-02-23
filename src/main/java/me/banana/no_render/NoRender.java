package me.banana.no_render;

import com.llamalad7.mixinextras.MixinExtrasBootstrap;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(NoRender.MOD_ID)
public class NoRender {
    public static final String MOD_ID = "no_render";

    public NoRender() {
        MixinExtrasBootstrap.init();

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, NoRenderConfig.GENERAL_SPEC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(NoRender::onConfigReload);
    }

    public static void onConfigReload(ModConfigEvent modConfigEvent) {
        if (modConfigEvent.getConfig().getModId().equals(MOD_ID)) {
            NoRenderConfig.CONFIG.onConfigReload();
        }
    }
}
