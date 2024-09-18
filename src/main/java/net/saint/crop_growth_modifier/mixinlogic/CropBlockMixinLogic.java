package net.saint.crop_growth_modifier.mixinlogic;

import static net.minecraft.util.math.MathHelper.clamp;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.tick.OrderedTick;
import net.saint.crop_growth_modifier.Mod;

public interface CropBlockMixinLogic {
	
	public int getScheduledExtraRolls();
	public void setScheduledExtraRolls(int scheduledExtraRolls);

	public int getMaxAge();

	public default boolean shouldAllowRandomTick(CropBlock block, BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (random.nextFloat() > Mod.config.cropTickChance) {
			return false;
		}

		if (random.nextFloat() <= Mod.config.cropExtraRollChance) {
			var scheduledExtraRolls = getScheduledExtraRolls();

			if (scheduledExtraRolls >= Mod.config.cropExtraRollMax) {
				setScheduledExtraRolls(0);
				return true;
			}

			setScheduledExtraRolls(scheduledExtraRolls + 1);

			// Schedule extra random tick for block.
			OrderedTick<Block> extraBlockTick = OrderedTick.create(block, pos);
			world.getBlockTickScheduler().scheduleTick(extraBlockTick);

			return true;
		}

		setScheduledExtraRolls(0);
		return true;
	}

	public default boolean shouldApplyGrowth(World world, BlockPos pos, BlockState state) {
		return world.random.nextFloat() < Mod.config.cropTickChance;
	}

	public default int getGrowthAmountForAllowedEvent(World world) {
		var growthMin = clamp(Mod.config.cropGrowthStagesMin, 1, getMaxAge());
		var growthMax = clamp(Mod.config.cropGrowthStagesMax, growthMin + 1, getMaxAge() + 1);

		return world.random.nextBetween(growthMin, growthMax);
	}

}
