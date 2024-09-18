package net.saint.crop_growth_modifier.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.saint.crop_growth_modifier.mixinlogic.CowEntityMixinLogic;

@Mixin(CowEntity.class)
public abstract class CowEntityMixin implements CowEntityMixinLogic {

	// Logic

	@Inject(method = "interactMob", at = @At("HEAD"), cancellable = true)
	public void interactMob(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> callbackInfo) {
		var cowEntity = (CowEntity) (Object) this;
		var result = onInteractMob(cowEntity, player, hand);

		if (!result) {
			callbackInfo.setReturnValue(ActionResult.FAIL);
			callbackInfo.cancel();
			return;
		}
	}

}
