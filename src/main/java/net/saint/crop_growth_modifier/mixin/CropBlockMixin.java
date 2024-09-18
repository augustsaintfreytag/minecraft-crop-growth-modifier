package net.saint.crop_growth_modifier.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random,
			CallbackInfo callbackInfo) {
		CropBlock block = (CropBlock) (Object) this;

		var result = shouldAllowRandomTick(block, state, world, pos, random);

		if (!result) {
			callbackInfo.cancel();
		}
	}

	@Inject(method = "applyGrowth", at = @At("HEAD"), cancellable = true)
	public void applyGrowth(World world, BlockPos pos, BlockState state, CallbackInfo callbackInfo) {
		if (!shouldApplyGrowth(world, pos, state)) {
			callbackInfo.cancel();
		}
	}

	@Overwrite
	protected int getGrowthAmount(World world) {
		return getGrowthAmountForAllowedEvent(world);
	}

}
