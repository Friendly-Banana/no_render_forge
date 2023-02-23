package me.banana.no_render.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import me.banana.no_render.NoRenderConfig;
import net.minecraft.client.Camera;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.lighting.LevelLightEngine;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Collections;
import java.util.Set;
import java.util.stream.StreamSupport;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {
    @Shadow
    @Final
    private static Logger LOGGER;

    @WrapWithCondition(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/lighting/LevelLightEngine;runUpdates(IZZ)I"))
    private boolean noRender$skipLightUpdates(LevelLightEngine lightEngine, int i, boolean skylight, boolean skipEdgeLightPropagation_) {
        return !NoRenderConfig.CONFIG.skipLightUpdates.get();
    }

    @ModifyExpressionValue(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;entitiesForRendering()Ljava/lang/Iterable;"))
    private Iterable<Entity> noRender$hideEntities(Iterable<Entity> entitiesForRendering) {
        if (NoRenderConfig.CONFIG.hideAllEntities.get()) {
            return Collections.emptyList();
        }
        return StreamSupport.stream(entitiesForRendering.spliterator(), true)
            .filter(entity -> NoRenderConfig.hiddenTypes.stream().noneMatch(type -> type.isInstance(entity)))
            .toList();
    }

    @WrapWithCondition(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;renderChunkLayer(Lnet/minecraft/client/renderer/RenderType;Lcom/mojang/blaze3d/vertex/PoseStack;DDDLcom/mojang/math/Matrix4f;)V"))
    private boolean noRender$hideBLocks(LevelRenderer levelRenderer, RenderType renderType, PoseStack poseStack, double cameraX, double cameraY, double cameraZ, Matrix4f positionMatrix) {
        return !NoRenderConfig.CONFIG.hideBlocks.get();
    }

    @ModifyExpressionValue(method = "renderLevel", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/LevelRenderer;globalBlockEntities:Ljava/util/Set;", ordinal = 1))
    private Set<BlockEntity> noRender$hideBlockEntities(Set<BlockEntity> globalBlockEntities) {
        if (NoRenderConfig.CONFIG.hideBlockEntities.get()) {
            return Collections.emptySet();
        }
        return globalBlockEntities;
    }

    @WrapWithCondition(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;renderSky(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/math/Matrix4f;FLnet/minecraft/client/Camera;ZLjava/lang/Runnable;)V"))
    private boolean noRender$hideSky(LevelRenderer levelRenderer, PoseStack poseStack, Matrix4f projectionMatrix, float tickDelta, Camera camera, boolean bl, Runnable runnable) {
        return !NoRenderConfig.CONFIG.hideSky.get();
    }

    @WrapWithCondition(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/ParticleEngine;render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;Lnet/minecraft/client/renderer/LightTexture;Lnet/minecraft/client/Camera;FLnet/minecraft/client/renderer/culling/Frustum;)V"), remap = false)
    private boolean noRender$hideParticles(ParticleEngine particleEngine, PoseStack poseStack, MultiBufferSource.BufferSource bufferSource, LightTexture lightTexture, Camera camera, float f, Frustum clippingHelper) {
        return !NoRenderConfig.CONFIG.hideParticles.get();
    }
}
