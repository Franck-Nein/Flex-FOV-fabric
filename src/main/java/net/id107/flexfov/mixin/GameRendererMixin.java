package net.id107.flexfov.mixin;

import net.id107.flexfov.mixinHelpers.GameRendererAdditions;
import net.id107.flexfov.projection.Projection;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;

@Mixin({GameRenderer.class})
public abstract class GameRendererMixin implements GameRendererAdditions {
	@Unique
	private MatrixStack handMatrices;
	@Unique
	private float handTickDelta;
	@Final
	@Shadow
	private Camera camera;

	@Redirect(
		method = {"render"},
		at = @At(
	value = "INVOKE",
	target = "Lnet/minecraft/client/render/GameRenderer;renderWorld(FJLnet/minecraft/client/util/math/MatrixStack;)V"
)
	)
	private void renderWorld(GameRenderer instance, float tickDelta, long limitTime, MatrixStack matrices) {
		Projection.getInstance().renderWorld(instance, tickDelta, limitTime, matrices);
	}

	@Inject(
		method = {"getFov"},
		at = {@At("RETURN")},
		cancellable = true
	)
	private void setFOV(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Double> cir) {
		cir.setReturnValue(Double.valueOf(Projection.getInstance().getPassFOV(((Double)cir.getReturnValue()).doubleValue())));
	}
	
	@ModifyVariable(
		method = {"renderWorld"},
		ordinal = 1,
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/render/GameRenderer;bobViewWhenHurt(Lnet/minecraft/client/util/math/MatrixStack;F)V"
		)
	)
	private MatrixStack updateCamera(MatrixStack matrixStack) {
		Projection.getInstance().rotateCamera(matrixStack);
		return matrixStack;
	}

	@Redirect(
		method = {"renderWorld"},
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/render/GameRenderer;renderHand(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/Camera;F)V"
		)
	)
	private void renderHand(GameRenderer instance, MatrixStack matrices, Camera camera, float tickDelta) {
		Projection projection = Projection.getInstance();
		if (projection.getShowHand() && projection.getResizeGui() && projection.renderPass == 0) {
			renderHand(matrices, camera, tickDelta);
		} else {
			if (projection.renderPass == 0) {
				handMatrices = matrices;
				handTickDelta = tickDelta;
			}

			matrices.peek().getPositionMatrix().loadIdentity();
		}
	}

	@ModifyArg(
		method = {"renderWorld"},
		at = @At(
	value = "INVOKE",
	target = "Lnet/minecraft/client/render/WorldRenderer;setupFrustum(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Matrix4f;)V"
),
		index = 0
	)
	private MatrixStack setupFrustum(MatrixStack matrices) {
		matrices.push();
		matrices.loadIdentity();
		MinecraftClient mc = MinecraftClient.getInstance();
		Entity entity = mc.getCameraEntity() == null ? mc.player : mc.getCameraEntity();
		Projection.getInstance().rotateCamera(matrices);
		if (mc.options.getPerspective().isFrontView()) {
			matrices.peek().getPositionMatrix().multiply(new Quaternion(new Vec3f(0.0F, 1.0F, 0.0F), 180.0F, true));
		}

		matrices.peek().getPositionMatrix().multiply(new Quaternion(((Entity)entity).getPitch(), ((Entity)entity).getYaw() + 180.0F, 0.0F, true));
		return matrices;
	}

	@ModifyArg(
		method = {"renderWorld"},
		at = @At(
	value = "INVOKE",
	target = "Lnet/minecraft/client/render/WorldRenderer;render(Lnet/minecraft/client/util/math/MatrixStack;FJZLnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/GameRenderer;Lnet/minecraft/client/render/LightmapTextureManager;Lnet/minecraft/util/math/Matrix4f;)V"
),
		index = 0
	)
	private MatrixStack worldRender(MatrixStack matrices) {
		matrices.pop();
		return matrices;
	}

	@Inject(
		method = {"render"},
		at = {@At("TAIL")}
	)
	private void saveGui(CallbackInfo ci) {
		if (MinecraftClient.getInstance().world != null) {
			Projection projection = Projection.getInstance();
			if (projection.getResizeGui() && projection.renderPass == 0) {
				projection.saveRenderPass();
				projection.loadUniforms();
				projection.runShader();
			}

		}
	}

	public void flexFOV$renderHand() {
		renderHand(handMatrices, camera, handTickDelta);
	}

	@Shadow
	private void renderHand(MatrixStack matrices, Camera camera, float tickDelta) {

	}
}
