package net.saint.crop_growth_modifier.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.saint.crop_growth_modifier.mixinlogic.CowEntityMixinLogic;

@Mixin(CowEntity.class)
public abstract class CowEntityMixin implements CowEntityMixinLogic {

	// Init

	@Inject(method = "<init>", at = @At("TAIL"))
	public void AnimalEntity(EntityType<? extends CowEntity> entityType, World world, CallbackInfo callbackInfo) {
		var cowEntity = (CowEntity) (Object) this;
		var initialMilkAmount = getInitialRandomMilkAmount(world.random, cowEntity);

		setMilkAmount(initialMilkAmount);
	}

	// Logic

	@Inject(method = "interactMob", at = @At("HEAD"), cancellable = true)
	public void interactMob(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> callbackInfo) {
		var cowEntity = (CowEntity) (Object) this;
		var wantsEventCancel = onInteractMob(cowEntity, player, hand);

		if (wantsEventCancel) {
			callbackInfo.setReturnValue(ActionResult.CONSUME);
			return;
		}
	}

}
