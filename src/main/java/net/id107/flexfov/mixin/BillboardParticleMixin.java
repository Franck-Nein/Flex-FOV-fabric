package net.id107.flexfov.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import net.id107.flexfov.projection.Projection;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.BillboardParticle;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;
import net.minecraft.util.math.Vec3d;

@Mixin({BillboardParticle.class})
public abstract class BillboardParticleMixin extends Particle {
	protected BillboardParticleMixin(ClientWorld world, double x, double y, double z) {
		super(world, x, y, z);
	}

	@Redirect(
		method = {"buildGeometry"},
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/particle/BillboardParticle$Rotator;setRotation(Lorg/joml/Quaternionf;Lnet/minecraft/client/render/Camera;F)V"
		)
	)
	private void rotateParticle(BillboardParticle.Rotator rotator, Quaternionf rotation, Camera camera, float tickDelta) {
		if (!Projection.getInstance().shouldRotateParticles()) {
			rotator.setRotation(rotation, camera, tickDelta);
		} else {
			MinecraftClient mc = MinecraftClient.getInstance();
			Vec3d particlePos = new Vec3d(MathHelper.lerp(tickDelta, this.prevPosX, this.x), MathHelper.lerp(tickDelta, this.prevPosY, this.y), MathHelper.lerp(tickDelta, this.prevPosZ, this.z));
			Vec3d dir = mc.gameRenderer.getCamera().getPos().subtract(particlePos).normalize();
			rotation.set(0.0F, 0.0F, 0.0F, 1.0F);
			rotation.mul(RotationAxis.POSITIVE_Y.rotation((float) Math.atan2(-dir.x, -dir.z)));
			rotation.mul(RotationAxis.POSITIVE_X.rotation((float) Math.asin(dir.y)));
		}
	}

	@Shadow
	public abstract void buildGeometry(VertexConsumer var1, Camera var2, float var3);

	public abstract ParticleTextureSheet getType();
}
