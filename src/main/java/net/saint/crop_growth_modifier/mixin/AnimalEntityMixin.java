package net.saint.crop_growth_modifier.mixin;

import java.util.function.Consumer;
import java.util.function.Function;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import blue.endless.jankson.annotation.Nullable;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.saint.crop_growth_modifier.library.CowEntityMilkAccessor;
import net.saint.crop_growth_modifier.mixinlogic.AnimalEntityMixinLogic;

@Mixin(AnimalEntity.class)
public class AnimalEntityMixin implements AnimalEntityMixinLogic {

	// Properties

	public float getMilkAmount() {
		return returnWithCowEntityMilkAccess(cowEntity -> {
			return cowEntity.getMilkAmount();
		}, 0f);
	}

	public void setMilkAmount(float milkAmount) {
		withCowEntityMilkAccess(cowEntity -> {
			cowEntity.setMilkAmount(milkAmount);
		});
	}

	public long getLastMilkProductionTime() {
		return returnWithCowEntityMilkAccess(cowEntity -> {
			return cowEntity.getLastMilkProductionTime();
		}, 0L);
	}

	public void setLastMilkProductionTime(long lastMilkProductionTime) {
		withCowEntityMilkAccess(cowEntity -> {
			cowEntity.setLastMilkProductionTime(lastMilkProductionTime);
		});
	}

	// Init

	@Inject(method = "<init>", at = @At("TAIL"))
	public void injectedAnimalEntity(CallbackInfo callbackInfo) {
		initClientNetworking();
	}

	// NBT

	@Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
	public void injectedWriteCustomDataToNbt(NbtCompound nbt, CallbackInfo callbackInfo) {
		withCowEntity(cowEntity -> {
			writeNbt(cowEntity, nbt);
		});
	}

	@Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
	public void injectedReadCustomDataFromNbt(NbtCompound nbt, CallbackInfo callbackInfo) {
		withCowEntity(cowEntity -> {
			readNbt(cowEntity, nbt);
		});
	}

	// Tick

	@Inject(method = "mobTick", at = @At("TAIL"))
	private void injectedMobTick(CallbackInfo callbackInfo) {
		withCowEntity(cowEntity -> {
			mobTick(cowEntity);
		});
	}

	// @Inject(method = "interactMob", at = @At("HEAD"), cancellable = true)
	// private void injectedInteractMob(PlayerEntity player, Hand hand,
	// CallbackInfoReturnable<ActionResult> callbackInfo) {
	// var animal = (AnimalEntity) (Object) this;
	// var world = animal.getWorld();
	// var itemStack = player.getStackInHand(hand);

	// if (animal.isBreedingItem(itemStack)) {
	// int breedingAge = animal.getBreedingAge();
	// if (!world.isClient && breedingAge == 0 && animal.canEat()) {
	// if (world.getRandom().nextFloat() > Mod.config.animalBreedingChance) {
	// // Breeding chance roll failed, consume food and proceed.
	// this.eat(player, hand, itemStack);
	// callbackInfo.setReturnValue(ActionResult.CONSUME);
	// }
	// }
	// }
	// }

	// Breeding

	@Inject(method = "breed(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/passive/AnimalEntity;Lnet/minecraft/entity/passive/PassiveEntity;)V", at = @At("HEAD"))
	private void injectedBreedWithBaby(ServerWorld world, AnimalEntity other, @Nullable PassiveEntity baby,
			CallbackInfo callbackInfo) {
		breedWithBaby(world, other, baby);
	}

	// Convenience Access

	private void withCowEntity(Consumer<CowEntity> block) {
		if (!((Object) this instanceof CowEntity)) {
			return;
		}

		var cowEntity = (CowEntity) (Object) this;
		block.accept(cowEntity);
	}

	private void withCowEntityMilkAccess(Consumer<CowEntityMilkAccessor> block) {
		if (!((Object) this instanceof CowEntity)) {
			return;
		}

		var cowEntity = (CowEntityMilkAccessor) (Object) this;
		block.accept(cowEntity);
	}

	private <ReturnType> ReturnType returnWithCowEntityMilkAccess(Function<CowEntityMilkAccessor, ReturnType> block,
			ReturnType fallbackValue) {
		if (!((Object) this instanceof CowEntity)) {
			return fallbackValue;
		}

		var cowEntity = (CowEntityMilkAccessor) (Object) this;
		return block.apply(cowEntity);
	}

}
