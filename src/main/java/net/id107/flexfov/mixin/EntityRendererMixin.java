package net.id107.flexfov.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import net.id107.flexfov.projection.Projection;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.render.entity.EntityRenderer;

@Mixin({EntityRenderer.class})
public abstract class EntityRendererMixin {
	@Redirect(
		method = {"renderLabelIfPresent"},
		at = @At(
	value = "INVOKE",
	target = "Lnet/minecraft/client/util/math/MatrixStack;multiply(Lorg/joml/Quaternionf;)V"
)
	)
	private void rotateNameplate(MatrixStack instance, Quaternionf quaternion, Entity entity) {
		if (!Projection.getInstance().shouldRotateParticles()) {
			instance.multiply(quaternion);
		} else {
			MinecraftClient mc = MinecraftClient.getInstance();
			float tickDelta = mc.getTickDelta();
			Vec3d entityPos = (new Vec3d(MathHelper.lerp((double)tickDelta, entity.prevX, entity.getX()), MathHelper.lerp((double)tickDelta, entity.prevY, entity.getY()), MathHelper.lerp((double)tickDelta, entity.prevZ, entity.getZ()))).add(0.0, (double)(entity.getHeight() + 0.5F), 0.0);
			Vec3d dir = mc.gameRenderer.getCamera().getPos().subtract(entityPos).normalize();
			Quaternionf rotation = new Quaternionf(0.0F, 0.0F, 0.0F, 1.0F);
			rotation.mul(RotationAxis.POSITIVE_Y.rotation((float)Math.atan2(-dir.x, -dir.z)));
			rotation.mul(RotationAxis.POSITIVE_X.rotation((float)Math.asin(dir.y)));
			instance.multiply(rotation);
		}
	}
}
