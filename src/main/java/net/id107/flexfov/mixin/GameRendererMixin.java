package net.id107.flexfov.mixin;

import net.id107.flexfov.mixinHelpers.GameRendererAdditions;
import net.id107.flexfov.projection.Projection;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin implements GameRendererAdditions {
	@Unique
	private Matrix4f handMatrix;
	@Unique
	private float handTickDelta;
	@Final
	@Shadow
	private Camera camera;
	@Shadow
	private void bobView(MatrixStack matrices, float tickDelta) {}
	@Final
	@Shadow
	private MinecraftClient client;

	@Redirect(
		method = {"render(FJZ)V"},
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/render/GameRenderer;renderWorld(FJ)V"
		)
	)
	private void renderWorld(GameRenderer instance, float tickDelta, long limitTime) {
		Projection.getInstance().renderWorld(instance, tickDelta, limitTime, new MatrixStack());
	}

	@Inject(
		method = {"getFov"},
		at = {@At("RETURN")},
		cancellable = true
	)
	private void setFOV(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Double> cir) {
		cir.setReturnValue(Projection.getInstance().getPassFOV(cir.getReturnValue()));
	}

	//cancel bob
	@Redirect(
			method = {"renderWorld(FJ)V"},
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/render/GameRenderer;bobView(Lnet/minecraft/client/util/math/MatrixStack;F)V"
			)
	)
	private void cancelBob(GameRenderer instance, MatrixStack matrices, float tickDelta){
	}

	@Inject(
		method = {"renderWorld(FJ)V"},
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/render/GameRenderer;tiltViewWhenHurt(Lnet/minecraft/client/util/math/MatrixStack;F)V"
			),
			locals = LocalCapture.CAPTURE_FAILHARD
	)
	private void updateCamera(float tickDelta, long limitTime, CallbackInfo ci, boolean bl, Camera camera, Entity entity, double d, Matrix4f matrix4f, MatrixStack matrixStack) {
		Projection.getInstance().rotateCamera(matrixStack);
		// Trigger bob
		if (this.client.options.getBobView().getValue()) {
			this.bobView(matrixStack, tickDelta);
		}
	}

	@Redirect(
		method = {"renderWorld(FJ)V"},
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/render/GameRenderer;renderHand(Lnet/minecraft/client/render/Camera;FLorg/joml/Matrix4f;)V"
		)
	)
	private void renderHand(GameRenderer instance, Camera camera, float tickDelta, Matrix4f matrix) {
		Projection projection = Projection.getInstance();
		if (projection.getShowHand() && projection.getResizeGui() && projection.renderPass == 0) {
			this.renderHand(camera, tickDelta, matrix);
		} else {
			if (projection.renderPass == 0) {
				this.handMatrix = matrix;
				this.handTickDelta = tickDelta;
			}

		}
	}

	@Inject(
		method = {"render(FJZ)V"},
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

	@Override
	public void flexFOV$renderHand() {
		renderHand(camera, handTickDelta, handMatrix);
	}
	@Shadow
	private void renderHand(Camera camera, float tickDelta, Matrix4f matrix) {
	}
}
