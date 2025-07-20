package net.id107.flexfov.mixin;

import net.minecraft.client.render.Frustum;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//This is a really bad hotfix, please help
@Mixin(Frustum.class)
public class FrustumMixin {
    @Inject(
            method = "isVisible(Lnet/minecraft/util/math/Box;)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void forceEverythingToBeVisible(Box box, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }
}