package net.saint.crop_growth_modifier.mixinlogic;

import static net.minecraft.util.math.MathHelper.clamp;

import net.minecraft.entity.passive.CowEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.random.Random;
import net.saint.crop_growth_modifier.Mod;

public interface AnimalEntityMixinLogic {

	public static final String NBT_KEY_MILK = "MilkAmount";

	// Properties

	public float getMilkAmount();

	public void setMilkAmount(float milkProductionAmount);

	public long getLastMilkProductionTime();

	public void setLastMilkProductionTime(long lastMilkProductionTime);

	// NBT

	public default void onWriteNbt(CowEntity cowEntity, NbtCompound nbt) {
		nbt.putFloat(NBT_KEY_MILK, this.getMilkAmount());
	}

	public default void onReadNbt(CowEntity cowEntity, NbtCompound nbt) {
		if (nbt.contains(NBT_KEY_MILK)) {
			setMilkAmount(nbt.getFloat(NBT_KEY_MILK));
		}
	}

	public default float getInitialRandomMilkAmount(CowEntity cowEntity) {
		var maxInitialMilkAmount = Mod.config.cowMilkProductionCapacity * Mod.config.cowMilkInitialRandomFraction;
		var initialMilkAmount = (float) (Random.createLocal().nextBetween(0, (int) maxInitialMilkAmount * 1000)
				/ 1000);

		return initialMilkAmount;
	}

	// Logic

	public default void onMobTick(CowEntity cowEntity) {
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
