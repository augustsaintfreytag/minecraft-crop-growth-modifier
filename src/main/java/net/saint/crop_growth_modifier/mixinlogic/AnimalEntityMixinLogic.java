package net.saint.crop_growth_modifier.mixinlogic;

import static net.minecraft.util.math.MathHelper.clamp;

import blue.endless.jankson.annotation.Nullable;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.saint.crop_growth_modifier.Mod;

public interface AnimalEntityMixinLogic {

	public static final String NBT_KEY_MILK = "MilkAmount";

	// Properties

	public float getMilkAmount();

	public void setMilkAmount(float milkProductionAmount);

	public long getLastMilkProductionTime();

	public void setLastMilkProductionTime(long lastMilkProductionTime);

	// NBT

	public default void writeNbt(CowEntity cowEntity, NbtCompound nbt) {
		if (!Mod.config.cowLimitedMilkProduction) {
			return;
		}

		nbt.putFloat(NBT_KEY_MILK, this.getMilkAmount());
	}

	public default void readNbt(CowEntity cowEntity, NbtCompound nbt) {
		if (!Mod.config.cowLimitedMilkProduction) {
			return;
		}

		if (nbt.contains(NBT_KEY_MILK)) {
			setMilkAmount(nbt.getFloat(NBT_KEY_MILK));
		}
	}
	// Logic

	public default void breedWithBaby(ServerWorld world, AnimalEntity other, @Nullable PassiveEntity baby) {
		var animalEntity = (AnimalEntity) (Object) this;
		var random = world.getRandom();

		var baseAnimalMultiplifer = 1 + random.nextFloat() * Mod.config.animalBreedingCooldownMultiplier;
		var baseAnimalCooldown = (int) (Mod.config.animalBreedingCooldown * baseAnimalMultiplifer);

		var otherAnimalMultiplifer = 1 + random.nextFloat() * Mod.config.animalBreedingCooldownMultiplier;
		var otherAnimalCooldown = (int) (Mod.config.animalBreedingCooldown * otherAnimalMultiplifer);

		animalEntity.setBreedingAge(baseAnimalCooldown);
		other.setBreedingAge(otherAnimalCooldown);
	}

	public default void mobTick(CowEntity cowEntity) {
		if (!Mod.config.cowLimitedMilkProduction) {
			return;
		}

		var time = cowEntity.getWorld().getTime();

		if (time - getLastMilkProductionTime() < 100) {
			return;
		}

		var milkProductionAmount = getMilkAmount();

		setLastMilkProductionTime(time);
		setMilkAmount(clamp(milkProductionAmount + Mod.config.cowMilkProductionPerHundredTicks, 0,
				Mod.config.cowMilkProductionCapacity));
	}

}
