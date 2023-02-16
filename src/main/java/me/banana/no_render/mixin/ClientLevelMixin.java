package me.banana.no_render.mixin;

import me.banana.no_render.NoRenderConfig;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.LevelEntityGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collections;
import java.util.stream.StreamSupport;

@Mixin(ClientLevel.class)
public class ClientLevelMixin {
    @Shadow
    protected LevelEntityGetter<Entity> getEntities() {
        throw new UnsupportedOperationException();
    }

    /** this mixin modifies the entities to be rendered for a ClientLevel */
    @Inject(method = "entitiesForRendering", at = @At("HEAD"), cancellable = true)
    private void noRender$dontRenderEntities(CallbackInfoReturnable<Iterable<Entity>> cir) {
        if (NoRenderConfig.CONFIG.hideAllEntities.get()) {
            cir.setReturnValue(Collections.emptyList());
            return;
        }

        var entitiesToRender = StreamSupport.stream(getEntities().getAll().spliterator(), true)
            .filter(entity -> NoRenderConfig.hiddenTypes.stream().noneMatch(type -> type.isInstance(entity)))
            .toList();
        cir.setReturnValue(entitiesToRender);
    }
}
