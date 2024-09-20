package net.saint.crop_growth_modifier.mixin;

import static net.minecraft.util.math.MathHelper.clamp;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.block.StemBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.saint.crop_growth_modifier.mixinlogic.StemBlockMixinLogic;

@Mixin(StemBlock.class)
public abstract class StemBlockMixin implements StemBlockMixinLogic {

	// Properties

	private int scheduledExtraRolls = 0;

	public int getScheduledExtraRolls() {
		return scheduledExtraRolls;
	}

	public void setScheduledExtraRolls(int scheduledExtraRolls) {
		this.scheduledExtraRolls = scheduledExtraRolls;
	}

	@Unique
	public int getMaxAge() {
		return StemBlock.MAX_AGE;
	}

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

	@Inject(method = "grow", at = @At("HEAD"), cancellable = true)
	private void grow(ServerWorld world, Random random, BlockPos position, BlockState state,
			CallbackInfo callbackInfo) {
		if (!shouldApplyGrowth(world, position, state)) {
			callbackInfo.cancel();
			return;
		}

		var growthAmount = getGrowthAmountForAllowedEvent(world);
		var updatedAge = clamp(state.get(StemBlock.AGE) + growthAmount, 0, getMaxAge());
		var updatedState = state.with(StemBlock.AGE, updatedAge);

		world.setBlockState(position, updatedState, 2);

		if (updatedAge == getMaxAge()) {
			state.randomTick(world, position, random);
		}
	}

}
