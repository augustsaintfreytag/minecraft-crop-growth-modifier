package net.saint.crop_growth_modifier.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.saint.crop_growth_modifier.Mod;
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

	// Init

	@Inject(method = "<init>", at = @At("TAIL"))
	public void AnimalEntity(EntityType<? extends AnimalEntity> entityType, World world, CallbackInfo callbackInfo) {
		if (!((Object) this instanceof CowEntity)) {
			return;
		}

		if (milkAmount == 0) {
			var maxInitialMilkAmount = Mod.config.cowMilkProductionCapacity * Mod.config.cowMilkInitialRandomFraction;
			var initialMilkAmount = (float) (Random.createLocal().nextBetween(0, (int) maxInitialMilkAmount * 1000)
					/ 1000);

			milkAmount = initialMilkAmount;
		}
	}

	// NBT

	@Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
	public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo callbackInfo) {
		if (!((Object) this instanceof CowEntity)) {
			return;
		}

		onWriteNbt(nbt);
	}

	@Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
	public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo callbackInfo) {
		if (!((Object) this instanceof CowEntity)) {
			return;
		}

		onReadNbt(nbt);
	}

	// Logic

	@Inject(method = "mobTick", at = @At("TAIL"))
	protected void mobTick(CallbackInfo callbackInfo) {
		if (!((Object) this instanceof CowEntity)) {
			return;
		}

		var cowEntity = (CowEntity) (Object) this;
		onMobTick(cowEntity);
	}

}
