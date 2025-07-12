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
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.math.Quaternion;
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
	target = "Lnet/minecraft/client/render/Camera;getRotation()Lnet/minecraft/util/math/Quaternion;"
)
	)
	private Quaternion rotateParticle(Camera instance) {
		if (!Projection.getInstance().shouldRotateParticles()) {
			return instance.getRotation();
		} else {
			MinecraftClient mc = MinecraftClient.getInstance();
			float tickDelta = mc.getTickDelta();
			Vec3d particlePos = new Vec3d(MathHelper.lerp((double)tickDelta, prevPosX, x), MathHelper.lerp((double)tickDelta, prevPosY, y), MathHelper.lerp((double)tickDelta, prevPosZ, z));
			Vec3d dir = mc.gameRenderer.getCamera().getPos().subtract(particlePos).normalize();
			Quaternion rotation = new Quaternion(0.0F, 0.0F, 0.0F, 1.0F);
			rotation.hamiltonProduct(Vec3f.POSITIVE_Y.getRadialQuaternion((float)Math.atan2(-dir.x, -dir.z)));
			rotation.hamiltonProduct(Vec3f.POSITIVE_X.getRadialQuaternion((float)Math.asin(dir.y)));
			return rotation;
		}
	}

	@Shadow
	public abstract void buildGeometry(VertexConsumer var1, Camera var2, float var3);

	public abstract ParticleTextureSheet getType();
}
