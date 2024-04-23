package net.saint.crop_growth_modifier.mixin;

import static net.minecraft.util.math.MathHelper.clamp;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.tick.OrderedTick;
import net.saint.crop_growth_modifier.Mod;

@Mixin(CropBlock.class)
public abstract class CropBlockMixin {

	private int scheduledExtraRolls = 0;

	@Inject(method = "randomTick", at = @At("HEAD"), cancellable = true)
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random,
			CallbackInfo callbackInfo) {
		CropBlock block = (CropBlock) (Object) this;

		if (random.nextFloat() > Mod.config.cropTickChance) {
			callbackInfo.cancel();
		}

		if (random.nextFloat() <= Mod.config.cropExtraRollChance) {
			if (scheduledExtraRolls >= Mod.config.cropExtraRollMax) {
				scheduledExtraRolls = 0;
				return;
			}

			scheduledExtraRolls += 1;

			// Schedule extra random tick for block.
			OrderedTick<Block> extraBlockTick = OrderedTick.create(block, pos);
			world.getBlockTickScheduler().scheduleTick(extraBlockTick);
		} else {
			scheduledExtraRolls = 0;
		}
	}

	@Inject(method = "applyGrowth", at = @At("HEAD"), cancellable = true)
	public void applyGrowth(World world, BlockPos pos, BlockState state, CallbackInfo callbackInfo) {
		if (world.random.nextFloat() <= Mod.config.cropTickChance) {
			callbackInfo.cancel();
		}
	}
}
