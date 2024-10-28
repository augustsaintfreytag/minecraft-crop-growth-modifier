package net.saint.crop_growth_modifier.mixinlogic;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.saint.crop_growth_modifier.Mod;

public interface StemBlockMixinLogic extends GrowableBlockMixinLogic {

	public default boolean shouldAllowRandomTick(Block block, BlockState state, ServerWorld world, BlockPos pos,
			Random random) {
		var randomValue = random.nextFloat();

		if (randomValue > Mod.config.cropTickChance * Mod.config.stemBlockMultiplier) {
			return false;
		}

		return true;
	}

	public default boolean shouldApplyGrowth(World world, BlockPos pos, BlockState state) {
		return world.random.nextFloat() < Mod.config.cropTickChance * Mod.config.stemBlockMultiplier;
	}

}
