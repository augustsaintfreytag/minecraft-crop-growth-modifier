package net.saint.crop_growth_modifier.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.nbt.NbtCompound;
import net.saint.crop_growth_modifier.mixinlogic.AnimalEntityMixinLogic;

@Mixin(AnimalEntity.class)
public abstract class AnimalEntityMixin implements AnimalEntityMixinLogic {

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

	// NBT

	@Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
	public void injectedWriteCustomDataToNbt(NbtCompound nbt, CallbackInfo callbackInfo) {
		if (!((Object) this instanceof CowEntity)) {
			return;
		}

		var cowEntity = (CowEntity) (Object) this;
		writeNbt(cowEntity, nbt);
	}

	@Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
	public void injectedReadCustomDataFromNbt(NbtCompound nbt, CallbackInfo callbackInfo) {
		if (!((Object) this instanceof CowEntity)) {
			return;
		}

		var cowEntity = (CowEntity) (Object) this;
		readNbt(cowEntity, nbt);
	}

	// Logic

	@Inject(method = "mobTick", at = @At("TAIL"))
	protected void injectedMobTick(CallbackInfo callbackInfo) {
		if (!((Object) this instanceof CowEntity)) {
			return;
		}

		var cowEntity = (CowEntity) (Object) this;
		mobTick(cowEntity);
	}

}
