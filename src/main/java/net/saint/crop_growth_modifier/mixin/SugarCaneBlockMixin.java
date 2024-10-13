import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.BlockState;
import net.minecraft.block.SugarCaneBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.saint.crop_growth_modifier.mixinlogic.SugarCaneBlockMixinLogic;

@Mixin(SugarCaneBlock.class)
public abstract class SugarCaneBlockMixin implements SugarCaneBlockMixinLogic {

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
		var block = (SugarCaneBlock) (Object) this;
		if (!shouldAllowRandomTick(block, state, world, position, random)) {
			callbackInfo.cancel();
			return;
		}

		scheduleExtraRolls(block, state, world, position, random);
	}

}
