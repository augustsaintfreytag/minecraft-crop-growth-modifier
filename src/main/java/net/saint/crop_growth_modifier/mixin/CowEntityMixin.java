package net.saint.crop_growth_modifier.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
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

@Mixin(value = CowEntity.class)
public abstract class CowEntityMixin implements CowEntityMixinLogic {

	// Properties

	private float milkAmount = 0;
	private long lastMilkProductionTime = 0;

	@Unique
	public float getMilkAmount() {
		return milkAmount;
	}

	@Unique
	public void setMilkAmount(float milkProductionAmount) {
		this.milkAmount = milkProductionAmount;
	}

	@Unique
	public long getLastMilkProductionTime() {
		return lastMilkProductionTime;
	}

	@Unique
	public void setLastMilkProductionTime(long lastMilkProductionTime) {
		this.lastMilkProductionTime = lastMilkProductionTime;
	}

	// Init

	@Inject(method = "<init>", at = @At("TAIL"))
	public void AnimalEntity(EntityType<? extends CowEntity> entityType, World world, CallbackInfo callbackInfo) {
		if (world.isClient) {
			// Client will not have access to NBT data and can not store properties.
			return;
		}

		var cowEntity = (CowEntity) (Object) this;
		var initialMilkAmount = getInitialRandomMilkAmount(cowEntity.getRandom(), cowEntity);

		setMilkAmount(initialMilkAmount);
	}

	// Logic

	@Inject(method = "interactMob", at = @At("HEAD"), cancellable = true)
	private void injectedInteractMob(PlayerEntity player, Hand hand,
			CallbackInfoReturnable<ActionResult> callbackInfo) {
		var cowEntity = (CowEntity) (Object) this;
		var world = cowEntity.getWorld();
		var result = onInteractMob(cowEntity, player, hand);

		switch (result) {
			case DidMilk:
				callbackInfo.setReturnValue(ActionResult.success(world.isClient));
				return;
			case DidCancelAndReport:
				callbackInfo.setReturnValue(ActionResult.success(world.isClient));
				return;
			case DidNothing:
				return;
		}
	}

}
