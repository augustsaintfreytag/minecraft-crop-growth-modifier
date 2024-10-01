package net.saint.crop_growth_modifier.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.BambooSaplingBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.saint.crop_growth_modifier.mixinlogic.BambooSaplingBlockMixinLogic;

@Mixin(BambooSaplingBlock.class)
public class BambooSaplingBlockMixin implements BambooSaplingBlockMixinLogic {

	// Properties

	private int scheduledExtraRolls = 0;

	public int getScheduledExtraRolls() {
		return scheduledExtraRolls;
	}

	public void setScheduledExtraRolls(int scheduledExtraRolls) {
		this.scheduledExtraRolls = scheduledExtraRolls;
	}

	// Logic

	@Inject(method = "randomTick", at = @At("HEAD"), cancellable = true)
	private void injectedRandomTick(BlockState state, ServerWorld world, BlockPos position, Random random,
			CallbackInfo callbackInfo) {
		var block = (CropBlock) (Object) this;
		if (!shouldAllowRandomTick(block, state, world, position, random)) {
			callbackInfo.cancel();
			return;
		}

		scheduleExtraRolls(block, state, world, position, random);
	}

}
