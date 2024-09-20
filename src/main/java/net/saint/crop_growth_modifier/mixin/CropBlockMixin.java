package net.saint.crop_growth_modifier.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.saint.crop_growth_modifier.mixinlogic.CropBlockMixinLogic;

@Mixin(CropBlock.class)
public abstract class CropBlockMixin implements CropBlockMixinLogic {

	// Properties

	private int scheduledExtraRolls = 0;

	public int getScheduledExtraRolls() {
		return scheduledExtraRolls;
	}

	public void setScheduledExtraRolls(int scheduledExtraRolls) {
		this.scheduledExtraRolls = scheduledExtraRolls;
	}

	@Shadow
	public abstract int getMaxAge();

	// Logic

	@Inject(method = "randomTick", at = @At("HEAD"), cancellable = true)
	private void injectedRandomTick(BlockState state, ServerWorld world, BlockPos position, Random random,
			CallbackInfo callbackInfo) {
		CropBlock block = (CropBlock) (Object) this;
		if (!shouldAllowRandomTick(block, state, world, position, random)) {
			callbackInfo.cancel();
			return;
		}

		scheduleExtraRolls(block, state, world, position, random);
	}

	@Inject(method = "applyGrowth", at = @At("HEAD"), cancellable = true)
	private void injectedApplyGrowth(World world, BlockPos position, BlockState state, CallbackInfo callbackInfo) {
		if (!shouldApplyGrowth(world, position, state)) {
			callbackInfo.cancel();
			return;
		}
	}

	@Inject(method = "getGrowthAmount", at = @At("HEAD"), cancellable = true)
	private void injectedGetGrowthAmount(World world, CallbackInfoReturnable<Integer> callbackInfo) {
		var growthAmount = getGrowthAmountForAllowedEvent(world);
		callbackInfo.setReturnValue(growthAmount);
	}

}
